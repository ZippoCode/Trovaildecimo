package it.prochilo.salvatore.trovaildecimo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.prochilo.salvatore.trovaildecimo.activities.MainActivity;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.util.Utils;

public class ImpostazioniFragment extends Fragment {

    private MainActivity mMainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_impostazioni, container, false);
        Toolbar mToolbar = (Toolbar) layout.findViewById(R.id.toolbar_fragment_impostazioni);
        mToolbar.setTitle("Impostazioni");
        Utils.setActionBarDrawerToggle(mMainActivity, mToolbar);
        return layout;
    }
}
