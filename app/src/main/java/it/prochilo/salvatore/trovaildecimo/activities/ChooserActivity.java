package it.prochilo.salvatore.trovaildecimo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import it.prochilo.salvatore.trovaildecimo.R;

/**
 * Questa è la prima classe che viene avviata quando l'utente effettua l'accesso e permette di
 * scegliere con quale metodo l'utente vuoi registrarsi all'applicazione. In questa prima versione
 * sono settati solamente due edittext e un bottone per prova, successivamente saranno implementate
 * le varie opzioni che permettono di accedere in modo diverso
 *
 * @author Zippo
 * @version 1.0
 */
public class ChooserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Visualizzo subito la Activity per loggarmi con Email e Password
        startActivity(new Intent(this, EmailPasswordActivity.class));
    }


}