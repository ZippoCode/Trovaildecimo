package it.prochilo.salvatore.trovaildecimo.fragments.matchs;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import it.prochilo.salvatore.trovaildecimo.Dati;
import it.prochilo.salvatore.trovaildecimo.GestorePartite;
import it.prochilo.salvatore.trovaildecimo.MainActivity;
import it.prochilo.salvatore.trovaildecimo.NuovaPartitaActivity;
import it.prochilo.salvatore.trovaildecimo.ProfiloAmicoActivity;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.Partita;
import it.prochilo.salvatore.trovaildecimo.models.User;
import it.prochilo.salvatore.trovaildecimo.util.Utils;

public class PartiteFragment extends Fragment {

    public static final String TAG = PartiteFragment.class.getSimpleName();

    private static MainActivity mMainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        final View layout = inflater.inflate(R.layout.fragment_partite, container, false);
        //Toolbar
        final Toolbar mToolbar = (Toolbar) layout.findViewById(R.id.fragment_partite_toolbar);
        mToolbar.setTitle("Partite");

        // DA ELIMINARE
        GestorePartite.get(getContext()).addPartita(Dati.partita);

        //Set IconNavigationDrawer
        Utils.setActionBarDrawerToggle(mMainActivity, mToolbar);
        final RecyclerView mRecyclerView = (RecyclerView) layout.findViewById(R.id.partite_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        final PartitaAdapter mAdapter = new PartitaAdapter(GestorePartite.get(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        final FloatingActionButton mFloatingActionButton = (FloatingActionButton) layout.findViewById(R.id.nuova_partita);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivity(NuovaPartitaActivity.class);
            }
        });
        return layout;
    }

    /**
     * PartitaViewHolder
     */
    private final static class PartitaViewHolder extends RecyclerView.ViewHolder {

        private PartitaDetailsFragment mPartitaDetailsFragment;
        private TextView organizzatore, luogo, orario, giorno;
        private LinearLayout card_view_header;
        private Button dettagli_partita_button;

        private PartitaViewHolder(View itemView) {
            super(itemView);
            organizzatore = (TextView) itemView.findViewById(R.id.card_view_organizzatore_text);
            luogo = (TextView) itemView.findViewById(R.id.card_view_luogo_text);
            orario = (TextView) itemView.findViewById(R.id.card_view_ora_text);
            giorno = (TextView) itemView.findViewById(R.id.card_view_giorno_text);
            card_view_header = (LinearLayout) itemView.findViewById(R.id.card_view_header);
            card_view_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    ProfiloAmicoActivity.setUtente(Dati.partita.mUser);
                    context.startActivity(new Intent(context, ProfiloAmicoActivity.class));
                }
            });
            mPartitaDetailsFragment = new PartitaDetailsFragment();
            dettagli_partita_button = (Button) itemView.findViewById(R.id.dettagli_partita_button);
            dettagli_partita_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMainActivity.showFragment(mPartitaDetailsFragment);
                }
            });
        }


        private void bind(final Partita partita) {
            organizzatore.setText(partita.mUser.name + " " + partita.mUser.surname);
            luogo.setText(partita.mNomeCampo);
            orario.setText(partita.mOrario.toString());
            giorno.setText(partita.mData.toString());
            mPartitaDetailsFragment.setPartita(partita);
        }
    }

    /**
     * PartitaAdapter
     */
    private final static class PartitaAdapter extends RecyclerView.Adapter<PartitaViewHolder> {

        private final GestorePartite mGestorePartite;
        private List<Partita> mModel;

        PartitaAdapter(final GestorePartite gestorePartite) {
            mGestorePartite = gestorePartite;
            mModel = mGestorePartite.getFavoritePartite();
        }

        @Override
        public PartitaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View layout = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view_partita, parent, false);
            return new PartitaViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(PartitaViewHolder holder, int position) {
            mModel = mGestorePartite.getFavoritePartite();
            holder.bind(mModel.get(position));
        }

        @Override
        public int getItemCount() {
            return mModel.size();
        }
    }


    private void showActivity(Class classe) {
        startActivity(new Intent(getActivity(), classe));
    }
}
