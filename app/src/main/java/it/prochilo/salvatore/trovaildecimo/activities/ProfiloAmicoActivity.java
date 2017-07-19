package it.prochilo.salvatore.trovaildecimo.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.datamodels.User;

public class ProfiloAmicoActivity extends AppCompatActivity {

    private static User utente = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_amico);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_profilo_amico);
        if (utente != null) {
            mToolbar.setTitle(utente.name + " " + utente.surname);
            ((TextView) findViewById(R.id.nome_cognome_amico)).setText(utente.name + " " + utente.surname);
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void setUtente(User user) {
        utente = user;
    }

}