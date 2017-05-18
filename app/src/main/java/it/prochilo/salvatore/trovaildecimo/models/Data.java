package it.prochilo.salvatore.trovaildecimo.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

public final class Data {

    public final int mGiorno, mMese, mAnno;

    public final String nomeGiorno;

    public final String nomeMese;

    private interface KeysData {
        String GIORNO = "giorno";
        String MESE = "mese";
        String ANNO = "anno";
    }

    public Data(final int giorno, final int mese, final int anno) {
        if (giorno < 0 || giorno > 31 || mese < 0 || mese > 12)
            throw new IllegalArgumentException("Formata data errato");
        this.mGiorno = giorno;
        this.mMese = mese;
        this.mAnno = anno;
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(anno, mese, giorno);
        nomeGiorno = mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ITALIAN);
        nomeMese = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ITALIAN);
    }

    public static Data fromJson(JSONObject jsonObject) throws JSONException {
        final int giorno = jsonObject.getInt(KeysData.GIORNO);
        final int mese = jsonObject.getInt(KeysData.MESE);
        final int anno = jsonObject.getInt(KeysData.ANNO);
        return new Data(giorno, mese, anno);
    }

    public JSONObject toJson() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(KeysData.GIORNO, mGiorno);
        jsonObject.put(KeysData.MESE, mMese);
        jsonObject.put(KeysData.ANNO, mAnno);
        return jsonObject;
    }

    public static Data getCurrentData() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        return new Data(currentDay, currentMonth, currentYear);
    }

    @Override
    public String toString() {
        return nomeGiorno + " " + mGiorno + " " + nomeMese;
    }

}
