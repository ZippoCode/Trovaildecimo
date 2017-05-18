package it.prochilo.salvatore.trovaildecimo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.util.Utils;

public class SearchSoccerFieldFragment extends Fragment {

    SupportMapFragment mMapFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search_soccer_field, container, false);
        final Toolbar mToolbar = (Toolbar) layout.findViewById(R.id.fragment_search_soccer_field_toolbar);
        mToolbar.setTitle("Cerca un campo");
        Utils.setActionBarDrawerToggle(getActivity(), mToolbar);
        mMapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_search_soccer_field_map_anchor, mMapFragment)
                .commit();
        return layout;
    }
}
