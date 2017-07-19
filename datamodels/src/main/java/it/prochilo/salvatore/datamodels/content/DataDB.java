package it.prochilo.salvatore.datamodels.content;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * I metadati che racchiudono le informazioni delle tabelle presenti nel Database dell'applicazione
 *
 * @author Zippo
 * @version 1.1
 * @see BaseColumns
 */

public final class DataDB {

    /**
     * Il nome del Database
     */
    public static final String DB_NAME = "DataDB";
    /**
     * La versione del Database, da aggiornare quando è necessario aggiornale le tabelle
     */
    public static final int DB_VERSION = 1;
    /**
     * Caratterizza in modo univoco il particolare ContentProvider. Può essere qualunque stringa ma
     * è bene legarla in qualche modo all'applicazione che definisc il ContentProvider associato
     */
    public static final String AUTHORITY = "it.prochilo.salvatore.trovaildecimo";

    /**
     * Questa classe racchiuede i metadati per la tabella che racchiude gli utenti che l'utente può
     * aggiungere alla lista dei suoi amici
     */
    public static final class Player implements BaseColumns {

        /**
         * Consente di specificare il tipo di risorsa memorizzata nel ContentProvider
         */
        public static final String PATH = "users";
        /**
         * Il metodo parse(String uri) crea un Uri dalla stringa fornita come parametro.
         * In particolare la costante statica SCHEMA_CONTENT contiene il valore "content" e dunque
         * la stringa passata come parametro è:
         * content://it.prochilo.salvatore.trovaildecimo/other_users
         */
        public static final Uri CONTENT_URI = Uri.parse(ContentResolver.SCHEME_CONTENT + "://"
                + AUTHORITY + "/" + PATH);
        /**
         * Il mime-type che consente di individuare un elenco di risorse. La variabile statica
         * CURSOR_DIR_BASE_TYPE assume il valore vnd.android.cursor.dir al quale aggiungiamo
         * /vnd.other_users
         */
        public static final String MIME_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd."
                + PATH;
        /**
         * Il mime-tpye che permette di individuare una singola risorsa. La variabile statica
         * CURSOR_DIR_ITEM_TYPE ha il valore costante vnd.android.cursor.item al quale aggiungiamo
         * /vnd.other_users
         */
        public static final String MIME_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd."
                + PATH;
        /**
         * Il nome della tabella che conterrà i dati
         */
        public static final String TABLE_NAME = "USERS";
        /**
         * L'ID per identificare l'Utente
         */
        public static final String PLAYER_ID = "userId";
        /**
         * Il nome dell'utente
         */
        public static final String NAME = "name";
        /**
         * Il cognome dell'utente
         */
        public static final String SURNAME = "surname";
        /**
         * L'età dell'utente. (Da modificare perché è più interessante inserire la data di nascita
         * e ricavarne l'età)
         */
        public static final String BIRTHDAY = "birthday";
        /**
         * La città dell'utente
         */
        public static final String CITY = "city";
        /**
         * Il ruolo nel quale gioca l'utente
         */
        public static final String ROLE = "role";
        /**
         * Il numero di incontri che l'utente ha disputato
         */
        public static final String NUM_PLAYED_GAME = "numPlayedGame";
        /**
         * Il Feedback che l'utente ha accumulato
         */
        public static final String FEEDBACK = "feedback";
    }

    /**
     * Contentiene i metadati dell'utente che utilizza l'applicazione
     */
    public static final class User implements BaseColumns {
        /**
         * L'email dell'utente
         */
        public static final String EMAIL = "email";

    }

    /**
     * Contiene i metadati dei campi di gioco
     */
    public static final class PlayingField implements BaseColumns {

        /**
         * Consente di specificare il tipo di risorsa contenuta nel ContentProvider
         */
        public static final String PATH = "playingFields";
        /**
         * Rappresenta la stringa:
         * content://it.prochilo.salvatore.trovaildecimo/playingFields
         */
        public static final Uri CONTENT_URI = Uri.parse(ContentResolver.SCHEME_CONTENT + "://"
                + AUTHORITY + "/" + PATH);
        /**
         * Rappresenta la stringa: vnd.android.cursor.dir/vnd.playingFields
         */
        public static final String MIME_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd."
                + PATH;
        /**
         * Rappresenta la stringa: vnd.android.cursor.item/vnd.playingFields
         */
        public static final String MIME_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd."
                + PATH;
        /**
         * Il nome dalla taballa
         */
        public static final String TABLE_NAME = "PLAYING FIELDS";
        /**
         * Il nome della colonna che identifica ID di un campo
         */
        public static final String ID = "playingFieldId";
        /**
         * Il nome della colonna che identifica il nome di un campo
         */
        public static final String NAME = "name";
        /**
         * Il nome della colonna che identifica la latitudine di un campo
         */
        public static final String LATITUDE = "latitude";
        /**
         * Il nome della colonna che racchiude la longitudine di un campo
         */
        public static final String LONGITUDE = "longitude";
    }
}
