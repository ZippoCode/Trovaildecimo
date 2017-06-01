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
import java.util.Random;

import it.prochilo.salvatore.trovaildecimo.Dati;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.Data;
import it.prochilo.salvatore.trovaildecimo.models.Partita;
import it.prochilo.salvatore.trovaildecimo.models.Time;

public final class AddMatchDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = AddMatchDetailsFragment.class.getSimpleName();

    private static final Calendar CALENDAR = Calendar.getInstance();

    private static final int MIN_NUM_PARTECIPANT = 10;

    private Partita mMatch = new Partita(
            String.valueOf(new Random().nextInt(Integer.MAX_VALUE)),
            Dati.user);

    private TextView mOra, mData;

    private EditText luogo;

    private SeekBar mSeekBar;

    private TextView mTextSeekBar;

    private RadioButton mNormale, mSfida;

    private RadioButton mSessanta, mNovanta, mCentoVenti;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_add_match_details, container, false);

        //Set Orario e Data
        mOra = (TextView) layout.findViewById(R.id.nuova_partita_orario);
        mData = (TextView) layout.findViewById(R.id.nuova_partita_data);
        mOra.setText(mMatch.mOrarioIncontro.toString());
        mData.setText(mMatch.mDataIncontro.toString());
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
        mMatch.setNumeroPartecipanti(MIN_NUM_PARTECIPANT);
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
                mMatch.setNumeroPartecipanti(value);
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
                        mMatch = mMatch.setTime(new Time(i, i1));
                        mOra.setText(mMatch.mOrarioIncontro.toString());
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
                        mMatch = mMatch.setGiorno(new Data(i2, i1, i));
                        mData.setText(mMatch.mDataIncontro.toString());
                    }
                }, currentAnno, currentMese, currentGiorno);
        datePickerDialog.show();
    }

    private void setupMatch() {
        mMatch = mMatch.setNomeCampo(luogo.getText().toString());
        if (mNormale.isChecked()) {
            mMatch.setTipologia("Normale");
        } else if (mSfida.isChecked()) {
            mMatch.setTipologia("Sfida");
        }
        if (mSessanta.isChecked()) {
            mMatch.setMinutaggio(60);
        } else if (mNovanta.isChecked()) {
            mMatch.setMinutaggio(90);
        } else if (mCentoVenti.isChecked()) {
            mMatch.setMinutaggio(120);
        }
    }
}
