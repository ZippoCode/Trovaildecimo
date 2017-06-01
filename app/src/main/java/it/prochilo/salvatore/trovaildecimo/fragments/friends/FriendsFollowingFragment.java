package it.prochilo.salvatore.trovaildecimo.fragments.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.prochilo.salvatore.trovaildecimo.Dati;
import it.prochilo.salvatore.trovaildecimo.activities.ProfiloAmicoActivity;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.datamodels.User;
import it.prochilo.salvatore.trovaildecimo.recycler_view.UserListAdapter;

public class FriendsFollowingFragment extends Fragment {

    private static final String TAG = FriendsFollowingFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_friends_following, container, false);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.amici_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(layoutManager);

        //DA MODIFICARE
        mAdapter = new UserListAdapter(Dati.user.mFriendsList);
        mAdapter.setOnUserListListener(new UserListAdapter.OnUserClickedListener() {

            @Override
            public void onUserClicked(User user, int position) {
                Log.d(TAG, "Clicked on: " + user.mName + " " + user.mSurname);
                Context context = getContext();
                ProfiloAmicoActivity.setUtente(user);
                startActivity(new Intent(context, ProfiloAmicoActivity.class));
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        return layout;
    }

}
