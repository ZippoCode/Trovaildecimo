package it.prochilo.salvatore.trovaildecimo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.custom_view.CustomTextViewRobotoRegular;
import it.prochilo.salvatore.trovaildecimo.models.User;

public class ProfiloDetailsFragment extends Fragment {

    private User mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_profilo_details, container, false);
        setupUser();
        ((CustomTextViewRobotoRegular) layout.findViewById(R.id.user_name_text)).setText(mUser.name);
        ((CustomTextViewRobotoRegular) layout.findViewById(R.id.user_sunname_text)).setText(mUser.surname);
        ((CustomTextViewRobotoRegular) layout.findViewById(R.id.user_age_text)).setText(String.valueOf(mUser.age));
        ((CustomTextViewRobotoRegular) layout.findViewById(R.id.user_city_text)).setText(mUser.city);
        ((CustomTextViewRobotoRegular) layout.findViewById(R.id.user_role_text)).setText(mUser.role);
        ((CustomTextViewRobotoRegular) layout.findViewById(R.id.user_num_games_played_text)).setText(String.valueOf(mUser.numGamesPlayed));
        ((CustomTextViewRobotoRegular) layout.findViewById(R.id.user_feedbak_text)).setText((String.valueOf(mUser.feedback)));
        return layout;
    }

    private void setupUser() {
        mUser = new User("prochilo.salvatore@gmail.com", "Salvatore", "Prochilo")
                .addProprietas(24, "Taurianova", "Attaccante")
                .addFeedBack(4.5f);
    }
}
