package it.prochilo.salvatore.trovaildecimo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

import java.util.List;

import it.prochilo.salvatore.trovaildecimo.MainActivity;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.User;

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
        setSearchView();

        DrawerLayout drawerLayout = (DrawerLayout) mMainActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(),
                drawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Otteniamo le referenze della SearchView
        final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.amici_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        AmicoAdapter adapter = new AmicoAdapter(user.amiciList);
        recyclerView.setAdapter(adapter);
        return layout;
    }

    private final static class AmicoViewHolder extends RecyclerView.ViewHolder {

        private TextView nome_cognome;

        private AmicoViewHolder(View itemView) {
            super(itemView);
            nome_cognome = (TextView) itemView.findViewById(R.id.amico_name);
        }

        private void bind(User user) {
            nome_cognome.setText(user.name + " " + user.surname);
        }
    }


    private final static class AmicoAdapter extends RecyclerView.Adapter<AmicoViewHolder> {
        private final List<User> mModel;

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
        }

        @Override
        public int getItemCount() {
            return mModel.size();
        }
    }


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
