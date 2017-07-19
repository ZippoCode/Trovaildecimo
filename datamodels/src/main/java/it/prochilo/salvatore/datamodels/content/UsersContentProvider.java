package it.prochilo.salvatore.datamodels.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Zippo
 * @version 1.0
 */
public class UsersContentProvider extends ContentProvider {

    /**
     * Consente di riconoscere in maniera efficiente le tipologie di Uri che un ContentProvider può
     * gestire, associando dei valori numerici a un insieme di pattern che l'Uri può soddisfare. Il
     * valore costante NO_MATCH è un intero con valore -1 che è lo stesso valore che UriMatcher
     * restituirà nel caso in cui venisse confrontato con un URI che non soddisfa alcuna regola delle
     * regole registrate
     */
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int OTHER_USERS_DIR_INDICATOR = 1;
    private static final int OTHER_USERS_ITEM_INDICATOR = 2;

    static {
        URI_MATCHER.addURI(DataDB.AUTHORITY, DataDB.Player.PATH, OTHER_USERS_DIR_INDICATOR);
        URI_MATCHER.addURI(DataDB.AUTHORITY, DataDB.Player.PATH + "/#",
                OTHER_USERS_ITEM_INDICATOR);
    }

    /**
     * L'implementazione del SQLiteOpenHelper il quale ha il compito di creaze il DB
     */
    private DBHelper mDbHelper;

    /**
     * Ha la responsabilità di creare il Database nel quale verranno memorizzate le informazioni.
     *
     * @return un valore booleano che identifica se l'operazione è andata a buon fine oppure no.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new DBHelper(getContext());
        return false;
    }

    /**
     * @param uri L'Uri
     * @return Il valore del mime-type associato all?Uri
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case OTHER_USERS_DIR_INDICATOR:
                return DataDB.Player.MIME_TYPE_DIR;
            case OTHER_USERS_ITEM_INDICATOR:
                return DataDB.Player.MIME_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("L'Uri: " + uri + "è sconosciuto per il " +
                        "ContentProvider");
        }
    }

    /**
     * E' associata a una richiesta simile ad una SELECT
     *
     * @param uri           L'identificativo della risorsa da estrarre
     * @param projection    L'elenco di colonen da inserire nel cursore. Se null vengono incluse tutte
     * @param selection     Un criterio di selezione per le righe, se null vengono selezionate tutte
     * @param selectionArgs I valori della selezione.
     * @param sortOrder     Il modo nel quale verranno ordinate le righe del cursore. Se null non vi sarà
     *                      alcun ordinamento
     * @return Il Cursor oppure null
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int uriMatcherCode = URI_MATCHER.match(uri);
        Cursor cursor = null;
        String itemId = null;
        StringBuilder whereClause = null;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (uriMatcherCode) {
            case OTHER_USERS_ITEM_INDICATOR:
                itemId = uri.getPathSegments().get(1);
                whereClause = new StringBuilder(DataDB.Player.PLAYER_ID)
                        .append(" = ").append(itemId);
                if (selection != null) {
                    whereClause.append(" AND ( ").append(selection).append(" ) ");
                }
                cursor = db.query(DataDB.Player.TABLE_NAME, null, whereClause.toString(),
                        selectionArgs, null, null, null);
                break;
            case OTHER_USERS_DIR_INDICATOR:
                cursor = db.query(DataDB.Player.TABLE_NAME, null, selection, selectionArgs,
                        null, null, null);
                break;
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(),
                    DataDB.Player.CONTENT_URI);
        }
        return cursor;
    }

    /**
     * Consente di aggiornare i valori contenuti nel parametro values
     *
     * @param uri           L'Uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return Il numero di record aggiornati
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        String itemId;
        StringBuilder whereClause = null;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int updateNumber = 0;
        switch (URI_MATCHER.match(uri)) {
            case OTHER_USERS_DIR_INDICATOR:
                itemId = uri.getPathSegments().get(1);
                whereClause = new StringBuilder(DataDB.Player.PLAYER_ID).append(" = ")
                        .append(itemId);
                if (selection != null) {
                    whereClause.append("AND ( ").append(selection).append(" ) ");
                }
                updateNumber = db.update(DataDB.Player.TABLE_NAME, values,
                        whereClause.toString(), selectionArgs);
                break;
            case OTHER_USERS_ITEM_INDICATOR:
                updateNumber = db.update(DataDB.Player.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
        }
        if (updateNumber > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateNumber;
    }

    /**
     * Consente di inserire un dato, dunque una riga, all'interno della tabella
     *
     * @param uri    L'Uri
     * @param values Una coppia di come_colonna/valore da aggiungere all'interno del DB
     * @return L'Uri aggiornato con il nuovo valore
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri newTeamUri = null;
        //Uri da utilizzare deve essere relativo ad un insieme di risorse.
        if (URI_MATCHER.match(uri) == OTHER_USERS_DIR_INDICATOR) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            long newItemId = db.insert(DataDB.Player.TABLE_NAME, null, values);
            if (newItemId > 0) {
                newTeamUri = ContentUris.withAppendedId(DataDB.Player.CONTENT_URI,
                        newItemId);
                getContext().getContentResolver().notifyChange(newTeamUri, null);
            }
        } else {
            throw new IllegalArgumentException("L'Uri: " + uri + " è sconosciuto per questo " +
                    "ContentProvider");
        }
        return newTeamUri;
    }

    /**
     * Permette di elimare un le informazioni associate all'Uri passato come parametro, eventualmente
     * filtrate attraverso la selection
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return Il numero di righe eliminate
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        String itemId = null;
        StringBuilder whereClause = null;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int deleteNumber = 0;
        switch (URI_MATCHER.match(uri)) {
            case OTHER_USERS_DIR_INDICATOR:
                itemId = uri.getPathSegments().get(1);
                whereClause = new StringBuilder(DataDB.Player.PLAYER_ID).append(" = ")
                        .append(itemId);
                if (selection != null) {
                    whereClause.append("AND ( ").append(selection).append(" ) ");
                }
                deleteNumber = db.delete(DataDB.Player.TABLE_NAME, whereClause.toString(),
                        selectionArgs);
                break;
            case OTHER_USERS_ITEM_INDICATOR:
                deleteNumber = db.delete(DataDB.Player.TABLE_NAME, selection,
                        selectionArgs);
                break;
        }
        if (deleteNumber > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleteNumber;
    }
}
