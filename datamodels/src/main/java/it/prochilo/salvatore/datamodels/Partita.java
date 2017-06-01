package it.prochilo.salvatore.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Partita implements Parcelable {

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
        String PARTECIPANTI = "partecipanti";
        String PARTECIPANTE = "partecipante num";
        String NUM_PLAYED_ADD = "numPartecipantiAggiunti";
        String MESSAGGI = "messaggi";
        String MESSAGGIO_NUM = "messaggio num";
        String NUM_MESSAGE = "numeroDiMessaggi";
    }

    public static final Creator<Partita> CREATOR = new Creator<Partita>() {
        @Override
        public Partita createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Partita[] newArray(int size) {
            return new Partita[0];
        }
    };

    /**
     * L'organizzatore della partita
     */
    public User mUser;
    /**
     * L'identificatore delle partite che deve essere univoco
     */
    public String mId;
    /**
     * La lista dei partecipanti all'incontro
     */
    public List<User> listaPartecipanti = new ArrayList<>();
    /**
     * Il numero di partecipanti della partita
     */
    public Integer numPartecipanti;
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
    public Data mDataIncontro;
    /**
     * Rappresenta l'orario nel quale è stata aggiunta la partita
     */
    public Time mOrarioAggiunta;
    /**
     * Rappresenta la data nella quale è stata aggiunta la partita
     */
    public Data mDataAggiunta;
    /**
     * La tipologia dell'incontropuò essere o normale o sfida
     */
    public String mTipoIncontro;
    /**
     * La durata dell'incontro
     */
    public Integer mDurata;
    /**
     * Il numero di giocatori che mancano al completamento del numero di giocatori
     */
    public Integer numMissingPlayer;
    /**
     * La lista dei messaggi che vengono scambiati dai i partecipanti all'incontro
     */
    public List<Message> mMessageList = new ArrayList<>();
    /**
     * Il numero di messaggi scambiati dai partecipanti
     */
    public Integer numMessaggi;

    //Ho bisogno di questo costruttore per Firebase
    public Partita() {

    }

    public Partita(final String id, final User user) {
        this.mId = id;
        this.mUser = user;
        mDataAggiunta = Data.getCurrentData();
        mOrarioAggiunta = Time.getCurrentTime();
        //Inizialmente setto l'orario dell'incontro come quelli correnti, sarà compito
        //dell'utente settarli nel modo adeguato
        mOrarioIncontro = Time.getCurrentTime();
        mDataIncontro = Data.getCurrentData();
        numMessaggi = 0;

    }

    @SuppressWarnings("unchecked")
    public Partita(Parcel parcel) {
        mId = parcel.readString();
        mUser = parcel.readParcelable(User.class.getClassLoader());
        numPartecipanti = parcel.readInt();
        mNomeCampo = parcel.readString();
        mOrarioIncontro = parcel.readParcelable(Time.class.getClassLoader());
        mDataIncontro = parcel.readParcelable(Data.class.getClassLoader());
        mDurata = parcel.readInt();
        mTipoIncontro = parcel.readString();
        listaPartecipanti = parcel.readArrayList(User.class.getClassLoader());
        mMessageList = parcel.readArrayList(Message.class.getClassLoader());
    }

    public Partita setNumeroPartecipanti(int numPartecipanti) {
        this.numPartecipanti = numPartecipanti;
        numMissingPlayer = this.numPartecipanti;
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
        numMissingPlayer--;
        return this;
    }

    public synchronized void addMessage(Message message) {
        numMessaggi++;
        mMessageList.add(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeParcelable(mUser, flags);
        dest.writeInt(numPartecipanti);
        dest.writeString(mNomeCampo);
        dest.writeParcelable(mOrarioIncontro, flags);
        dest.writeParcelable(mDataIncontro, flags);
        dest.writeInt(mDurata);
        dest.writeString(mTipoIncontro);
        dest.writeList(listaPartecipanti);
        dest.writeList(mMessageList);
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
        //Aggiungo la lista dei partecipanti
        jsonObject.put(KeysPartita.NUM_PLAYED_ADD, listaPartecipanti.size());
        JSONObject partecipanti = new JSONObject();
        for (int i = 0; i < listaPartecipanti.size(); i++) {
            partecipanti.put(KeysPartita.PARTECIPANTE + i, listaPartecipanti.get(i).toJson());
        }
        jsonObject.put(KeysPartita.PARTECIPANTI, partecipanti);
        //Aggiungo la lista dei messaggi
        jsonObject.put(KeysPartita.NUM_MESSAGE, numMessaggi);
        JSONObject messageList = new JSONObject();
        for (int i = 0; i < numMessaggi; i++) {
            messageList.put(KeysPartita.MESSAGGIO_NUM + i, mMessageList.get(i).toJson());
        }
        jsonObject.put(KeysPartita.MESSAGGI, messageList);
        return jsonObject;
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
        final Partita partita = new Partita(id, organizzatore)
                .setNomeCampo(nomeCampo)
                .setGiorno(data)
                .setTime(orario)
                .setMinutaggio(durataIncontro)
                .setNumeroPartecipanti(numeroPartecipanti)
                .setTipologia(tipoIncontro);
        //Ricavo la lista dei partecipanti e l'aggiungo alla partita
        final int numPlayedAdd = jsonObject.getInt(KeysPartita.NUM_PLAYED_ADD);
        final JSONObject partecipanti = jsonObject.getJSONObject(KeysPartita.PARTECIPANTI);
        for (int i = 0; i < numPlayedAdd; i++) {
            partita.addPartecipante(
                    User.fromJson(partecipanti.getJSONObject(KeysPartita.PARTECIPANTE + i)));
        }
        //Ricavo la lista dei messaggi e li aggiungo alla partita
        final int numMessaggi = jsonObject.getInt(KeysPartita.NUM_MESSAGE);
        final JSONObject messaggi = jsonObject.getJSONObject(KeysPartita.MESSAGGI);
        for (int i = 0; i < numMessaggi; i++) {
            partita.addMessage(
                    Message.fromJson(messaggi.getJSONObject(KeysPartita.MESSAGGIO_NUM + i)));
        }
        return partita;
    }

    public void writeToDatabaseReference() {
        FirebaseDatabase.getInstance().getReference("matches/".concat(mId)).setValue(Partita.this);
    }
}
