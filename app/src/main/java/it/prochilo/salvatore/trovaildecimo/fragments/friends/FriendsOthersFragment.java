package it.prochilo.salvatore.trovaildecimo.fragments.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.prochilo.salvatore.trovaildecimo.R;

public class FriendsOthersFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_friends_other, container, false);
        return layout;
    }
}
