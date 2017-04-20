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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.prochilo.salvatore.trovaildecimo.MainActivity;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.Partita;
import it.prochilo.salvatore.trovaildecimo.models.User;

public class PartiteFragment extends Fragment {

    public static final String TAG = PartiteFragment.class.getSimpleName();

    private static MainActivity mMainActivity;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_partite, container, false);
        mMainActivity.getSupportActionBar().setTitle(R.string.app_name);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.partite_recycler_view);
        createModel();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new PartitaAdapter(mModel);
        mAdapter.setOnPartitaListener(new PartitaAdapter.OnPartitaListener() {
            @Override
            public void onPartitaClicked(Partita partita, int position) {
                Toast.makeText(getContext(), "Selected " + partita.id, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity = null;
    }

    /**
     * PartitaViewHolder
     */
    private final static class PartitaViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView luogo, orario;
        private ImageView card_image;
        private Button dettagli_partita_button;

        private WeakReference<OnItemClickListener> mOnItemClickListenerWeakReference;

        public interface OnItemClickListener {

            void onItemClicked(int position);
        }

        public PartitaViewHolder(View itemView) {
            super(itemView);
            luogo = (TextView) itemView.findViewById(R.id.card_view_luogo_text);
            orario = (TextView) itemView.findViewById(R.id.card_view_ora_text);
            card_image = (ImageView) itemView.findViewById(R.id.card_image);
            dettagli_partita_button = (Button) itemView.findViewById(R.id.dettagli_partita_button);
            itemView.setOnClickListener(this);
        }


        public void bind(final Partita partita) {
            luogo.setText(partita.luogo);
            orario.setText(partita.orario);
            card_image.setImageResource(R.drawable.image_card_view);
            dettagli_partita_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        public void setOnItemClickListenerWeakReference(final OnItemClickListener onItemClickListener) {
            this.mOnItemClickListenerWeakReference = new WeakReference<OnItemClickListener>(onItemClickListener);
        }

        @Override
        public void onClick(View view) {
            OnItemClickListener listener;
            if (mOnItemClickListenerWeakReference != null &&
                    (listener = mOnItemClickListenerWeakReference.get()) != null) {
                listener.onItemClicked(getLayoutPosition());
            }
        }
    }

    /**
     * PartitaAdapter
     */
    private final static class PartitaAdapter extends RecyclerView.Adapter<PartitaViewHolder> implements PartitaViewHolder.OnItemClickListener {

        private final List<Partita> mModel;
        private WeakReference<OnPartitaListener> mOnPartitaListenerWeakReference;

        public interface OnPartitaListener {

            void onPartitaClicked(Partita partita, int position);
        }


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
            holder.setOnItemClickListenerWeakReference(this);
        }

        @Override
        public int getItemCount() {
            return mModel.size();
        }

        @Override
        public void onItemClicked(int position) {
            OnPartitaListener listener;
            if (mOnPartitaListenerWeakReference != null &&
                    (listener = mOnPartitaListenerWeakReference.get()) != null) {
                listener.onPartitaClicked(mModel.get(position), position);
            }
        }

        public void setOnPartitaListener(final OnPartitaListener onPartitaListener) {
            this.mOnPartitaListenerWeakReference = new WeakReference<OnPartitaListener>(onPartitaListener);
        }

    }

    private void createModel() {
        mModel.clear();
        Random random = new Random();
        List<User> lista = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User("prochilo.salvatore@gmail.com", "Salvatore" + i, "Prochilo" + i);
            lista.add(user);
        }
        for (int i = 0; i < 10; i++) {
            final Partita item = new Partita("ID:" + i, "Via F. Rossi, " + random.nextInt(189), random.nextInt(24) + " : " + random.nextInt(60), 10)
                    .addPartecipante(lista.get(i));
            mModel.add(item);
        }
    }

    private void cardViewSelected() {
        Toast.makeText(getContext(), "Selected", Toast.LENGTH_SHORT)
                .show();
    }

}
