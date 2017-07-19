package it.prochilo.salvatore.datamodels;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import it.prochilo.salvatore.datamodels.content.DataDB;

public class User extends Player implements Parcelable {


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mEmail);
    }

    private interface KeysUser {
        String EMAIL = "email";
    }

    /**
     * L'email con il quale l'utente si è registrato
     */
    public final String mEmail;


    private User(final String id, final String email, final String name, final String surname, final Date birthday,
                 final String city, final String role, final int numPlayedGame,
                 final float feedback) {
        super(id, name, surname, birthday, city, role, numPlayedGame, feedback);
        this.mEmail = email;
    }

    @SuppressWarnings("unchecked")
    private User(Parcel in) {
        super(in);
        mEmail = in.readString();
    }

    public static class Builder extends Player.Builder {

        private String mEmail;

        private Builder(final String id, final String email, final String name,
                        final String surname) {
            super(id, name, surname);
            this.mEmail = email;
        }

        public static Builder create(final String id, final String email, final String name,
                                     final String surname) {
            return new Builder(id, email, name, surname);
        }

        @Override
        public User build() {
            return new User(mId, mEmail, mName, mSurname, mBirthday, mCity, mRole, mNumPlayedGame,
                    mFeedback);

        }
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = super.toJson();
        jsonObject.put(KeysUser.EMAIL, mEmail);
        return jsonObject;
    }

    public ContentValues toContentValues() {
        final ContentValues contentValues = super.toContentValues();
        contentValues.put(DataDB.User.EMAIL, mEmail);
        return contentValues;
    }

    public static User fromJson(final JSONObject jsonObject) throws JSONException {
        Player player = Player.fromJSON(jsonObject);
        final String email = jsonObject.getString(KeysUser.EMAIL);
        return (User) User.Builder.create(player.id, email, player.name, player.surname)
                .withInfoPerson(player.birthday, player.city, player.role)
                .withGamePerson(player.numPlayedGame, player.feedback)
                .build();
    }

}
