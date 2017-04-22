package it.prochilo.salvatore.trovaildecimo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import it.prochilo.salvatore.trovaildecimo.models.Partita;

public class NuovaPartita extends AppCompatActivity {

    private static final String TAG = NuovaPartita.class.getCanonicalName();

    private EditText luogo, ora, minuti;
    private Button nuovaPartita;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuova_partita);
        luogo = (EditText) findViewById(R.id.nuova_partita_luogo);
        ora = (EditText) findViewById(R.id.nuova_partita_ora);
        minuti = (EditText) findViewById(R.id.nuova_partita_minuti);
        nuovaPartita = (Button) findViewById(R.id.nuova_partita_button);
        nuovaPartita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Partita partita = new Partita("1", 10)
                        .setLuogo(luogo.getText().toString())
                        .setTime(Integer.parseInt(ora.getText().toString()), Integer.parseInt(minuti.getText().toString()));
                Log.d(TAG, "Utente creato");
            }
        });
    }
}
