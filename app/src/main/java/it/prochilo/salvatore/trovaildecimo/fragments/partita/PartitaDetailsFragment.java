package it.prochilo.salvatore.trovaildecimo.fragments.partita;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.Partita;

public class PartitaDetailsFragment extends Fragment {


    private Partita mPartita;

    public PartitaDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_partita_details, container, false);
        final Toolbar mToolbar = (Toolbar) layout.findViewById(R.id.toolbar_details_partite);
        mToolbar.setTitle("Dettagli partita");

        // Set value of Layout
        final TextView orarioDetails = (TextView) layout.findViewById(R.id.orario_text);
        final TextView giornoDetails = (TextView) layout.findViewById(R.id.giorno_text);
        final TextView nomeCampoDetails = (TextView) layout.findViewById(R.id.nomecampo_text);
        final TextView numeroGiocatoriDetails = (TextView) layout.findViewById(R.id.numero_giocatori_text);
        orarioDetails.setText(mPartita.mOrario.toString());
        giornoDetails.setText(mPartita.mData.toString());
        nomeCampoDetails.setText(mPartita.mNomeCampo);
        numeroGiocatoriDetails.setText(String.valueOf(mPartita.numeroPartecipanti));

        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setPartita(Partita partita) {
        this.mPartita = partita;
    }

}
