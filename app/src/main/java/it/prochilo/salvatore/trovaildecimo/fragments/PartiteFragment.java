package it.prochilo.salvatore.trovaildecimo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.prochilo.salvatore.trovaildecimo.MainActivity;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.Partita;
import it.prochilo.salvatore.trovaildecimo.models.User;

public class PartiteFragment extends Fragment {

    public static final String TAG = PartiteFragment.class.getSimpleName();

    private MainActivity mMainActivity;

    private RecyclerView mRecyclerView;
    private List<Partita> mModel = new ArrayList<>();
    private PartitaAdapter mAdapter;

    public PartiteFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMainActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_partite, container, false);
        mMainActivity.getSupportActionBar().setTitle(R.string.app_name);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.partite_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        createModel();
        mAdapter = new PartitaAdapter(mModel);
        mRecyclerView.setAdapter(mAdapter);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity = null;
    }

    private final static class PartitaViewHolder extends RecyclerView.ViewHolder {

        private TextView cart_title;

        public PartitaViewHolder(View itemView) {
            super(itemView);
            cart_title = (TextView) itemView.findViewById(R.id.card_title);
        }

        public void bind(Partita partita) {
            cart_title.setText(partita.orario + " - " + partita.luogo);
        }
    }

    private final static class PartitaAdapter extends RecyclerView.Adapter<PartitaViewHolder> {

        private final List<Partita> mModel;

        PartitaAdapter(final List<Partita> model) {
            mModel = model;
        }

        @Override
        public PartitaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View layout = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.partita_layout, parent, false);
            return new PartitaViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(PartitaViewHolder holder, int position) {
            holder.bind(mModel.get(position));
        }

        @Override
        public int getItemCount() {
            return mModel.size();
        }
    }

    private void createModel() {
        mModel.clear();
        List<User> lista = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User("prochilo.salvatore@gmail.com", "Salvatore" + i, "Prochilo" + i);
            lista.add(user);
        }
        for (int i = 0; i < 10; i++) {
            final Partita item = new Partita("ID:" + i, i + " : " + i, "Via F. Rossi, " + i, 10)
                    .addPartecipante(lista.get(i));
            mModel.add(item);
        }
    }

}
