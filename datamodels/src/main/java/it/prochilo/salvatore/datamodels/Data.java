package it.prochilo.salvatore.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

/**
 * Rappresenta la Data di un incontro
 *
 * @version 1.1
 */
public final class Data implements Parcelable {

    private interface KeysData {
        String GIORNO = "giorno";
        String MESE = "mese";
        String ANNO = "anno";
    }

    public int mGiorno, mMese, mAnno;

    public String nomeGiorno;

    public String nomeMese;

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[0];
        }
    };

    //Necessarrio per FirebaseDatabase
    public Data() {

    }

    public Data(final int giorno, final int mese, final int anno) {
        if (giorno < 0 || giorno > 31 || mese < 0 || mese > 12)
            throw new IllegalArgumentException("Formata data errato");
        this.mGiorno = giorno;
        this.mMese = mese;
        this.mAnno = anno;
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(mAnno, mMese, mGiorno);
        nomeGiorno = mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ITALIAN);
        nomeMese = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ITALIAN);
    }

    public Data(Parcel parcel) {
        mGiorno = parcel.readInt();
        mMese = parcel.readInt();
        mAnno = parcel.readInt();
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(mGiorno, mMese, mGiorno);
        nomeGiorno = mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ITALIAN);
        nomeMese = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ITALIAN);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mGiorno);
        dest.writeInt(mMese);
        dest.writeInt(mAnno);
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
