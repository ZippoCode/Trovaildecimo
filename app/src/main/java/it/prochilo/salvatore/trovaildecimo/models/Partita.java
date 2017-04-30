package it.prochilo.salvatore.trovaildecimo.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Partita {


    public static List<Partita> list = new ArrayList<>();

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
     * L'organizzatore della partita
     */
    public final User mUser;
    /**
     * L'identificatore delle partite che deve essere univoco
     */
    public String id;
    /**
     * La lista dei partecipanti all'incontro
     */
    private List<User> listaPartecipanti;
    /**
     * Il numero di partecipanti della partita
     */
    public NumeroPartecipanti numeroPartecipanti;
    /**
     * Il nome del campo nel quale si disputerà la partita
     */
    public String mNomeCampo;
    /**
     * L'ora nella quale si disputerà la partita
     */
    public Ora mOra;
    /**
     * La rappresenta il giorno nel quale verrà giocato l'incontro
     */
    public Data mGiorno;
    /**
     *
     */
    public TipoIncontro mTipoIncontro;
    /**
     * Viene utilizzato per generare in automatico i numeri identificativi della partita
     */
    private Random random = new Random();


    public Partita(User user) {
        this.mUser = user;
        id = generaId();
    }

    public Partita setPartecipanti(int numPartecipanti) {
        listaPartecipanti = new ArrayList<>(numPartecipanti);
        numeroPartecipanti = getNumeroPartecipanti(numPartecipanti);
        return this;
    }

    public Partita setNomeCampo(final String nomeCampo) {
        this.mNomeCampo = nomeCampo;
        return this;
    }

    public Partita setTime(final int ora, final int minuti) {
        mOra = new Ora(ora, minuti);
        return this;
    }

    public Partita setGiorno(final int giorno, final int mese, final int anno) {
        mGiorno = new Data(giorno, mese, anno);
        return this;
    }


    public Partita setTipologia(TipoIncontro tipoIncontro) {
        this.mTipoIncontro = tipoIncontro;
        return this;
    }

    public Partita addPartecipante(User utente) {
        listaPartecipanti.add(utente);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: " + id);
        sb.append("Numero partecipanti: " + numeroPartecipanti.name());
        sb.append("Nome campetto: " + mNomeCampo);
        sb.append("Ora: " + mOra + " e Giorno: " + mGiorno);
        return sb.toString();
    }

    /**
     * Genera in automatico i numeri identificativi della partita
     */
    private String generaId() {
        return String.valueOf(random.nextInt(Integer.MAX_VALUE));
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
    public static class Ora {
        private int mOra, mMinuti;

        Ora(int ora, int minuti) {
            if ((ora < 0 || ora > 23) || (minuti < 0 || minuti > 59))
                throw new IllegalArgumentException("Formato ora errato");
            this.mOra = ora;
            this.mMinuti = minuti;
        }

        @Override
        public String toString() {
            return String.format("%02d", mOra) + ":" + String.format("%02d", mMinuti);
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

        @Override
        public String toString() {
            return nomeGiorno + " " + mGiorno + " " + nomeMese;
        }

    }
}
