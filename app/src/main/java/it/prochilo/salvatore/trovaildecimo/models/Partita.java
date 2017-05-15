package it.prochilo.salvatore.trovaildecimo.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Partita {

    /**
     * Rappresenta il numero dei partecipanti della partita
     */
    private enum NumeroPartecipanti {
        DIECI, DODICI, QUATTORDICI, SEDICI, DICIOTTO, VENTI, VENTIDUE
    }

    /**
     * Indica la tipologia dell'incontro
     */
    public enum TipoIncontro {
        NORMALE, SFIDA
    }

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
    public NumeroPartecipanti numeroPartecipanti;
    public int numPartecipanti;
    /**
     * Il nome del campo nel quale si disputerà la partita
     */
    public String mNomeCampo;
    /**
     * L'ora nella quale si disputerà la partita
     */
    public Orario mOrario;
    /**
     * La rappresenta il giorno nel quale verrà giocato l'incontro
     */
    public Data mData;
    /**
     *
     */
    public TipoIncontro mTipoIncontro;
    /**
     *
     */
    public int mDurata;

    public Partita(final String id, final User user) {
        this.mId = id;
        this.mUser = user;
    }

    public Partita setNumeroPartecipanti(int numPartecipanti) {
        listaPartecipanti = new ArrayList<>(numPartecipanti);
        this.numPartecipanti = numPartecipanti;
        numeroPartecipanti = getNumeroPartecipanti(numPartecipanti);
        return this;
    }

    public Partita setNomeCampo(final String nomeCampo) {
        this.mNomeCampo = nomeCampo;
        return this;
    }

    public Partita setTime(final int ora, final int minuti) {
        mOrario = new Orario(ora, minuti);
        return this;
    }

    public Partita setGiorno(final int giorno, final int mese, final int anno) {
        mData = new Data(giorno, mese, anno);
        return this;
    }


    public Partita setTipologia(TipoIncontro tipoIncontro) {
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
        final Orario orario = Orario.fromJson(jsonObject.getJSONObject(KeysPartita.ORARIO));
        final Data data = Data.fromJson(jsonObject.getJSONObject(KeysPartita.DATA));
        final int durataIncontro = jsonObject.getInt(KeysPartita.DURATAINCONTRO);

        return new Partita(id, organizzatore)
                .setNomeCampo(nomeCampo)
                .setGiorno(data.mGiorno, data.mMese, data.mAnno)
                .setTime(orario.mOra, orario.mMinuto)
                .setMinutaggio(durataIncontro);
    }


    public JSONObject toJson() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(KeysPartita.ID, mId);
        jsonObject.put(KeysPartita.ORGANIZZATORE, mUser.toJson());
        jsonObject.put(KeysPartita.NUMEROPARTECIPANTI, numPartecipanti);
        jsonObject.put(KeysPartita.NOMECAMPO, mNomeCampo);
        jsonObject.put(KeysPartita.ORARIO, mOrario.toJson());
        jsonObject.put(KeysPartita.DATA, mData.toJson());
        jsonObject.put(KeysPartita.DURATAINCONTRO, mDurata);
        return jsonObject;
    }

    private NumeroPartecipanti getNumeroPartecipanti(int numero) {
        switch (numero) {
            case 10:
                return NumeroPartecipanti.DIECI;
            case 12:
                return NumeroPartecipanti.DODICI;
            case 14:
                return NumeroPartecipanti.QUATTORDICI;
            case 16:
                return NumeroPartecipanti.SEDICI;
            case 18:
                return NumeroPartecipanti.DICIOTTO;
            case 20:
                return NumeroPartecipanti.VENTI;
            case 22:
                return NumeroPartecipanti.VENTIDUE;
            default:
                throw new IllegalArgumentException("Numero di partecipanti errato");
        }
    }

    /**
     * La classe rappresenta l'ora nel quale si disputerà l'incontro
     */
    public static class Orario {
        private int mOra, mMinuto;

        private interface KeysOrario{
            String ORA = "ora";
            String MINUTO = "minuto";
        }

        Orario(int ora, int minuto) {
            if ((ora < 0 || ora > 23) || (minuto < 0 || minuto > 59))
                throw new IllegalArgumentException("Formato ora errato");
            this.mOra = ora;
            this.mMinuto = minuto;
        }

        private static Orario fromJson(final JSONObject jsonObject) throws JSONException {
            final int ora = jsonObject.getInt(KeysOrario.ORA);
            final int minuto = jsonObject.getInt(KeysOrario.MINUTO);
            return new Orario(ora, minuto);
        }

        private JSONObject toJson() throws JSONException {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(KeysOrario.ORA, mOra);
            jsonObject.put(KeysOrario.MINUTO, mMinuto);
            return jsonObject;
        }

        @Override
        public String toString() {
            return String.format("%02d", mOra) + ":" + String.format("%02d", mMinuto);
        }
    }

    /**
     * La classe rappresenta la data nella quale verrà giocata la partita
     */
    public static class Data {

        private final int mGiorno, mMese, mAnno;
        private final Calendar calendar;
        private final String nomeGiorno;
        private final String nomeMese;

        private interface  KeysData{
            String GIORNO = "giorno";
            String MESE = "mese";
            String ANNO = "anno";
        }
        Data(final int giorno, final int mese, final int anno) {
            if (giorno < 0 || giorno > 31 || mese < 0 || mese > 12)
                throw new IllegalArgumentException("Formata data errato");
            this.mGiorno = giorno;
            this.mMese = mese;
            this.mAnno = anno;
            calendar = Calendar.getInstance();
            calendar.set(anno, mese, giorno);
            nomeGiorno = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ITALIAN);
            nomeMese = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ITALIAN);
        }

        private static Data fromJson(JSONObject jsonObject) throws JSONException {
            final int giorno = jsonObject.getInt(KeysData.GIORNO);
            final int mese = jsonObject.getInt(KeysData.MESE);
            final int anno = jsonObject.getInt(KeysData.ANNO);
            return new Data(giorno, mese, anno);
        }

        private JSONObject toJson() throws JSONException {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(KeysData.GIORNO, mGiorno);
            jsonObject.put(KeysData.MESE, mMese);
            jsonObject.put(KeysData.ANNO, mAnno);
            return jsonObject;
        }

        @Override
        public String toString() {
            return nomeGiorno + " " + mGiorno + " " + nomeMese;
        }

    }
}
