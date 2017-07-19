package it.prochilo.salvatore.datamodels.content.cursor;

import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteQuery;

import java.util.Date;

import it.prochilo.salvatore.datamodels.Player;
import it.prochilo.salvatore.datamodels.content.DataDB;

/**
 * Questo è la classe che implementa il cursor per le persone all'interno dell'applicazione
 *
 * @author Zippo
 * @version 1.0
 * @see SQLiteCursor
 */

public final class PlayerCursor extends SQLiteCursor {
    /**
     * L'indice della colonna relativa all'ID dell'utente
     */
    private static int sPlayerIdIndex = -1;
    /**
     * L'indice della colonna relativa al nome dell'utente
     */
    private static int sNameIndex;
    /**
     * L'indice della colonna relativa al cognome dell'utente
     */
    private static int sSurnameIndex;
    /**
     * L'indice della colonna relativa all'età dell'utente
     */
    private static int sBirthDayIndex;
    /**
     * L'indice della colonna relativa alla città dell'utente
     */
    private static int sCityIndex;
    /**
     * L'indice della colonna relativa al ruolo dell'utente
     */
    private static int sRoleIndex;
    /**
     * L'indice della colonna relativa al numero di incontri giocati dell'utente
     */
    private static int sNumPlayedGameIndex;
    /**
     * L'indice della colonna relativa al feedback dell'utente
     */
    private static int sFeedback;

    /**
     * Il costruttore
     *
     * @param driver
     * @param editTable
     * @param query
     */
    public PlayerCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(driver, editTable, query);
        //Si inizializzo i numeri delle colonne della tabella che saranno necessari per accedere
        //ai dati della tabella
        if (sPlayerIdIndex == -1) {
            //In queste circostanze bisogna inizializzare gli indici
            sPlayerIdIndex = getColumnIndex(DataDB.Player.PLAYER_ID);
            sNameIndex = getColumnIndex(DataDB.Player.NAME);
            sSurnameIndex = getColumnIndex(DataDB.Player.SURNAME);
            sBirthDayIndex = getColumnIndex(DataDB.Player.BIRTHDAY);
            sCityIndex = getColumnIndex(DataDB.Player.CITY);
            sRoleIndex = getColumnIndex(DataDB.Player.ROLE);
            sNumPlayedGameIndex = getColumnIndex(DataDB.Player.NUM_PLAYED_GAME);
            sFeedback = getColumnIndex(DataDB.Player.FEEDBACK);
        }
    }

    /**
     * @return L'ID corrente
     */
    public String getPlayerId() {
        return getString(sPlayerIdIndex);
    }

    /**
     * @return Il nome corrente
     */
    public String getName() {
        return getString(sNameIndex);
    }

    /**
     * @return Il cognome corrente
     */
    public String getSurname() {
        return getString(sSurnameIndex);
    }

    /**
     * @return L'età corrente
     */
    public long getBirthday() {
        return getLong(sBirthDayIndex);
    }

    /**
     * @return La città corrente
     */
    public String getCity() {
        return getString(sCityIndex);
    }

    /**
     * @return Il ruolo corrente
     */
    public String getRole() {
        return getString(sRoleIndex);
    }

    /**
     * @return Il numero di incontri giocatori corrente
     */
    public int getNumPlayedGame() {
        return getInt(sNumPlayedGameIndex);
    }

    /**
     * @return Il feedback corrente
     */
    public float getFeedback() {
        return getFloat(sFeedback);
    }

    /**
     * @return L'utente corrente
     */
    public Player getPlayer() {
        return Player.Builder.create(getPlayerId(), getName(), getSurname())
                .withInfoPerson(new Date(getBirthday()), getCity(), getRole())
                .withGamePerson(getNumPlayedGame(), getFeedback())
                .build();
    }

}
