package it.prochilo.salvatore.datamodels.content.cursor;


import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteQuery;
import android.support.annotation.NonNull;

import it.prochilo.salvatore.datamodels.PlayingField;
import it.prochilo.salvatore.datamodels.content.DataDB;

/**
 * Questa classe contiene i metadati dei campi da gioco presenti all'interno dell'applicazione
 *
 * @author Zippo
 * @version 1.0
 * @see SQLiteCursor
 */
public final class PlayingFieldCursor extends SQLiteCursor {

    /**
     * L'indice della colonna che contiene gli identificativi dei campetti
     */
    private static int sPlayingFieldIdIndex = -1;
    /**
     * L'indice della colonna che contiene i nomi dei campetti
     */
    private static int sNameIndex;
    /**
     * L'indice della colonna che contiene la latitudine dei campetti
     */
    private static int sLatitudeIndex;
    /**
     * L'indice della colonna che contiene la longitudine dei campetti
     */
    private static int sLongitudeIndex;

    public PlayingFieldCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(driver, editTable, query);
        if (sPlayingFieldIdIndex == -1) {
            sPlayingFieldIdIndex = getColumnIndex(DataDB.PlayingField.ID);
            sNameIndex = getColumnIndex(DataDB.PlayingField.NAME);
            sLatitudeIndex = getColumnIndex(DataDB.PlayingField.LATITUDE);
            sLongitudeIndex = getColumnIndex(DataDB.PlayingField.LONGITUDE);
        }
    }

    /**
     * @return L'identificativo del campetto
     */
    public String getPlayingFieldId() {
        return getString(sPlayingFieldIdIndex);
    }

    /**
     * @return Il nome del campetto
     */
    public String getName() {
        return getString(sNameIndex);
    }

    /**
     * @return La latitudine del campetto
     */
    @NonNull
    public Float getLatitude() {
        return getFloat(sLatitudeIndex);
    }

    /**
     * @return La longitudine del campetto
     */
    @NonNull
    public Float getLongitude() {
        return getFloat(sLongitudeIndex);
    }

    /**
     * @return Il PlayingField
     */
    public PlayingField getPlayingField() {
        return PlayingField.Builder.create(getPlayingFieldId(), getName())
                .withLocation(getLatitude(), getLongitude())
                .build();
    }
}
