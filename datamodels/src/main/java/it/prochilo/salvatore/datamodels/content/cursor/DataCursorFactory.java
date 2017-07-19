package it.prochilo.salvatore.datamodels.content.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

import it.prochilo.salvatore.datamodels.content.DataDB;

/**
 * Questa classe implementa CursorFactory e rappresenta una costetualizzazione degli utenti
 *
 * @author Zippo
 * @version 1.0
 * @see Cursor
 * @see android.database.sqlite.SQLiteDatabase.CursorFactory
 */
public class DataCursorFactory implements SQLiteDatabase.CursorFactory {


    /**
     * Ha la responsabilità di creare l'implementazione del Cursor da utilizzare. In particolare
     * il metodo restituisce una specializzazione di una classe che estende SQLiteCursor e
     * ridefinsce la struttura del cursor
     *
     * @param db          Il Database nel quale sono contenute le tabelle
     * @param masterQuery
     * @param editTable   Il nome dell'tabella della quale si intende definire il cursor
     * @param query
     * @return Il Cursor relativo agli elementi identificati all'interno della tabella
     */
    @Override
    public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable,
                            SQLiteQuery query) {
        if (DataDB.Player.TABLE_NAME.equals(editTable)) {
            return new PlayerCursor(masterQuery, editTable, query);
        } else if (DataDB.PlayingField.TABLE_NAME.equals(editTable)) {
            return new PlayingFieldCursor(masterQuery, editTable, query);
        } else {
            throw new IllegalArgumentException("La tabella :" + editTable + " non è supportata");
        }
    }
}
