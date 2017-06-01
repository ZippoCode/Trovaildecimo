package it.prochilo.salvatore.trovaildecimo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import it.prochilo.salvatore.datamodels.Partita;

public final class GestorePartite {

    private static final String SHARED_PREFERENCE_NAME = "GestorePartite";
    private static final String FAVORITE_KEY = "FAVORITE_KEY";
    private static GestorePartite sInstance;

    private final SharedPreferences mSharedPreferences;
    private boolean mDirty = true;
    private List<Partita> mFavoriteCache;

    private GestorePartite(final Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static GestorePartite get(Context context) {
        if (sInstance == null) {
            sInstance = new GestorePartite(context);
        }
        return sInstance;
    }

    public static GestorePartite get() {
        if (sInstance == null) {
            throw new IllegalArgumentException("Invoca prima get(Context)!");
        }
        return sInstance;
    }

    @NonNull
    public List<Partita> getFavoritePartite() {
        if (!mDirty && mFavoriteCache != null) {
            return mFavoriteCache;
        }
        final String favoriteAsString = mSharedPreferences.getString(FAVORITE_KEY, null);
        if (favoriteAsString != null) {
            try {
                JSONArray jsonArray = new JSONArray(favoriteAsString);
                List<Partita> favoriteTemp = new LinkedList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    final JSONObject item = jsonArray.getJSONObject(i);
                    final Partita partita = Partita.fromJson(item);
                    favoriteTemp.add(partita);
                }
                mFavoriteCache = favoriteTemp;
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else {
            mFavoriteCache = new LinkedList<>();
        }
        mDirty = false;
        return mFavoriteCache;
    }

    public boolean addPartita(@NonNull final Partita partita) {
        List<Partita> currentFavorite = getFavoritePartite();
        if (currentFavorite == null) {
            currentFavorite = new LinkedList<>();
        }
        int duplicateIndex = -1;
        for (int i = 0; i < currentFavorite.size(); i++) {
            final Partita item = currentFavorite.get(i);
            if (item.mId.equals(partita.mId)) {
                duplicateIndex = i;
                break;
            }
        }
        if (duplicateIndex >= 0) {
            mFavoriteCache.set(duplicateIndex, partita);
            save();
            mDirty = false;
        } else {
            currentFavorite.add(partita);
            mDirty = save();
        }
        return mDirty;
    }

    public boolean forceFavourite(final List<Partita> newFavorite) {
        mFavoriteCache = newFavorite;
        return save();
    }

    public boolean clear() {
        mFavoriteCache = new LinkedList<>();
        return save();
    }

    private boolean save() {
        if (mFavoriteCache != null) {
            try {
                final JSONArray array = new JSONArray();
                for (Partita partita : mFavoriteCache) {
                    JSONObject item = partita.toJson();
                    array.put(item);
                }
                final String arrayAsString = array.toString();
                return mSharedPreferences.edit().putString(FAVORITE_KEY, arrayAsString).commit();
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
