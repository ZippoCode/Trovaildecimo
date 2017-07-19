package it.prochilo.salvatore.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Questa classe rappresenta un incontro. Contiene tutti i paramentri per gestirne le funzioni
 *
 * @author Zippo
 * @version 2.0
 */
public class Match implements Parcelable {

    private static final byte PRESENT = 1;

    private static final byte NOT_PRESENT = 0;

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(user, flags);
        dest.writeLong(matchday.getTime());
        dest.writeLong(dayOfAddition.getTime());
        dest.writeParcelable(playingField, flags);
        if (challengeType != null) {
            dest.writeByte(PRESENT);
            dest.writeInt(numPlayer);
            dest.writeString(durationMatch.name());
            dest.writeString(challengeType.name());
        } else {
            dest.writeByte(NOT_PRESENT);
        }
    }

    /**
     * Chiavi per le proprietà
     */
    interface KeysMatch {
        String ID = "id";
        String USER = "organizzatore";
        String MATCH_DAY = "matchDay";
        String ADDITION_DAY = "additionDay";
        String PLAYING_FIELD = "playingField";
        String NUM_PLAYER = "numPlayer";
        String DURATION_MATCH = "durationMatch";
        String CHALLENGE_TYPE = "challengeType";
    }

    /**
     * L'identificatore delle partite che deve essere univoco
     */
    public final String id;
    /**
     * L'organizzatore della partita
     */
    public final User user;
    /**
     * La data nella quale di svolgerà l'incontro
     */
    public final Date matchday;
    /**
     * Rappresenta l'orario nel quale è stata creata la partita
     */
    public final Date dayOfAddition;
    /**
     * Il luogo dell'incontro
     */
    public final PlayingField playingField;
    /**
     * Il numero dei partecipanti
     */
    public final int numPlayer;
    /**
     * La tipologia dell'incontro può essere o normale o sfida
     */
    public final Enumeration.ChallangeType challengeType;
    /**
     * La durata dell'incontro
     */
    public final Enumeration.DurationMatch durationMatch;

    private Match(final String id, final User user, final Date matchDay,
                  final Date dayOfAddition, final PlayingField playingField,
                  final int numPlayer,
                  final Enumeration.ChallangeType challangeType,
                  final Enumeration.DurationMatch durationMatch) {
        this.id = id;
        this.user = user;
        this.matchday = matchDay;
        this.dayOfAddition = dayOfAddition;
        this.playingField = playingField;
        this.numPlayer = numPlayer;
        this.challengeType = challangeType;
        this.durationMatch = durationMatch;
    }

    @SuppressWarnings("unchecked")
    public Match(Parcel parcel) {
        id = parcel.readString();
        user = parcel.readParcelable(User.class.getClassLoader());
        matchday = new Date(parcel.readLong());
        dayOfAddition = new Date(parcel.readLong());
        playingField = parcel.readParcelable(User.class.getClassLoader());
        if (parcel.readByte() == PRESENT) {
            numPlayer = parcel.readInt();
            durationMatch = Enumeration.DurationMatch.valueOf(parcel.readString());
            challengeType = Enumeration.ChallangeType.valueOf(parcel.readString());
        } else {
            numPlayer = 0;
            durationMatch = null;
            challengeType = null;
        }
    }

    public static class Builder {
        private String mId;
        private User mUser;
        private Date mMatchday;
        private Date mDayOfAddition;
        private PlayingField mPlayingField;
        private int mNumPlayer;
        private Enumeration.ChallangeType mChallengeType;
        private Enumeration.DurationMatch mDurationMatch;

        private Builder(final String id, final User user, final Date matchDay,
                        final Date dayOfAddition, final PlayingField playingField) {
            this.mId = id;
            this.mUser = user;
            this.mMatchday = matchDay;
            this.mDayOfAddition = dayOfAddition;
            this.mPlayingField = playingField;
        }


        public static Builder create(final String id, final User user, final Date matchDay,
                                     final Date dayOfAddition,
                                     final PlayingField playingField) {
            return new Builder(id, user, matchDay, dayOfAddition, playingField);
        }

        public Builder withInfoMatch(final int numPlayer,
                                     final Enumeration.DurationMatch durationMatch,
                                     final Enumeration.ChallangeType challangeType) {
            this.mNumPlayer = numPlayer;
            this.mDurationMatch = durationMatch;
            this.mChallengeType = challangeType;
            return this;
        }

        public Match build() {
            return new Match(mId, mUser, mMatchday, mDayOfAddition, mPlayingField, mNumPlayer,
                    mChallengeType, mDurationMatch);
        }
    }

    public JSONObject toJson() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(KeysMatch.ID, id);
        jsonObject.put(KeysMatch.USER, user.toJson());
        jsonObject.put(KeysMatch.MATCH_DAY, matchday.getTime());
        jsonObject.put(KeysMatch.ADDITION_DAY, dayOfAddition.getTime());
        jsonObject.put(KeysMatch.PLAYING_FIELD, playingField.toJson());
        jsonObject.put(KeysMatch.NUM_PLAYER, numPlayer);
        jsonObject.put(KeysMatch.DURATION_MATCH, durationMatch.toString());
        jsonObject.put(KeysMatch.CHALLENGE_TYPE, challengeType.toString());
        return jsonObject;
    }

    public static Match fromJson(final JSONObject jsonObject) throws JSONException {
        final String id = jsonObject.getString(KeysMatch.ID);
        final User organizzatore = User.fromJson(jsonObject.getJSONObject(KeysMatch.USER));
        final Date matchDay = new Date(jsonObject.getLong(KeysMatch.MATCH_DAY));
        final Date additionDay = new Date(jsonObject.getLong(KeysMatch.ADDITION_DAY));
        final PlayingField playingField = PlayingField
                .fromJson(jsonObject.getJSONObject(KeysMatch.PLAYING_FIELD));
        final int numPlayer = jsonObject.getInt(KeysMatch.NUM_PLAYER);
        final Enumeration.DurationMatch durationMatch = Enumeration.DurationMatch
                .valueOf(jsonObject.getString(KeysMatch.DURATION_MATCH));
        final Enumeration.ChallangeType challangeType = Enumeration.ChallangeType
                .valueOf(jsonObject.getString(KeysMatch.CHALLENGE_TYPE));
        return Match.Builder.create(id, organizzatore, matchDay, additionDay, playingField)
                .withInfoMatch(numPlayer, durationMatch, challangeType)
                .build();
    }

}
