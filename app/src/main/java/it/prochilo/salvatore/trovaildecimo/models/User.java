package it.prochilo.salvatore.trovaildecimo.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class User {


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

    private String mId;
    private String mEmail;
    public String mName;
    public String mSurname;
    public int mAge;
    public String mCity;
    public String mRole;
    public int mNumPlayedGame = 0;
    public float mFeedback;
    public List<User> mFriendsList = null;

    public User(String id, String email, String name, String surname) {
        this.mId = id;
        this.mEmail = email;
        this.mName = name;
        this.mSurname = surname;
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

    public void addFriend(User user) {
        if (mFriendsList == null) {
            mFriendsList = new LinkedList<>();
        }
        mFriendsList.add(user);
    }

    public void rimuoviAmico(User user) {
        mFriendsList.remove(user);
    }

    public static User fromJson(final JSONObject jsonObject) throws JSONException {
        final String id = jsonObject.getString(KeysUser.ID);
        final String email = jsonObject.getString(KeysUser.EMAIL);
        final String nome = jsonObject.getString(KeysUser.NAME);
        final String cognome = jsonObject.getString(KeysUser.SURNAME);
        final int age = jsonObject.getInt(KeysUser.AGE);
        final String city = jsonObject.getString(KeysUser.CITY);
        final String role = jsonObject.getString(KeysUser.ROLE);
        final float feedback = jsonObject.getLong(KeysUser.FEEDBACK);
        return new User(id, email, nome, cognome)
                .addProprietas(age, city, role)
                .addFeedBack(feedback);
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
        jsonObject.put(KeysUser.FEEDBACK, mFeedback);
        return jsonObject;
    }

}
