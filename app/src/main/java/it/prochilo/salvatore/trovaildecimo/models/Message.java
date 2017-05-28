package it.prochilo.salvatore.trovaildecimo.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Message implements Parcelable {

    private interface KeysMessage {
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
     * Il mittente del Messaggio
     */
    public final User mUser;

    /**
     * Il testo di ogni messaggio
     */
    public String mText;

    public Message(final User user, final String text) {
        this.mUser = user;
        this.mText = text;
    }

    public Message(final Parcel parcel) {
        mUser = parcel.readParcelable(User.class.getClassLoader());
        mText = parcel.readString();
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KeysMessage.USER, mUser.toJson());
        jsonObject.put(KeysMessage.TEXT, mText);
        return jsonObject;
    }

    public static Message fromJson(final JSONObject jsonObject) throws JSONException {
        final User user = User.fromJson(jsonObject.getJSONObject(KeysMessage.USER));
        final String text = jsonObject.getString(KeysMessage.TEXT);
        return new Message(user, text);
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
