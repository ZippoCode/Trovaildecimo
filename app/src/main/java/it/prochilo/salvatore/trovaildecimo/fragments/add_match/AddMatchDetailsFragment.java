package it.prochilo.salvatore.trovaildecimo.fragments.add_match;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import it.prochilo.salvatore.datamodels.Enumeration;
import it.prochilo.salvatore.datamodels.PlayingField;
import it.prochilo.salvatore.trovaildecimo.Dati;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.datamodels.Match;

public final class AddMatchDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = AddMatchDetailsFragment.class.getSimpleName();

    private static final Calendar CALENDAR = Calendar.getInstance();

    private static final int MIN_NUM_PARTECIPANT = 10;

    private TextView mOra, mData;

    private EditText luogo;

    private SeekBar mSeekBar;

    private TextView mTextSeekBar;

    private RadioButton mNormale, mSfida;

    private RadioButton mSessanta, mNovanta, mCentoVenti;

    private Match mMatch;

    private int numPartecipanti;

    private Calendar calendar = Calendar.getInstance();

    private int min, hour;

    private int day, month, year;

    private String nameCampo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_add_match_details, container, false);

        //Set Orario e Data
        mOra = (TextView) layout.findViewById(R.id.nuova_partita_orario);
        mData = (TextView) layout.findViewById(R.id.nuova_partita_data);
        mOra.setOnClickListener(this);
        mData.setOnClickListener(this);

        //Setto il luogo
        luogo = (EditText) layout.findViewById(R.id.nuova_partita_luogo);

        //Setto il numero di partecipanti
        mSeekBar = (SeekBar) layout.findViewById(R.id.numero_partecipanti_seekbar);
        mTextSeekBar = (TextView) layout.findViewById(R.id.numero_giocatori_seekbar_text);
        setupSeekBar();

        //Setto la tipologia di sfida
        mNormale = (RadioButton) layout.findViewById(R.id.normale_radio_button);
        mSfida = (RadioButton) layout.findViewById(R.id.sfida_radio_button);

        //Setto la durata della mMatch
        mSessanta = (RadioButton) layout.findViewById(R.id.sessanta_minuti_button);
        mNovanta = (RadioButton) layout.findViewById(R.id.novanta_minuti_button);
        mCentoVenti = (RadioButton) layout.findViewById(R.id.centoventi_minuti_button);

        final Button addPartecipant = (Button) layout.findViewById(R.id.fragment_add_match_details_button);
        addPartecipant.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nuova_partita_orario:
                setTimeMatch();
                break;
            case R.id.nuova_partita_data:
                setDayMatch();
                break;
            case R.id.fragment_add_match_details_button:
                setupMatch();

                //Passaggio dell paramentro mMatch appena creato
                Bundle bundle = new Bundle();
                bundle.putParcelable("partita", mMatch);
                Fragment nextFragment = new AddMatchUserFragment();
                nextFragment.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .addToBackStack(AddMatchDetailsFragment.TAG)
                        .replace(R.id.fragment_add_match_anchor, nextFragment)
                        .commit();
                break;
            default:
                throw new IllegalArgumentException("View ID: " + view.getId() + " non trovato");
        }
    }

    /**
     * Setto la setupSeekBar
     */
    private void setupSeekBar() {
        //Setto un valore iniziale nel caso nel quale l'utente non toccasse la barra
        numPartecipanti = MIN_NUM_PARTECIPANT;
        mTextSeekBar.setText(getString(R.string.numero_giocatori_text) + ": "
                + MIN_NUM_PARTECIPANT);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private int value;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Notifica che il livello di avanzamento è cambiato
                value = MIN_NUM_PARTECIPANT + progress * 2;
                mTextSeekBar.setText(getString(R.string.numero_giocatori_text) + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Notifica che l'utente ha iniziato la gesture sulla SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Notifica che l'utente ha arrestato la gesture sulla SeekBar
                numPartecipanti = value;
                mTextSeekBar.setText(getString(R.string.numero_giocatori_text) + ": " + value);
            }
        });
    }

    private void setTimeMatch() {
        int currentOra = CALENDAR.get(Calendar.HOUR_OF_DAY);
        int currentMinute = CALENDAR.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        min = i;
                        hour = i1;
                        mOra.setText(i + " " + i1);
                    }
                }, currentOra, currentMinute, true);
        timePickerDialog.show();
    }

    private void setDayMatch() {
        int currentGiorno = CALENDAR.get(Calendar.DAY_OF_MONTH);
        int currentMese = CALENDAR.get(Calendar.MONTH);
        int currentAnno = CALENDAR.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        year = i2;
                        month = i1;
                        day = i;
                        mData.setText(1 + "/" + i1 + "/" + i2);
                    }
                }, currentAnno, currentMese, currentGiorno);
        datePickerDialog.show();
    }

    private void setupMatch() {
        CALENDAR.set(year, month, day, hour, min);
        Calendar current = Calendar.getInstance();
        Enumeration.DurationMatch durationMatch = null;
        Enumeration.ChallangeType challangeType = null;
        if (mNormale.isChecked()) {
            challangeType = Enumeration.ChallangeType.NORMALE;
        } else if (mSfida.isChecked()) {
            challangeType = Enumeration.ChallangeType.SFIDA;
        }
        if (mSessanta.isChecked()) {
            durationMatch = Enumeration.DurationMatch.SESSANTA;
        } else if (mNovanta.isChecked()) {
            durationMatch = Enumeration.DurationMatch.NOVANTA;
        } else if (mCentoVenti.isChecked()) {
            durationMatch = Enumeration.DurationMatch.CENTOVENTI;
        }
        mMatch = Match.Builder.create(String.valueOf(new Random().nextInt(Integer.MAX_VALUE)),
                Dati.user,
                new Date(calendar.getTimeInMillis()),
                new Date(current.getTimeInMillis()),
                PlayingField.Builder.create("5151654984", luogo.getText().toString()).build())
                .withInfoMatch(numPartecipanti, durationMatch, challangeType)
                .build();


    }
}
