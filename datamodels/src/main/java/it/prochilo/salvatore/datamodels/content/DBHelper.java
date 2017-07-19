package it.prochilo.salvatore.datamodels.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

import it.prochilo.salvatore.datamodels.R;
import it.prochilo.salvatore.datamodels.content.cursor.DataCursorFactory;
import it.prochilo.salvatore.datamodels.util.ResourceUtils;

/**
 * Questa classe viene utilizzata per la gestione del Database. In particolare consente la creazione
 * e l'aggiornamento dello stesso.
 *
 * @author Zippo
 * @version 1.0
 */
public class DBHelper extends SQLiteOpenHelper {

    /**
     * Il TAG per il Log
     */
    private static final String TAG = DBHelper.class.getSimpleName();

    private Context mContext;

    public DBHelper(Context context) {
        super(context, DataDB.DB_NAME, new DataCursorFactory(), DataDB.DB_VERSION);
        this.mContext = context;
    }

    /**
     * Il metodo di callback viene invocato nell'eventualità nella quale il Database non dovesse
     * essere presente e ha il compito di crearlo
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL(ResourceUtils.getRawAsString(mContext, R.raw.create_schema_users));
            db.execSQL(ResourceUtils.getRawAsString(mContext, R.raw.create_schema_playing_fields));
            db.setTransactionSuccessful();
        } catch (IOException ioe) {
            Log.e(TAG, "Errore lettura del file per lo script per la creazione della tabella", ioe);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Viene invocato quando si ha la necessità di aggiornare il Dabatase
     *
     * @param db Il Database
     * @param oldVersion La vecchia versione
     * @param newVersion La nuova versione
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.beginTransaction();
            db.execSQL(ResourceUtils.getRawAsString(mContext,R.raw.drop_schema_users));
            db.execSQL(ResourceUtils.getRawAsString(mContext, R.raw.drop_schema_playing_fields));
            db.setTransactionSuccessful();
        } catch (IOException ioe) {
            Log.e(TAG, "Errore nella lettura del file raw per la aggiornamento del DB", ioe);
        } finally {
            db.endTransaction();
        }
    }


}
