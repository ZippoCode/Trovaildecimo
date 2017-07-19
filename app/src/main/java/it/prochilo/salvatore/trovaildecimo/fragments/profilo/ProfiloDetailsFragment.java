package it.prochilo.salvatore.trovaildecimo.fragments.profilo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.prochilo.salvatore.trovaildecimo.Dati;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.datamodels.User;

public class ProfiloDetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_profilo_details, container, false);
        final User mUser = Dati.user;
        ((TextView) layout.findViewById(R.id.user_name_text)).setText(mUser.name);
        ((TextView) layout.findViewById(R.id.user_sunname_text)).setText(mUser.surname);
        ((TextView) layout.findViewById(R.id.user_age_text)).setText(String.valueOf(mUser.birthday));
        ((TextView) layout.findViewById(R.id.user_city_text)).setText(mUser.city);
        ((TextView) layout.findViewById(R.id.user_role_text)).setText(mUser.role);
        ((TextView) layout.findViewById(R.id.user_num_games_played_text)).setText(String.valueOf(mUser.numPlayedGame));
        ((TextView) layout.findViewById(R.id.user_feedbak_text)).setText((String.valueOf(mUser.feedback)));
        return layout;
    }

}
