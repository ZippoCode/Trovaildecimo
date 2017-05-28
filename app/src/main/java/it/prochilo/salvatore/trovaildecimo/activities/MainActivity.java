package it.prochilo.salvatore.trovaildecimo.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.fragments.friends.FriendsMainFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.SearchSoccerFieldFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.SettingsFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.matches.MatchesMainFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.profilo.ProfiloFragment;
import it.prochilo.salvatore.trovaildecimo.location.LocationUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;

    private Button mLoginButton;

    private GoogleApiClient mGoogleApiClient;

    private volatile Location mCurrentLocation;

    private boolean mResolvingError = false;

    private final static int REQUEST_RESOLVE_ERROR = 1001;

    private final static int REQUEST_ACCESS_LOCATION = 2;

    private final static int UPDATE_LOCATION_REQUEST_CODE = 3;

    private final static String RESOLVING_ERROR_STATE_KEY = "RESOLVING_ERROR_STATE_KEY";

    private final static String DIALOG_ERROR_TAG = "dialog_error";

    private final static int MIN_DISPLACEMENT = 0;

    private final static long FASTEST_INTERVAL = 1000L;

    private final static long UPDATE_INTERVAL = 1000L;

    private final static int MAX_GEOCODE_RESULTS = 1;

    private final GoogleApiClient.ConnectionCallbacks mConnectionCallbacks =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {
                    //Se è stato concesso il permesso di avere la localizzazione viene
                    // richiamato questo metodo
                    manageLocationPermission();
                }

                @Override
                public void onConnectionSuspended(int i) {
                    Log.d(TAG, "");
                }
            };

    private final GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    if (mResolvingError) {
                        return;
                    } else if (connectionResult.hasResolution()) {
                        try {
                            mResolvingError = true;
                            connectionResult.startResolutionForResult(MainActivity.this, REQUEST_RESOLVE_ERROR);
                        } catch (IntentSender.SendIntentException e) {
                            mGoogleApiClient.connect();
                        }
                    } else {
                        showErrorDialog(connectionResult.getErrorCode());
                        mResolvingError = true;
                    }
                }
            };

    public static final class ErrorDialogFragment extends DialogFragment {

        public ErrorDialogFragment() {
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt(DIALOG_ERROR_TAG);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainActivity) getActivity()).onDialogDismissed();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            final MatchesMainFragment partiteFragment = new MatchesMainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.anchor_point, partiteFragment, MatchesMainFragment.TAG)
                    .commit();
        } else {
            mResolvingError = savedInstanceState.getBoolean(RESOLVING_ERROR_STATE_KEY, false);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mOnConnectionFailedListener)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        final PendingIntent callbackIntent = PendingIntent
                .getBroadcast(this, UPDATE_LOCATION_REQUEST_CODE,
                        LocationUtil.UPDATE_LOCATION_INTENT,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, callbackIntent);
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(RESOLVING_ERROR_STATE_KEY, mResolvingError);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startLocationListener();
            else {
                new AlertDialog.Builder(this)
                        .setTitle("Attenzione")
                        .setMessage("E' necessario autorizzare l'applicazione ad accedere alla" +
                                "geocalizzazione per poter permettere di cercare il campo")
                        .setPositiveButton("CAPITO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //L'applicazione verrà chiusa
                                finish();
                            }
                        })
                        .create()
                        .show();
            }
        }
    }

    /**
     * Usato dal NavigationDrawer per selezionare i vari Fragment dell'applicazione
     *
     * @param item Identifica l'istenza di MenuItem selezionata dall'utente
     * @return True
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "Selected: " + item);
        final int itemId = item.getItemId();
        final Fragment nextFragment;
        switch (itemId) {
            case R.id.home_menu:
                nextFragment = new MatchesMainFragment();
                break;
            case R.id.profilo_menu:
                nextFragment = new ProfiloFragment();
                break;
            case R.id.amici_menu:
                nextFragment = new FriendsMainFragment();
                break;
            case R.id.cerca_campo_menu:
                nextFragment = new SearchSoccerFieldFragment();
                break;
            case R.id.impostazioni_menu:
                nextFragment = new SettingsFragment();
                break;
            default:
                throw new IllegalArgumentException();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.anchor_point, nextFragment)
                .commit();
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(MatchesMainFragment.TAG)
                .replace(R.id.anchor_point, fragment)
                .commit();
    }

    private void showErrorDialog(int errorCode) {
        ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR_TAG, errorCode);
        errorDialogFragment.setArguments(args);
        errorDialogFragment.show(getSupportFragmentManager(), DIALOG_ERROR_TAG);
    }

    private void onDialogDismissed() {
        mResolvingError = false;
    }

    private void manageLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Attiva il GPS")
                        .setMessage("E' necessario attivare il GPS")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(
                                        MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_ACCESS_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_ACCESS_LOCATION);
            }
        } else {
            mCurrentLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            startLocationListener();
        }
    }


    private void startLocationListener() {
        updateLocation();
    }

    private void updateLocation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(FASTEST_INTERVAL)
                .setSmallestDisplacement(MIN_DISPLACEMENT)
                .setInterval(UPDATE_INTERVAL);
        final PendingIntent callbackIntent = PendingIntent
                .getBroadcast(this, UPDATE_LOCATION_REQUEST_CODE,
                        LocationUtil.UPDATE_LOCATION_INTENT, PendingIntent.FLAG_UPDATE_CURRENT);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient,
                            locationRequest, callbackIntent);
    }

    private void geoCodeLocation(final Location location) {
        if (location != null) {
            //Verifichiamo se il Geocoder è presente
            if (Geocoder.isPresent()) {
                final GeoCoderAsyncTask geoCoderAsyncTask =
                        new GeoCoderAsyncTask(this, MAX_GEOCODE_RESULTS);
                geoCoderAsyncTask.execute(location);
            } else {
                //In questo caso il Geocoder non è disponibile
                Toast.makeText(this, "Il Geocoder non è disponibile", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Log.w(TAG, "Nessuna Location per il geocode");
            Toast.makeText(this, "Nessuna locazione disponibile", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * DA ELIMINARE: CAZZATELLE
     *
     * @param addressesText
     */
    private void updateTextFromAddresses(final String addressesText) {
        if (!addressesText.isEmpty())
            mLoginButton.setText(addressesText);
        else
            mLoginButton.setText("Login");

    }

    /**
     * Utilizzato per trovare il nome della vita tramite un Location
     */
    private static final class GeoCoderAsyncTask extends AsyncTask<Location, Void, List<Address>> {

        private WeakReference<MainActivity> mActivityRef;
        private int mMaxResult;

        private GeoCoderAsyncTask(final MainActivity context, final int maxResult) {
            mActivityRef = new WeakReference<MainActivity>(context);
            mMaxResult = maxResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            final MainActivity activity = mActivityRef.get();
            //CAZZATELLE
            activity.updateTextFromAddresses("Login");
        }

        @Override
        protected List<Address> doInBackground(Location... params) {
            //Se il Context non è disponibile skippiamo
            final MainActivity activity = mActivityRef.get();
            if (activity == null) {
                Log.w(TAG, "Il Context è null");
                return null;
            }
            //Creiamo un'istanza di Geocoder
            final Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
            //Abbiamo La Location del geocoder
            final Location location = params[0];
            Log.d(TAG, "" + location);
            //Otteniamo gli indirizzi delel vide dalla Location
            List<Address> geoAddresses = null;
            try {
                geoAddresses = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(),
                        mMaxResult);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Errore; non è possibile ottenere gli indirizzi dalla locazione: " + location);
            }
            return geoAddresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            super.onPostExecute(addresses);
            final MainActivity activity = mActivityRef.get();
            if (activity != null && addresses != null && addresses.size() > 0) {
                final Address address = addresses.get(0);
                final StringBuilder geoCoding = new StringBuilder();
                final int maxIndex = address.getMaxAddressLineIndex();
                for (int i = 0; i <= maxIndex; i++) {
                    geoCoding.append(address.getAddressLine(i));
                    if (i < maxIndex) {
                        geoCoding.append(", ");
                    }
                }
                activity.updateTextFromAddresses(geoCoding.toString());
            } else {
                Log.w(TAG, "I dati del Geocoder non sono disponibili");
                if (activity != null) {
                    Toast.makeText(activity, "No Geocodec disponibile", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

}
