package it.prochilo.salvatore.trovaildecimo.models;

public class Message {

    /**
     * Il mittente del Messaggio
     */
    public final User mUser;

    /**
     * Il testo di ogni messaggio
     */
    public final String mText;

    public Message(User mUser, String mText){
        this.mUser = mUser;
        this.mText = mText;
    }
}
