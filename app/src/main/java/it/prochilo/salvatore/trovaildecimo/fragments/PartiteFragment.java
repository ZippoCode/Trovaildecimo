package it.prochilo.salvatore.trovaildecimo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.prochilo.salvatore.trovaildecimo.MainActivity;
import it.prochilo.salvatore.trovaildecimo.R;

public class PartiteFragment extends Fragment {

    public static final String TAG = PartiteFragment.class.getSimpleName();

    private MainActivity mMainActivity;

    public PartiteFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMainActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_partite, container, false);
        mMainActivity.getSupportActionBar().setTitle(R.string.app_name);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity = null;
    }
}
