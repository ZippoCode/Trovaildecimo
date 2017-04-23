package it.prochilo.salvatore.trovaildecimo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import it.prochilo.salvatore.trovaildecimo.NuovaPartita;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.Partita;
import it.prochilo.salvatore.trovaildecimo.models.User;

public class PartiteFragment extends Fragment {

    public static final String TAG = PartiteFragment.class.getSimpleName();

    private static MainActivity mMainActivity;

    private RecyclerView mRecyclerView;
    private List<Partita> mModel = new ArrayList<>();
    private PartitaAdapter mAdapter;
    private FloatingActionButton mFloatingActionButton;

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
        Log.d(TAG, "onCreateView");
        final View layout = inflater.inflate(R.layout.fragment_partite, container, false);
        mMainActivity.getSupportActionBar().setTitle(R.string.app_name);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.partite_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new PartitaAdapter(mModel);
        mAdapter.setOnPartitaListener(new PartitaAdapter.OnPartitaListener() {
            @Override
            public void onPartitaClicked(Partita partita, int position) {
                Log.d(TAG, "onPartitaClicked: ");
                mMainActivity.showFragment(new PartitaDetailsFragment());
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mFloatingActionButton = (FloatingActionButton) layout.findViewById(R.id.nuova_partita);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ciaone();
            }
        });
        return layout;
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

        private interface OnItemClickListener {

            void onItemClicked(int position);
        }

        private PartitaViewHolder(View itemView) {
            super(itemView);
            luogo = (TextView) itemView.findViewById(R.id.card_view_luogo_text);
            orario = (TextView) itemView.findViewById(R.id.card_view_ora_text);
            card_image = (ImageView) itemView.findViewById(R.id.card_image);
            dettagli_partita_button = (Button) itemView.findViewById(R.id.dettagli_partita_button);
            itemView.setOnClickListener(this);
        }


        private void bind(final Partita partita) {
            luogo.setText(partita.mNomeCampo);
            orario.setText(partita.mOra.toString());
            card_image.setImageResource(R.drawable.image_card_view);
        }

        private void setOnItemClickListenerWeakReference(final OnItemClickListener onItemClickListener) {
            this.mOnItemClickListenerWeakReference =
                    new WeakReference<OnItemClickListener>(onItemClickListener);
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
    private final static class PartitaAdapter extends RecyclerView.Adapter<PartitaViewHolder>
            implements PartitaViewHolder.OnItemClickListener {

        private final List<Partita> mModel;
        private WeakReference<OnPartitaListener> mOnPartitaListenerWeakReference;

        private interface OnPartitaListener {

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

        private void setOnPartitaListener(final OnPartitaListener onPartitaListener) {
            this.mOnPartitaListenerWeakReference = new WeakReference<>(onPartitaListener);
        }

    }


    private void ciaone() {
        startActivity(new Intent(getActivity(), NuovaPartita.class));
    }
}
