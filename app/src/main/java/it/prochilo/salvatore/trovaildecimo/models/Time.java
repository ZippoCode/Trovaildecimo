package it.prochilo.salvatore.trovaildecimo.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Rappresenta l'orario di una partita. La classe contiene due parametri di tipo intero
 * che rappresentato rispettivamente l'ora e il minuto.
 *
 * @version 1.0
 */
public final class Time {

    private final int mHour, mMinute;

    private interface KeysOrario {
        String HOUR = "ora";
        String MINUTE = "minuto";
    }

    public Time(final int hour, final int minute) {
        if ((hour < 0 || hour > 23) || (minute < 0 || minute > 59))
            throw new IllegalArgumentException("Formato ora errato");
        this.mHour = hour;
        this.mMinute = minute;
    }

    public static Time fromJson(final JSONObject jsonObject) throws JSONException {
        final int ora = jsonObject.getInt(KeysOrario.HOUR);
        final int minuto = jsonObject.getInt(KeysOrario.MINUTE);
        return new Time(ora, minuto);
    }

    public JSONObject toJson() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(KeysOrario.HOUR, mHour);
        jsonObject.put(KeysOrario.MINUTE, mMinute);
        return jsonObject;
    }

    @Override
    public String toString() {
        return String.format("%02d", mHour) + ":" + String.format("%02d", mMinute);
    }

    public static Time getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return new Time(hours, minutes);
    }
}
