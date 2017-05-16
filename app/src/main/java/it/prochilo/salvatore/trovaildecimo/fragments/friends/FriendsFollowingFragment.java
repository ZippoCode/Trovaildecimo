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
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import it.prochilo.salvatore.trovaildecimo.Dati;
import it.prochilo.salvatore.trovaildecimo.ProfiloAmicoActivity;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.User;

public class FriendsFollowingFragment extends Fragment {

    private static final String TAG = FriendsFollowingFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_friends_following, container, false);

        final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.amici_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        //DA MODIFICARE
        AmicoAdapter adapter = new AmicoAdapter(Dati.user.amiciList);

        adapter.setOnFriendClickedListener(new AmicoAdapter.OnFriendClickedListener() {
            @Override
            public void onFriendClicked(User user, int position) {
                Log.d(TAG, "Clicked on: " + user.name + " " + user.surname);
                Context context = getContext();
                ProfiloAmicoActivity.setUtente(user);
                startActivity(new Intent(context, ProfiloAmicoActivity.class));
            }
        });

        recyclerView.setAdapter(adapter);
        return layout;
    }

    private final static class AmicoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView nome_cognome;
        private WeakReference<OnItemClickListener> mOnItemClickListener;

        private interface OnItemClickListener {
            void onItemClicked(int position);
        }

        private AmicoViewHolder(View itemView) {
            super(itemView);
            nome_cognome = (TextView) itemView.findViewById(R.id.amico_name);
            itemView.setOnClickListener(this);
        }

        private void bind(User user) {
            nome_cognome.setText(user.name + " " + user.surname);
        }

        private void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = new WeakReference<>(onItemClickListener);
        }

        @Override
        public void onClick(View v) {
            OnItemClickListener listener;
            if (mOnItemClickListener != null &&
                    (listener = mOnItemClickListener.get()) != null)
                listener.onItemClicked(getLayoutPosition());
        }
    }


    private final static class AmicoAdapter extends RecyclerView.Adapter<AmicoViewHolder>
            implements AmicoViewHolder.OnItemClickListener {

        private final List<User> mModel;

        private WeakReference<OnFriendClickedListener> mOnFriendClickedListener;

        private interface OnFriendClickedListener {
            void onFriendClicked(User user, int position);
        }

        private AmicoAdapter(final List<User> model) {
            this.mModel = model;
        }

        @Override
        public AmicoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View layout = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.amici_layout, parent, false);
            return new AmicoViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(AmicoViewHolder holder, int position) {
            holder.bind(mModel.get(position));
            holder.setOnItemClickListener(this);
        }

        @Override
        public int getItemCount() {
            return mModel.size();
        }

        private void setOnFriendClickedListener(final OnFriendClickedListener onFriendClickedListener) {
            this.mOnFriendClickedListener = new WeakReference<>(onFriendClickedListener);
        }

        @Override
        public void onItemClicked(int position) {
            OnFriendClickedListener listener;
            if (mOnFriendClickedListener != null &&
                    (listener = mOnFriendClickedListener.get()) != null)
                listener.onFriendClicked(mModel.get(position), position);
        }
    }
}
