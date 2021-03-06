package it.prochilo.salvatore.trovaildecimo.fragments.matches;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.datamodels.Match;

public class MatchesInfoFragment extends Fragment {

    private Match mPartita;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_matches_info, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPartita = bundle.getParcelable(MatchesMainFragment.KEY_PARTITA_TAG);
        }
        // Set value of Layout
        final TextView orarioDetails = (TextView) layout.findViewById(R.id.orario_text);
        final TextView giornoDetails = (TextView) layout.findViewById(R.id.giorno_text);
        final TextView nomeCampoDetails = (TextView) layout.findViewById(R.id.nomecampo_text);
        final TextView numeroGiocatoriDetails = (TextView) layout.findViewById(R.id.numero_giocatori_text);
        final TextView tipoIncontro = (TextView) layout.findViewById(R.id.tipoincontro);
        final Button mViewMapButton = (Button) layout.findViewById(R.id.fragment_matche_info_button);
        mViewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        //orarioDetails.setText(mPartita.mOrarioIncontro.toString());
        //giornoDetails.setText(mPartita.mMatchDay.toString());
        //nomeCampoDetails.setText(mPartita.playingField.toString());
        numeroGiocatoriDetails.setText(String.valueOf(mPartita.numPlayer));
        tipoIncontro.setText(mPartita.challengeType.toString());

        //TEMPORANEO, UTILIZZATO UNICAMENTE PER VEDERE MOMENTAMENTE I PARTECIPANTI
        final TextView textView = (TextView) layout.findViewById(R.id.text_partecipanti_incontro);
        StringBuilder sb = new StringBuilder();
        //for (int i = 0; i < mPartita.listaPartecipanti.size(); i++) {
         //   sb.append(mPartita.listaPartecipanti.get(i)).append('\n');
       // }
        textView.setText(sb.toString());
        return layout;
    }
}
