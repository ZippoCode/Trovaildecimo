package it.prochilo.salvatore.trovaildecimo.fragments;

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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.activities.MainActivity;
import it.prochilo.salvatore.trovaildecimo.location.LocationAwareFragment;
import it.prochilo.salvatore.trovaildecimo.location.LocationUtil;
import it.prochilo.salvatore.trovaildecimo.util.Utils;

public class SearchSoccerFieldFragment extends LocationAwareFragment
        implements OnMapReadyCallback {

    private static final String TAG = SearchSoccerFieldFragment.class.getSimpleName();

    private SupportMapFragment mMapFragment;

    private GoogleMap mGoogleMap;

    private static final float DEFAULT_ZOOM = 16.0f;

    private static final int MAP_ANIMATION_DURATION = 400;

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

    private final static int RESULT_OK = 0;

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
                            connectionResult.startResolutionForResult(getActivity(), REQUEST_RESOLVE_ERROR);
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
            ((SearchSoccerFieldFragment) getParentFragment()).onDialogDismissed();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search_soccer_field, container, false);
        final Toolbar mToolbar = (Toolbar) layout.findViewById(R.id.fragment_search_soccer_field_toolbar);
        mToolbar.setTitle("Cerca un campo");
        Utils.setActionBarDrawerToggle(getActivity(), mToolbar);
        return layout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mResolvingError = savedInstanceState.getBoolean(RESOLVING_ERROR_STATE_KEY, false);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mOnConnectionFailedListener)
                .build();

        mMapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_search_soccer_field_map_anchor, mMapFragment)
                .commit();
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mResolvingError)
            mGoogleApiClient.connect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(RESOLVING_ERROR_STATE_KEY, mResolvingError);
    }

    @Override
    public void onStop() {
        final PendingIntent callbackIntent = PendingIntent
                .getBroadcast(getContext(), UPDATE_LOCATION_REQUEST_CODE,
                        LocationUtil.UPDATE_LOCATION_INTENT,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, callbackIntent);
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startLocationListener();
            else {
                new AlertDialog.Builder(getContext())
                        .setTitle("Attenzione")
                        .setMessage("E' necessario autorizzare l'applicazione ad accedere alla" +
                                "geocalizzazione per poter permettere di cercare il campo")
                        .setPositiveButton("CAPITO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //L'applicazione verrà chiusa
                                getActivity().finish();
                            }
                        })
                        .create()
                        .show();
            }
        }
    }

    private void showErrorDialog(int errorCode) {
        ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR_TAG, errorCode);
        errorDialogFragment.setArguments(args);
        errorDialogFragment.show(getFragmentManager(), DIALOG_ERROR_TAG);
    }

    private void onDialogDismissed() {
        mResolvingError = false;
    }

    private void manageLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Attiva il GPS")
                        .setMessage("E' necessario attivare il GPS")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(
                                        getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_ACCESS_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(
                        getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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
                .getBroadcast(getContext(), UPDATE_LOCATION_REQUEST_CODE,
                        LocationUtil.UPDATE_LOCATION_INTENT, PendingIntent.FLAG_UPDATE_CURRENT);
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient,
                            locationRequest, callbackIntent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        //Visualizza la propria posizione sulla mappa
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onLocationAvaiable(Location location) {
        super.onLocationAvaiable(location);
        showLocationMap(location);
    }

    private void showLocationMap(final Location location) {
        if (location == null || mGoogleMap == null)
            return;
        final LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        final CameraPosition cameraPosition = CameraPosition.builder()
                .target(currentLatLng)
                .zoom(DEFAULT_ZOOM)
                .bearing(0)
                .target(currentLatLng)
                .tilt(0)
                .build();
        final CameraUpdate cameraUpdate = CameraUpdateFactory
                .newCameraPosition(cameraPosition);
        mGoogleMap.moveCamera(cameraUpdate);
    }

}
