package it.prochilo.salvatore.trovaildecimo.fragments.matches;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.prochilo.salvatore.trovaildecimo.activities.ChooserActivity;
import it.prochilo.salvatore.trovaildecimo.activities.MainActivity;
import it.prochilo.salvatore.trovaildecimo.fragments.add_match.AddMatchActivity;
import it.prochilo.salvatore.trovaildecimo.activities.ProfiloAmicoActivity;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.datamodels.Partita;
import it.prochilo.salvatore.trovaildecimo.util.Utils;

public class MatchesMainFragment extends Fragment {

    public static final String TAG = MatchesMainFragment.class.getSimpleName();

    public static final String KEY_PARTITA_TAG = "PARTITA_TAG";

    private MainActivity mMainActivity;

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private DatabaseReference mDatabaseReference;

    public MatchesMainFragment() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_matches_main, container, false);
        //Toolbar
        final Toolbar mToolbar = (Toolbar) layout.findViewById(R.id.fragment_match_main_toolbar);
        mToolbar.setTitle(getString(R.string.toolbar_matches_main_fragment));
        mToolbar.inflateMenu(R.menu.main_toolbar_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            //Può essere migliorato, facendo implementare la classe e ridefinendo il metodo
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main_toolbar_menu_order:
                        //Deve essere aggiunta l'opzione per ordinare
                        break;
                    case R.id.main_toolbar_menu_logout:
                        FirebaseAuth.getInstance().signOut();
                        Log.d(TAG, "Disconnessione");
                        //Deve essere convertito in String
                        Toast.makeText(getContext(), "Disconnessione...", Toast.LENGTH_SHORT)
                                .show();
                        startActivity(new Intent(getContext(), ChooserActivity.class));
                        break;
                    default:
                        throw new IllegalArgumentException("Not fount");
                }
                return true;
            }
        });
        //Set IconNavigationDrawer
        Utils.setActionBarDrawerToggle(mMainActivity, mToolbar);
        //Setto lo SwipeRefreshLayout
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout)
                layout.findViewById(R.id.fragment_match_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Qui dovrà cercare visualizzare eventuali aggiornamenti
                // (...)

                //Ci permette di notificare al SwipeRefreshLayout che l'operazione di refresh si è
                //conclusa e quindi la freccia rotante può essere fatta sparire
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.partite_recycler_view);
        mAdapter = new MatchAdapter(this);
        final FloatingActionButton mFloatingActionButton = (FloatingActionButton)
                layout.findViewById(R.id.fragment_matches_main_fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getContext();
                startActivity(new Intent(context, AddMatchActivity.class));
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //Nasconde il FAB durente lo scrolling verso il basso e lo fa ricomparire quando viene
                //eseguito uno scrolling verso l'alto
                if (dy > 0)
                    mFloatingActionButton.hide();
                else if (dy < 0)
                    mFloatingActionButton.show();
            }
        });
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mDatabaseReference.child("matches").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnaphot : dataSnapshot.getChildren()) {
                    //Ricavo la lista delle partite. Controllo se la partita è già presente
                    Partita partita = postSnaphot.getValue(Partita.class);
                    ((MatchAdapter) mAdapter).addMatches(partita);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private final class MatchViewHolder extends RecyclerView.ViewHolder {

        private MatchesDetailsFragment mPartitaDetailsFragment;
        private TextView mOrganizzatore, mLuogo, mTime, mDay;
        private TextView mNumPlayedMatch, mNumMissingPlayedMatch;
        private LinearLayout mCardViewHeader;
        private Button mMatchDetailsButton;
        private int mColorNormalMatch, mColorSfidaMatch;

        private MatchViewHolder(final FragmentManager fragmentManager, final View itemView) {
            super(itemView);
            mOrganizzatore = (TextView) itemView.findViewById(R.id.card_view_organizzatore_text);
            mLuogo = (TextView) itemView.findViewById(R.id.card_view_luogo_text);
            mTime = (TextView) itemView.findViewById(R.id.card_view_ora_text);
            mDay = (TextView) itemView.findViewById(R.id.card_view_giorno_text);
            mCardViewHeader = (LinearLayout) itemView.findViewById(R.id.card_view_header);
            mNumPlayedMatch = (TextView) itemView.findViewById(R.id.card_view_numPartecipanti);
            mNumMissingPlayedMatch = (TextView) itemView.findViewById(R.id.card_view_numPlayedMissing);
            mMatchDetailsButton = (Button) itemView.findViewById(R.id.dettagli_partita_button);
            mMatchDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.anchor_point, mPartitaDetailsFragment)
                            .commit();
                }
            });

            mColorNormalMatch = ContextCompat.getColor(itemView.getContext(), R.color.blue_700);
            mColorSfidaMatch = ContextCompat.getColor(itemView.getContext(), R.color.red_700);
        }


        private void bind(final Partita partita) {
            mOrganizzatore.setText(partita.mUser.mName + " " + partita.mUser.mSurname);
            mLuogo.setText(partita.mNomeCampo);
            mTime.setText(partita.mOrarioIncontro.toString());
            mDay.setText(partita.mDataIncontro.toString());
            if (partita.mTipoIncontro.equals("Normale")) {
                mCardViewHeader.setBackgroundColor(mColorNormalMatch);
            } else
                mCardViewHeader.setBackgroundColor(mColorSfidaMatch);
            mNumPlayedMatch.setText("Numero partecipanti: " + partita.numPartecipanti);
            mNumMissingPlayedMatch.setText("Giocatori mancanti: " + partita.numMissingPlayer);
            //Creo il Fragment che contiene i dettagli della partita e gli passo le informazioni
            mPartitaDetailsFragment = new MatchesDetailsFragment();
            Bundle arguments = new Bundle();
            arguments.putParcelable(KEY_PARTITA_TAG, partita);
            mPartitaDetailsFragment.setArguments(arguments);
            // Setto il listener per visualizzare le informazioni sull'organizzatore quando l'utente
            // clicca sull'intestazione della CardView
            mCardViewHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    ProfiloAmicoActivity.setUtente(partita.mUser);
                    context.startActivity(new Intent(context, ProfiloAmicoActivity.class));
                }
            });
        }
    }

    private final class MatchAdapter extends RecyclerView.Adapter<MatchViewHolder> {

        private Fragment mFragment;
        private List<Partita> mModel;

        private MatchAdapter(final Fragment fragment) {
            this.mFragment = fragment;
            //Prendo le partite preferite
            //mModel = mGestorePartite.getFavoritePartite();
            mModel = new ArrayList<>();
        }

        private void addMatches(Partita match) {
            mModel.add(match);
            notifyItemInserted(mModel.size() - 1);
        }


        @Override
        public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View layout = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view_partita, parent, false);
            return new MatchViewHolder(mFragment.getFragmentManager(), layout);
        }

        @Override
        public void onBindViewHolder(MatchViewHolder holder, int position) {
            holder.bind(mModel.get(position));
        }

        @Override
        public int getItemCount() {
            return mModel.size();
        }
    }
}
