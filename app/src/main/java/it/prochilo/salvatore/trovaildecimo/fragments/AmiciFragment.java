package it.prochilo.salvatore.trovaildecimo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import it.prochilo.salvatore.trovaildecimo.MainActivity;
import it.prochilo.salvatore.trovaildecimo.ProfiloAmicoActivity;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.User;
import it.prochilo.salvatore.trovaildecimo.util.Utils;

public class AmiciFragment extends Fragment {

    private static final String TAG = AmiciFragment.class.getSimpleName();

    private Toolbar mToolbar;
    private MainActivity mMainActivity;
    private User user;


    public AmiciFragment setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_amici, container, false);
        //Otteniamo le referenze della Toolbar
        mToolbar = (Toolbar) layout.findViewById(R.id.toolbar_fragment_amici);
        mToolbar.inflateMenu(R.menu.amici_toolbar_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "Search icon selected!");
                return false;
            }
        });

        //Otteniamo le referenze della SearchView
        setSearchView();

        Utils.setActionBarDrawerToggle(mMainActivity, mToolbar);
        final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.amici_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        AmicoAdapter adapter = new AmicoAdapter(user.amiciList);

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
            OnItemClickListener listener = null;
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

        public void setOnFriendClickedListener(final OnFriendClickedListener onFriendClickedListener) {
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

    /**
     * Utilizzato per settare la Toolbar e viene utilizzata per cercare gli utenti
     */
    private void setSearchView() {
        MenuItem searchItem = mToolbar.getMenu().findItem(R.id.action_cerca);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d(TAG, "Query Text Submit! " + query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d(TAG, "Query Text Changed! " + newText);
                    return false;
                }
            });
        }
    }
}
