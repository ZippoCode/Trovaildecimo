package it.prochilo.salvatore.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {

    private interface KeysUser {
        String ID = "id";
        String EMAIL = "eMail";
        String NAME = "name";
        String SURNAME = "surname";
        String AGE = "age";
        String CITY = "city";
        String ROLE = "role";
        String NUMPLAYEDGAME = "numPlayedGame";
        String FEEDBACK = "feedback";
    }

    /**
     * L'identificativo dell'utente
     */
    private String mId;
    /**
     * L'email con il quale l'utente si è registrato
     */
    private String mEmail;
    /**
     * Il nome e il cognome dell'utente
     */
    public String mName;
    public String mSurname;
    /**
     * L'età dell'utente
     */
    public int mAge;
    /**
     * La città dell'utente
     */
    public String mCity;
    /**
     * Il ruolo dell'utente. In questo momento è di tipo String ma deve essere modificato
     * in un valore predefinito tramite un singleton
     */
    public String mRole;
    /**
     * Il numero di incontri che l'utente ha disputato
     */
    public int mNumPlayedGame = 0;
    /**
     * Il feedback dell'utente
     */
    public float mFeedback;
    /**
     * La lista degli amici
     */
    public List<User> mFriendsList = new ArrayList<>();

    private static final byte PRESENT = 1;
    private static final byte NOT_PRESENT = 0;

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //Necessario per FirebaseDatabase
    public User() {

    }

    public User(String id, String email, String name, String surname) {
        this.mId = id;
        this.mEmail = email;
        this.mName = name;
        this.mSurname = surname;
    }

    @SuppressWarnings("unchecked")
    private User(Parcel in) {
        mId = in.readString();
        mEmail = in.readString();
        mName = in.readString();
        mSurname = in.readString();
        if (in.readByte() == PRESENT) {
            mAge = in.readInt();
            mCity = in.readString();
            mRole = in.readString();
        }
        if (in.readByte() == PRESENT)
            mFeedback = in.readFloat();
        if (in.readByte() == PRESENT)
            mNumPlayedGame = in.readInt();
        if (in.readByte() == PRESENT)
            mFriendsList = in.readArrayList(User.class.getClassLoader());

    }

    public User addProprietas(int age, String city, String role) {
        this.mAge = age;
        this.mCity = city;
        this.mRole = role;
        return this;
    }

    public User addFeedBack(float feedback) {
        this.mFeedback = feedback;
        return this;
    }

    public User addNumberPlayedGame(int numPlayedGame) {
        this.mNumPlayedGame = numPlayedGame;
        return this;
    }

    public void addFriend(User user) {
        mFriendsList.add(user);
    }

    public void rimuoviAmico(User user) {
        mFriendsList.remove(user);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mEmail);
        dest.writeString(mName);
        dest.writeString(mSurname);
        if (mCity != null) {
            dest.writeByte(PRESENT);
            dest.writeInt(mAge);
            dest.writeString(mCity);
            dest.writeString(mRole);
        } else
            dest.writeByte(NOT_PRESENT);
        if (mFeedback != 0) {
            dest.writeByte(PRESENT);
            dest.writeFloat(mFeedback);
        } else
            dest.writeByte(NOT_PRESENT);
        if (mNumPlayedGame > 0) {
            dest.writeByte(PRESENT);
            dest.writeInt(mNumPlayedGame);
        } else
            dest.writeByte(NOT_PRESENT);
        if (mFriendsList != null && mFriendsList.size() > 0) {
            dest.writeByte(PRESENT);
            dest.writeList(mFriendsList);
        } else
            dest.writeByte(NOT_PRESENT);
    }

    public static User fromJson(final JSONObject jsonObject) throws JSONException {
        final String id = jsonObject.getString(KeysUser.ID);
        final String email = jsonObject.getString(KeysUser.EMAIL);
        final String nome = jsonObject.getString(KeysUser.NAME);
        final String cognome = jsonObject.getString(KeysUser.SURNAME);
        final int age = jsonObject.getInt(KeysUser.AGE);
        final String city = jsonObject.getString(KeysUser.CITY);
        final String role = jsonObject.getString(KeysUser.ROLE);
        final int numPlayedGame = jsonObject.getInt(KeysUser.NUMPLAYEDGAME);
        final float feedback = jsonObject.getLong(KeysUser.FEEDBACK);
        return new User(id, email, nome, cognome)
                .addProprietas(age, city, role)
                .addFeedBack(feedback)
                .addNumberPlayedGame(numPlayedGame);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KeysUser.ID, mId);
        jsonObject.put(KeysUser.EMAIL, mEmail);
        jsonObject.put(KeysUser.NAME, mName);
        jsonObject.put(KeysUser.SURNAME, mSurname);
        jsonObject.put(KeysUser.AGE, mAge);
        jsonObject.put(KeysUser.CITY, mCity);
        jsonObject.put(KeysUser.ROLE, mRole);
        jsonObject.put(KeysUser.NUMPLAYEDGAME, mNumPlayedGame);
        jsonObject.put(KeysUser.FEEDBACK, mFeedback);
        return jsonObject;
    }
}
