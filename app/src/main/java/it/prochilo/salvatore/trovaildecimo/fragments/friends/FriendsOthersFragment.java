package it.prochilo.salvatore.trovaildecimo.fragments.friends;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.prochilo.salvatore.datamodels.content.DataDB;
import it.prochilo.salvatore.datamodels.content.cursor.DataCursorFactory;
import it.prochilo.salvatore.datamodels.content.cursor.PlayerCursor;
import it.prochilo.salvatore.trovaildecimo.R;

/**
 * Questo Fragment gestisce la lista degli utenti che sono presenti all'interno dell'applicazione
 * con la possibilità di essere aggiunti nella lista degli amici dell'Utente
 *
 * @author Zippo
 * @version 2.0
 */
public class FriendsOthersFragment extends Fragment {

    /**
     * The TAG per il Log
     */
    private static final String TAG = FriendsOthersFragment.class.getSimpleName();

    private static class OtherUsersViewHolder extends RecyclerView.ViewHolder {

        private TextView nameFriends;

        private OtherUsersViewHolder(View itemView) {
            super(itemView);
            nameFriends = (TextView) itemView.findViewById(R.id.amico_name);
        }

        private void bindData(final Cursor cursor) {
            PlayerCursor playerCursor = (PlayerCursor) cursor;
            nameFriends.setText(playerCursor.getName() + " " + playerCursor.getSurname());
        }

    }

    private final static class OthersUsersAdapter
            extends RecyclerView.Adapter<OtherUsersViewHolder> {

        private Cursor mCursor;

        public Cursor swapCursor(final Cursor newCursor) {
            Cursor oldCursor = mCursor;
            mCursor = newCursor;
            notifyDataSetChanged();
            return oldCursor;
        }

        @Override
        public void onBindViewHolder(OtherUsersViewHolder holder, int position) {
            if (mCursor == null) {
                return;
            }
            mCursor.moveToPosition(position);
            holder.bindData(mCursor);
        }

        @Override
        public OtherUsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.amici_layout, parent, false);
            return new OtherUsersViewHolder(layout);
        }

        @Override
        public int getItemCount() {
            if (mCursor != null) {
                return mCursor.getCount();
            }
            return 0;
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_friends_other, container, false);
        RecyclerView mRecyclerView = (RecyclerView)
                layout.findViewById(R.id.fragment_friend_other_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final OthersUsersAdapter mAdapter = new OthersUsersAdapter();
        //Ricavo le informazioni dal Database
        CursorWrapper cursorWrapper = (CursorWrapper) getContext().getContentResolver()
                .query(DataDB.Player.CONTENT_URI, null, null, null, null);
        PlayerCursor cursor = (PlayerCursor) cursorWrapper
                .getWrappedCursor();
        Cursor oldCursor = mAdapter.swapCursor(cursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
        mRecyclerView.setAdapter(mAdapter);
        return layout;
    }

}
