package it.prochilo.salvatore.datamodels.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Questa classe è utilizzata per la gestione di flussi
 *
 * @author Zippo
 * @version 1.0
 * @see InputStream
 * @see StringWriter
 */
final class IOUtils {

    private static final int DEFAULT_BUFFER_SIZE = 2048;

    /**
     * Il costuttore privato
     */
    private IOUtils() {
        throw new AssertionError("Impossibile istanzializzare la classe");
    }

    /**
     * Legge il contenuto proveniente da un InputStream come una oggetto di tipo String.
     * Osservazione: L'InputStream non verrà chiuso al termine dell'operazione
     *
     * @param in       L'InputStream dal quale leggere il contenuto
     * @param encoding La codifica utilizzata per leggere i caratteri
     * @return La String
     * @throws IOException Se si verifica un errore di I/O
     */
    public static String toString(InputStream in, String encoding) throws IOException {
        InputStreamReader reader = new InputStreamReader(in, encoding);
        StringWriter writer = new StringWriter();
        copy(reader, writer, DEFAULT_BUFFER_SIZE);
        return writer.getBuffer().toString();
    }

    /**
     * Copia i dati proveniente da un Reader all'interno di un Writer utilizzando la dimensione
     * custom del buffer.
     * Osservazione: I flussi del Reader e del Writer non verranno chiusi al termine dell'operazione
     *
     * @param in         The Reader
     * @param out        The Writer
     * @param bufferSize La dimensione del buffer (estressa in byte)
     * @throws IOException Se si verifica un errore di I/O
     */
    private static void copy(final Reader in, final Writer out, final int bufferSize)
            throws IOException {
        char[] buffer = new char[bufferSize];
        int read;
        while ((read = in.read(buffer)) != -1)
            out.write(buffer, 0, read);
    }
}
