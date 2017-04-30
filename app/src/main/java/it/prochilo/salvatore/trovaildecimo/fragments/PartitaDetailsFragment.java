package it.prochilo.salvatore.trovaildecimo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.prochilo.salvatore.trovaildecimo.MainActivity;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.Partita;

public class PartitaDetailsFragment extends Fragment {

    private TextView orarioDetails, giornoDetails, nomeCampoDetails, numeroGiocatoriDetails;
    private Toolbar mToolbar;
    private MainActivity mMainActivity;

    private Partita partita;

    public PartitaDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_partita_details, container, false);
        mToolbar = (Toolbar) layout.findViewById(R.id.toolbar_details_partite);
        mToolbar.setTitle("Dettagli partita");

        orarioDetails = (TextView) layout.findViewById(R.id.orario_text);
        orarioDetails.setText(partita.mOra.toString());
        giornoDetails = (TextView) layout.findViewById(R.id.giorno_text);
        giornoDetails.setText(partita.mGiorno.toString());
        nomeCampoDetails = (TextView) layout.findViewById(R.id.nomecampo_text);
        nomeCampoDetails.setText(partita.mNomeCampo);
        numeroGiocatoriDetails = (TextView) layout.findViewById(R.id.numero_giocatori_text);
        numeroGiocatoriDetails.setText(String.valueOf(partita.numeroPartecipanti));
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setPartita(Partita partita) {
        this.partita = partita;
    }

}
