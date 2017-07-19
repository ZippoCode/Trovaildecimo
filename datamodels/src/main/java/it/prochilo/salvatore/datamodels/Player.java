package it.prochilo.salvatore.datamodels;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import it.prochilo.salvatore.datamodels.content.DataDB;

/**
 * Questa classe rappresenta un utente che utilizza l'applicazione. Saranno gli persone con le quali
 * l'utente potrà interagire.
 *
 * @author 1.0
 * @version 1.0
 */

public class Player implements Parcelable {

    /**
     * Costante che identifica la presenza di un attributo
     */
    protected static final byte PRESENT = 1;
    /**
     * Costante che identifica la non presenza di un attributo
     */
    protected static final byte NOT_PRESENT = 0;

    /**
     *
     */
    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Player(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(surname);
        if (birthday != null) {
            dest.writeByte(PRESENT);
            dest.writeLong(birthday.getTime());
            dest.writeString(role);
            dest.writeString(city);
        } else {
            dest.writeByte(NOT_PRESENT);
        }
        if (numPlayedGame != 0) {
            dest.writeByte(PRESENT);
            dest.writeInt(numPlayedGame);
            dest.writeFloat(feedback);
        } else {
            dest.writeByte(NOT_PRESENT);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private interface KeysPerson {
        String ID = "id";
        String NAME = "name";
        String SURNAME = "surname";
        String BIRTHDAY = "birthday";
        String CITY = "city";
        String ROLE = "role";
        String NUMPLAYEDGAME = "numPlayedGame";
        String FEEDBACK = "feedback";
    }

    /**
     * L'identificativo della persona
     */
    public final String id;
    /**
     * Il nome della persona
     */
    public final String name;
    /**
     * Il cognome della persona
     */
    public final String surname;
    /**
     * Il giorno di nascita della persona
     */
    public final Date birthday;
    /**
     * La città nella quale la persona utilizza l'applicazione
     */
    public final String city;
    /**
     * Il ruolo nel quale gioca
     */
    public final String role;
    /**
     * Il numero di incontri giocati
     */
    public final int numPlayedGame;
    /**
     * Il feedback che la persona ha accumulato
     */
    public final float feedback;

    protected Player(final String id, final String name, final String surname, final Date birthday,
                     final String city, final String role, final int numPlayedGame,
                     final float feedback) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.city = city;
        this.role = role;
        this.numPlayedGame = numPlayedGame;
        this.feedback = feedback;
    }

    /**
     * Costruttore da un Parcel
     *
     * @param parcel
     */
    @SuppressWarnings("unchecked")
    protected Player(Parcel parcel) {
        id = parcel.readString();
        name = parcel.readString();
        surname = parcel.readString();
        if (parcel.readByte() == PRESENT) {
            birthday = new Date(parcel.readLong());
            role = parcel.readString();
            city = parcel.readString();
        } else {
            birthday = null;
            role = null;
            city = null;
        }
        if (parcel.readByte() == PRESENT) {
            numPlayedGame = parcel.readInt();
            feedback = parcel.readFloat();
        } else {
            numPlayedGame = 0;
            feedback = 0.0f;
        }
    }

    public static class Builder {

        protected String mId;
        protected String mName;
        protected String mSurname;
        protected Date mBirthday;
        protected String mCity;
        protected String mRole;
        protected int mNumPlayedGame;
        protected float mFeedback;

        protected Builder(final String id, final String name, final String surname) {
            this.mId = id;
            this.mName = name;
            this.mSurname = surname;
        }

        public static Builder create(final String id, final String name, final String surname) {
            return new Builder(id, name, surname);
        }

        public Builder withInfoPerson(final Date birthday, final String city,
                                      final String role) {
            this.mBirthday = birthday;
            this.mCity = city;
            this.mRole = role;
            return this;
        }

        public Builder withGamePerson(final int numPlayedGame, final float feedback) {
            this.mNumPlayedGame = numPlayedGame;
            this.mFeedback = feedback;
            return this;
        }

        public Player build() {
            return new Player(mId, mName, mSurname, mBirthday, mCity, mRole, mNumPlayedGame,
                    mFeedback);
        }

    }

    public JSONObject toJson() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(KeysPerson.ID, id);
        jsonObject.put(KeysPerson.NAME, name);
        jsonObject.put(KeysPerson.SURNAME, surname);
        jsonObject.put(KeysPerson.BIRTHDAY, birthday);
        jsonObject.put(KeysPerson.CITY, city);
        jsonObject.put(KeysPerson.ROLE, role);
        jsonObject.put(KeysPerson.NUMPLAYEDGAME, numPlayedGame);
        jsonObject.put(KeysPerson.FEEDBACK, feedback);
        return jsonObject;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataDB.Player.PLAYER_ID, id);
        contentValues.put(DataDB.Player.NAME, name);
        contentValues.put(DataDB.Player.SURNAME, surname);
        contentValues.put(DataDB.Player.BIRTHDAY, birthday.getTime());
        contentValues.put(DataDB.Player.CITY, city);
        contentValues.put(DataDB.Player.ROLE, role);
        contentValues.put(DataDB.Player.NUM_PLAYED_GAME, numPlayedGame);
        contentValues.put(DataDB.Player.FEEDBACK, feedback);
        return contentValues;
    }

    public static Player fromJSON(JSONObject jsonObject) throws JSONException {
        final String id = jsonObject.getString(KeysPerson.ID);
        final String nome = jsonObject.getString(KeysPerson.NAME);
        final String cognome = jsonObject.getString(KeysPerson.SURNAME);
        final long birthday = jsonObject.getInt(KeysPerson.BIRTHDAY);
        final String city = jsonObject.getString(KeysPerson.CITY);
        final String role = jsonObject.getString(KeysPerson.ROLE);
        final int numPlayedGame = jsonObject.getInt(KeysPerson.NUMPLAYEDGAME);
        final float feedback = jsonObject.getLong(KeysPerson.FEEDBACK);
        return it.prochilo.salvatore.datamodels.Player.Builder.create(id, nome, cognome)
                .withInfoPerson(new Date(birthday), city, role)
                .withGamePerson(numPlayedGame, feedback)
                .build();
    }
}
