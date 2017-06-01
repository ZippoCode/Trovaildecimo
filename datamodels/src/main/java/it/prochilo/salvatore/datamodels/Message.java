package it.prochilo.salvatore.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Message implements Parcelable {

    private interface KeysMessage {
        String ID = "messageId";
        String USER = "user";
        String TEXT = "text";
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    /**
     * Identifica il messaggio
     */
    public String mId;
    /**
     * Il mittente del Messaggio
     */
    public User mUser;

    /**
     * Il testo di ogni messaggio
     */
    public String mText;

    //Necessario per Firebase
    public Message() {

    }

    public Message(final String id, final User user, final String text) {
        this.mId = id;
        this.mUser = user;
        this.mText = text;
    }

    public Message(final Parcel parcel) {
        mUser = parcel.readParcelable(User.class.getClassLoader());
        mText = parcel.readString();
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KeysMessage.ID, mId);
        jsonObject.put(KeysMessage.USER, mUser.toJson());
        jsonObject.put(KeysMessage.TEXT, mText);
        return jsonObject;
    }

    public static Message fromJson(final JSONObject jsonObject) throws JSONException {
        final String id = jsonObject.getString(KeysMessage.ID);
        final User user = User.fromJson(jsonObject.getJSONObject(KeysMessage.USER));
        final String text = jsonObject.getString(KeysMessage.TEXT);
        return new Message(id, user, text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mUser, flags);
        dest.writeString(mText);
    }

}
