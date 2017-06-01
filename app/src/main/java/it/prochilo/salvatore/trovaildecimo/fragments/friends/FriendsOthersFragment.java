package it.prochilo.salvatore.trovaildecimo.fragments.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import it.prochilo.salvatore.trovaildecimo.Dati;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.activities.ProfiloAmicoActivity;
import it.prochilo.salvatore.trovaildecimo.models.User;

public class FriendsOthersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_friends_other, container, false);
        RecyclerView mRecyclerView = (RecyclerView)
                layout.findViewById(R.id.fragment_friend_other_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final FriendsOthersAdapter mAdapter = new FriendsOthersAdapter(Dati.exampleAmici);
        mAdapter.setOnFriendsOthersClicked(new FriendsOthersAdapter.OnFriendsOthersClickedListener() {
            @Override
            public void onFriendsOthersClicked(User user, int position) {
                Context context = getContext();
                ProfiloAmicoActivity.setUtente(user);
                startActivity(new Intent(context, ProfiloAmicoActivity.class));
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        return layout;
    }

    private static class FriendsOthersViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView nameFriends;
        private ImageButton mImageButton;
        private Snackbar mSnackbar;

        private WeakReference<OnItemClickListener> mOnItemClickListener;

        private interface OnItemClickListener {
            void onItemClicked(int position);
        }

        private FriendsOthersViewHolder(View itemView, Snackbar snackbar) {
            super(itemView);
            nameFriends = (TextView) itemView.findViewById(R.id.amico_name);
            mImageButton = (ImageButton) itemView.findViewById(R.id.add_friend_button);
            mSnackbar = snackbar;
            itemView.setOnClickListener(this);
        }

        public void bind(final User user, final FriendsOthersAdapter adapter) {
            nameFriends.setText(user.mName + " " + user.mSurname);
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Da modificare, per il momento funziona ma è fatto veramente male
                    Dati.user.addFriend(user);
                    Dati.exampleAmici.remove(user);
                    adapter.notifyDataSetChanged();
                    FriendsMainFragment.mViewPager.getAdapter().notifyDataSetChanged();

                    //SNACKBAR per permettere all'utente di annullare l'operazione
                    mSnackbar.setAction(R.string.delete_added, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dati.exampleAmici.add(user);
                            Dati.user.rimuoviAmico(user);
                            adapter.notifyDataSetChanged();
                            FriendsMainFragment.mViewPager.getAdapter().notifyDataSetChanged();
                        }
                    }).show();
                }
            });
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = new
                    WeakReference<>(onItemClickListener);
        }

        @Override
        public void onClick(View v) {
            OnItemClickListener listener;
            if (mOnItemClickListener != null &&
                    (listener = mOnItemClickListener.get()) != null)
                listener.onItemClicked(getLayoutPosition());
        }
    }

    private final static class FriendsOthersAdapter extends RecyclerView.Adapter<FriendsOthersViewHolder>
            implements FriendsOthersViewHolder.OnItemClickListener {

        private List<User> mModel;
        private WeakReference<OnFriendsOthersClickedListener> mOnFriendsOthersClicked;

        private interface OnFriendsOthersClickedListener {
            void onFriendsOthersClicked(User user, int position);
        }

        private FriendsOthersAdapter(List<User> model) {
            this.mModel = model;
        }

        public void setOnFriendsOthersClicked
                (final OnFriendsOthersClickedListener onFriendsOthersClicked) {
            mOnFriendsOthersClicked =
                    new WeakReference<>(onFriendsOthersClicked);
        }

        @Override
        public void onItemClicked(int position) {
            OnFriendsOthersClickedListener listener;
            if (mOnFriendsOthersClicked != null &&
                    (listener = mOnFriendsOthersClicked.get()) != null)
                listener.onFriendsOthersClicked(mModel.get(position), position);
        }

        @Override
        public FriendsOthersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.amici_layout, parent, false);
            return new FriendsOthersViewHolder(layout,
                    Snackbar.make(parent, R.string.friend_added, Snackbar.LENGTH_LONG));
        }

        @Override
        public void onBindViewHolder(FriendsOthersViewHolder holder, int position) {
            holder.bind(mModel.get(position), this);
            holder.setOnItemClickListener(this);
        }

        @Override
        public int getItemCount() {
            return mModel.size();
        }
    }
}
