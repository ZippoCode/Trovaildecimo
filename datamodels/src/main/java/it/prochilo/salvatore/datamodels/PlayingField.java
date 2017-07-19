package it.prochilo.salvatore.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Questa classe contiene le informazioni su un campo da calcio nel quale si svolgerà l'incontro
 */
public class PlayingField implements Parcelable {

    private static final byte PRESENT = 1;

    private static final byte NOT_PRESENT = 0;


    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new PlayingField(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new PlayingField[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        if (latitude != 0.0f) {
            dest.writeByte(PRESENT);
            dest.writeFloat(latitude);
            dest.writeFloat(longitude);
        } else {
            dest.writeByte(NOT_PRESENT);
        }
    }

    private interface KeysPlayingField {
        String ID = "id";
        String NAME = "name";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
    }

    /**
     * L'identificativo
     */
    public final String id;
    /**
     * Il nome del campo
     */
    public final String name;
    /**
     * Rappresenta la latitudine per le coordinate GPS
     */
    public final float latitude;
    /**
     * Rappresenta la longitudine per le coordinate GPS
     */
    public final float longitude;

    private PlayingField(final String id, final String name, final float latitude,
                         final float longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PlayingField(Parcel parcel) {
        id = parcel.readString();
        name = parcel.readString();
        if (parcel.readByte() == PRESENT) {
            latitude = parcel.readFloat();
            longitude = parcel.readFloat();
        } else {
            latitude = 0.0f;
            longitude = 0.0f;
        }
    }

    public static class Builder {

        private String mId;
        private String mName;
        private float mLatitude;
        private float mLongitude;

        private Builder(final String id, final String name) {
            this.mId = id;
            this.mName = name;
        }

        public static Builder create(final String id, final String name) {
            return new Builder(id, name);
        }

        public Builder withLocation(final float latitude, final float longitude) {
            this.mLatitude = latitude;
            this.mLongitude = longitude;
            return this;
        }

        public PlayingField build() {
            return new PlayingField(mId, mName, mLatitude, mLongitude);
        }
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KeysPlayingField.ID, id);
        jsonObject.put(KeysPlayingField.NAME, name);
        jsonObject.put(KeysPlayingField.LATITUDE, latitude);
        jsonObject.put(KeysPlayingField.LONGITUDE, longitude);
        return jsonObject;
    }

    public static PlayingField fromJson(JSONObject jsonObject) throws JSONException {
        final String id = jsonObject.getString(KeysPlayingField.ID);
        final String name = jsonObject.getString(KeysPlayingField.NAME);
        final float latitude = jsonObject.getLong(KeysPlayingField.LATITUDE);
        final float longitude = jsonObject.getLong(KeysPlayingField.LONGITUDE);
        return PlayingField.Builder.create(id, name)
                .withLocation(latitude, longitude)
                .build();
    }
}
