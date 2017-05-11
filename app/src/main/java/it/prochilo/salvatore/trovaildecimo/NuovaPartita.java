package it.prochilo.salvatore.trovaildecimo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

import it.prochilo.salvatore.trovaildecimo.models.Partita;
import it.prochilo.salvatore.trovaildecimo.models.User;

public class NuovaPartita extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = NuovaPartita.class.getCanonicalName();

    User organizzatore = new User("prochilo.salvatore@gmail.com", "Salvatore", "Prochilo")
            .addProprietas(24, "Taurianova", "Attaccante");

    private Partita partita = new Partita(organizzatore);
    private EditText luogo;

    private SeekBar mSeekBar;
    private TextView mTextSeekBar;
    private RadioButton mNormale, mSfida;
    private RadioButton mSessanta, mNovanta, mCentoVenti;
    private TextView mOra, mData;

    private Button nuovaPartita;

    final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_partita);

        //Set Default Time and Data. Saranno cambiati quando l'utente selezionerà i valore voluti
        partita.setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        partita.setGiorno(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        final Toolbar toolbar = (Toolbar) findViewById(R.id.nuova_partita_toolbar);
        toolbar.setTitle("Crea una nuova partita");
        setSupportActionBar(toolbar);

        //Set Orario e Data
        mOra = (TextView) findViewById(R.id.nuova_partita_orario);
        mData = (TextView) findViewById(R.id.nuova_partita_data);
        mOra.setText(partita.mOra.toString());
        mData.setText(partita.mGiorno.toString());

        mOra.setOnClickListener(this);
        mData.setOnClickListener(this);


        luogo = (EditText) findViewById(R.id.nuova_partita_luogo);

        mSeekBar = (SeekBar) findViewById(R.id.numero_partecipanti_seekbar);
        mTextSeekBar = (TextView) findViewById(R.id.numero_giocatori_seekbar_text);
        setupSeekBar();

        mNormale = (RadioButton) findViewById(R.id.normale_radio_button);
        mSfida = (RadioButton) findViewById(R.id.sfida_radio_button);

        mSessanta = (RadioButton) findViewById(R.id.sessanta_minuti_button);
        mNovanta = (RadioButton) findViewById(R.id.novanta_minuti_button);
        mCentoVenti = (RadioButton) findViewById(R.id.centoventi_minuti_button);

        nuovaPartita = (Button) findViewById(R.id.nuova_partita_button);
        nuovaPartita.setOnClickListener(this);
    }

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nuova_partita_orario:
                int currentOra = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        partita = partita.setTime(i, i1);
                        mOra.setText(partita.mOra.toString());
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
                        partita = partita.setGiorno(i2, i1, i);
                        mData.setText(partita.mGiorno.toString());
                    }
                }, currentAnno, currentMese, currentGiorno);
                datePickerDialog.show();
                break;
            case R.id.nuova_partita_button:
                partita = partita.setNomeCampo(luogo.getText().toString());
                if (mNovanta.isChecked()) {
                    partita.setTipologia(Partita.TipoIncontro.NORMALE);
                } else if (mSfida.isChecked()) {
                    partita.setTipologia(Partita.TipoIncontro.SFIDA);
                }
                if (mSessanta.isChecked()) {
                    partita.setMinutaggio(60);
                } else if (mNovanta.isChecked()) {
                    partita.setMinutaggio(90);
                } else if (mCentoVenti.isChecked()) {
                    partita.setMinutaggio(120);
                }
                returnToPartiteFragment(partita);
                break;

        }
    }


    private void setupSeekBar() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            final int MINVALUE = 10;
            int value;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = MINVALUE + progress * 2;
                mTextSeekBar.setText("Numero partecipanti: " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                partita.setPartecipanti(value);
                mTextSeekBar.setText("Numero partecipanti: " + value);
            }
        });
    }

    public void returnToPartiteFragment(Partita partita) {
        Partita.list.add(partita);
        startActivity(new Intent(this, MainActivity.class));
    }
}
