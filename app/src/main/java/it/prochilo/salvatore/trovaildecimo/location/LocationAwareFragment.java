package it.prochilo.salvatore.trovaildecimo.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderApi;

public class LocationAwareFragment extends Fragment {

    private class LocationBroadcastReceiver extends BroadcastReceiver {

        private boolean notLoop = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            final Location location = intent
                    .getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);
            mCurrentLocation = location;
            if (!notLoop) {
                onLocationAvaiable(location);
                notLoop = true;
            }
        }
    }

    private volatile Location mCurrentLocation = null;

    private LocationBroadcastReceiver mLocationBroadcastReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationBroadcastReceiver = new LocationBroadcastReceiver();
    }

    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter intentFilter =
                new IntentFilter(LocationUtil.LOCATION_UPDATE_ACTION);
        getContext().registerReceiver(mLocationBroadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(mLocationBroadcastReceiver);
    }

    protected Location getLocation() {
        if (mLocationBroadcastReceiver != null) {
            return mCurrentLocation;
        } else {
            return null;
        }
    }

    protected void onLocationAvaiable(final Location location) {

    }
}
