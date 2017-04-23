package it.prochilo.salvatore.trovaildecimo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import it.prochilo.salvatore.trovaildecimo.models.Partita;

public class NuovaPartita extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = NuovaPartita.class.getCanonicalName();

    private Partita partita = new Partita();
    private Toolbar toolbar;
    private EditText luogo, numeroPartecipanti;
    private TextView ora, data;
    private Button nuovaPartita;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuova_partita);

        toolbar = (Toolbar) findViewById(R.id.nuova_partita_toolbar);
        toolbar.setTitle("Crea una nuova partita");
        setSupportActionBar(toolbar);

        luogo = (EditText) findViewById(R.id.nuova_partita_luogo);
        numeroPartecipanti = (EditText) findViewById(R.id.nuova_partita_numero_partecipanti);

        ora = (TextView) findViewById(R.id.nuova_partita_orario);
        data = (TextView) findViewById(R.id.nuova_partita_data);
        ora.setOnClickListener(this);
        data.setOnClickListener(this);

        nuovaPartita = (Button) findViewById(R.id.nuova_partita_button);
    }

    @Override
    public void onClick(View view) {
        final Calendar calendar = Calendar.getInstance();
        switch (view.getId()) {
            case R.id.nuova_partita_orario:
                int currentOra = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        ora.setText(i + ":" + i1);
                        partita = partita.setTime(i, i1);
                    }
                }, currentOra, currentMinute, true);
                timePickerDialog.show();
                break;
            case R.id.nuova_partita_data:
                int currentGiorno = calendar.get(Calendar.DAY_OF_MONTH);
                int currentMese = calendar.get(Calendar.MONTH);
                int currentAnno = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        data.setText(i2 + "/" + i1++ + "/" + i);
                        partita = partita.setGiorno(i2, i1, i);
                    }
                }, currentAnno, currentMese, currentGiorno);
                datePickerDialog.show();
                break;
            case R.id.nuova_partita_button:
                partita = partita.setPartecipanti(Integer.parseInt(numeroPartecipanti.getText().toString()))
                        .setNomeCampo(luogo.getText().toString());
                Log.d(TAG, "" + partita);
                break;

        }
    }
}
