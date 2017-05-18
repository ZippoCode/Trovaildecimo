package it.prochilo.salvatore.trovaildecimo.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Partita {

    /**
     * Chiavi per le proprietà
     */
    interface KeysPartita {
        String ID = "id";
        String ORGANIZZATORE = "organizzatore";
        String NOMECAMPO = "nomeCampo";
        String NUMEROPARTECIPANTI = "numeroPartecipanti";
        String DURATAINCONTRO = "durataIncontro";
        String DATA = "data";
        String ORARIO = "orario";
        String TIPOINCONTRO = "tipoIncontro";
    }

    /**
     * L'organizzatore della partita
     */
    public final User mUser;
    /**
     * L'identificatore delle partite che deve essere univoco
     */
    public String mId;
    /**
     * La lista dei partecipanti all'incontro
     */
    private List<User> listaPartecipanti;
    /**
     * Il numero di partecipanti della partita
     */
    public int numPartecipanti;
    /**
     * Il nome del campo nel quale si disputerà la partita
     */
    public String mNomeCampo;
    /**
     * L'ora nella quale si disputerà la partita
     */
    public Time mOrarioIncontro;
    /**
     * La rappresenta il giorno nel quale verrà giocato l'incontro
     */

    public Time mOrarioAggiunta;

    public Data mDataAggiunta;

    public Data mDataIncontro;
    /**
     * La tipologia dell'Incontro è può essere o normale o sfida
     */
    public String mTipoIncontro;
    /**
     * La durata dell'incontro
     */
    public int mDurata;

    private int numMissingPlayer;

    public Partita(final String id, final User user) {
        this.mId = id;
        this.mUser = user;
        mDataAggiunta = Data.getCurrentData();
        mOrarioAggiunta = Time.getCurrentTime();
    }

    public Partita setNumeroPartecipanti(int numPartecipanti) {
        listaPartecipanti = new ArrayList<>(numPartecipanti);
        this.numPartecipanti = numPartecipanti;
        return this;
    }

    public Partita setNomeCampo(final String nomeCampo) {
        this.mNomeCampo = nomeCampo;
        return this;
    }

    public Partita setTime(final Time orario) {
        mOrarioIncontro = orario;
        return this;
    }

    public Partita setGiorno(final Data data) {
        mDataIncontro = data;
        return this;
    }


    public Partita setTipologia(String tipoIncontro) {
        this.mTipoIncontro = tipoIncontro;
        return this;
    }

    public Partita setMinutaggio(int durata) {
        mDurata = durata;
        return this;
    }

    public Partita addPartecipante(User utente) {
        listaPartecipanti.add(utente);
        return this;
    }

    public static Partita fromJson(final JSONObject jsonObject) throws JSONException {
        final String id = jsonObject.getString(KeysPartita.ID);
        final User organizzatore = User.fromJson(jsonObject.getJSONObject(KeysPartita.ORGANIZZATORE));
        final int numeroPartecipanti = jsonObject.getInt(KeysPartita.NUMEROPARTECIPANTI);
        final String nomeCampo = jsonObject.getString(KeysPartita.NOMECAMPO);
        final Time orario = Time.fromJson(jsonObject.getJSONObject(KeysPartita.ORARIO));
        final Data data = Data.fromJson(jsonObject.getJSONObject(KeysPartita.DATA));
        final int durataIncontro = jsonObject.getInt(KeysPartita.DURATAINCONTRO);
        final String tipoIncontro = jsonObject.getString(KeysPartita.TIPOINCONTRO);

        return new Partita(id, organizzatore)
                .setNomeCampo(nomeCampo)
                .setGiorno(data)
                .setTime(orario)
                .setMinutaggio(durataIncontro)
                .setNumeroPartecipanti(numeroPartecipanti)
                .setTipologia(tipoIncontro);
    }

    public JSONObject toJson() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(KeysPartita.ID, mId);
        jsonObject.put(KeysPartita.ORGANIZZATORE, mUser.toJson());
        jsonObject.put(KeysPartita.NUMEROPARTECIPANTI, numPartecipanti);
        jsonObject.put(KeysPartita.NOMECAMPO, mNomeCampo);
        jsonObject.put(KeysPartita.ORARIO, mOrarioIncontro.toJson());
        jsonObject.put(KeysPartita.DATA, mDataIncontro.toJson());
        jsonObject.put(KeysPartita.DURATAINCONTRO, mDurata);
        jsonObject.put(KeysPartita.TIPOINCONTRO, mTipoIncontro);
        return jsonObject;
    }

}
