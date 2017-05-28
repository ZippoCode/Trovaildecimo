package it.prochilo.salvatore.trovaildecimo.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import it.prochilo.salvatore.trovaildecimo.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String TAG = SettingsFragment.class.getSimpleName();


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }
}
