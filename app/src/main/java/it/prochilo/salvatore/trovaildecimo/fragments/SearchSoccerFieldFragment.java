package it.prochilo.salvatore.trovaildecimo.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.location.LocationAwareFragment;
import it.prochilo.salvatore.trovaildecimo.util.Utils;

public class SearchSoccerFieldFragment extends LocationAwareFragment
        implements OnMapReadyCallback {

    private static final String TAG = SearchSoccerFieldFragment.class.getSimpleName();
    private SupportMapFragment mMapFragment;
    private GoogleMap mGoogleMap;
    private static final float DEFAULT_ZOOM = 16.0f;
    private static final int MAP_ANIMATION_DURATION = 400;

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
        mMapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_search_soccer_field_map_anchor, mMapFragment)
                .commit();
        mMapFragment.getMapAsync(this);
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
