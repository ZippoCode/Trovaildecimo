package it.prochilo.salvatore.datamodels.util;


import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * La classe viene utilizzata per la lettura di file raw convertendo in stringhe
 *
 * @author Zippo
 * @version 1.0
 */
public final class ResourceUtils {

    /**
     * La codifica che viene utilizzata di Default
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Costruttore privato
     */
    private ResourceUtils() {
        throw new AssertionError("Impossibile istanzializzare la classe");
    }

    /**
     * Restituisce il contenuto di un file raw convertendolo in una stringa codificata usando UTF-8
     *
     * @param context Il Context di riferimento
     * @param rawId   L'ID della risorsa raw
     * @return Una oggetto di tipo String contenuto all'interno del file raw
     * @throws IOException                          In caso di un errore durante la lettura dallo Stream
     * @throws java.io.UnsupportedEncodingException In caso di una codifica errata
     */
    public static String getRawAsString(Context context, int rawId) throws IOException {
        return getRawAsString(context, DEFAULT_ENCODING, rawId);
    }

    /**
     * Restituisce il contenuto di un file raw convertendolo in una stringa utilizzando la condifica
     * che viene fornita come parametro
     *
     * @param context  Il Context di riferimento
     * @param encoding La codifica da utilizzare durante la lettura
     * @param rawId    L'ID della risorsa raw
     * @return Una stringa con il contenuto del file raw
     * @throws IOException                          In caso di un errore durante la lettura dello Stream
     * @throws java.io.UnsupportedEncodingException Se la codifica fosse errata
     */
    private static String getRawAsString(Context context, String encoding, int rawId) throws IOException {
        InputStream is = context.getResources().openRawResource(rawId);
        return IOUtils.toString(is, encoding);
    }
}
