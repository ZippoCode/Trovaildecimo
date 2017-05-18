package it.prochilo.salvatore.trovaildecimo.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import it.prochilo.salvatore.trovaildecimo.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;

    private volatile Location mCurrentLocation;

    private boolean mResolvingError = false;

    private static final int REQUEST_RESOLVE_ERROR = 1001;

    private static final String DIALOG_ERROR_TAG = "dialog_error";

    private static final String RESOLVING_ERROR_STATE_KEY = "RESOLVING_ERROR_STATE_KEY";

    private static final int REQUEST_ACCESS_LOCATION = 2;


    private static final String LOCATION_UPDATE_ACTION = "it.prochilo.salvatore.trovaildecimo.LOCATION_UPDATE";

    /**
     *
     */
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

    /**
     *
     */
    private final GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    if (mResolvingError) {
                        return;
                    } else if (connectionResult.hasResolution()) {
                        try {
                            mResolvingError = true;
                            connectionResult.startResolutionForResult(LoginActivity.this, REQUEST_RESOLVE_ERROR);
                        } catch (IntentSender.SendIntentException e) {
                            mGoogleApiClient.connect();
                        }
                    } else {
                        showErrorDialog(connectionResult.getErrorCode());
                        mResolvingError = true;
                    }
                }
            };

    /**
     *
     */
    public static final class ErrorDialogFragment extends DialogFragment {

        public ErrorDialogFragment() {
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR_TAG);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((LoginActivity) getActivity()).onDialogDismissed();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
        } else {
            mResolvingError = savedInstanceState.getBoolean(RESOLVING_ERROR_STATE_KEY, false);
        }

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.activity_login_toolbar);
        mToolbar.setTitle("Effettua il login");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mOnConnectionFailedListener)
                .build();
        final Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(RESOLVING_ERROR_STATE_KEY, mResolvingError);
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
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
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
                                        LoginActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_ACCESS_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_ACCESS_LOCATION);
            }
        } else {
            mCurrentLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            Log.d(TAG, "Location: " + mCurrentLocation);
        }
    }

}
