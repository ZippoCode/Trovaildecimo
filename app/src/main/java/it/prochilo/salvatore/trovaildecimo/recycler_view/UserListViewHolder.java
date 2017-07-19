package it.prochilo.salvatore.trovaildecimo.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.datamodels.User;

public final class UserListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView nome_cognome;

    private ImageButton mImageButton;

    private WeakReference<OnItemClickListener> mOnItemClickListener;

    protected interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public UserListViewHolder(View itemView) {
        super(itemView);
        nome_cognome = (TextView) itemView.findViewById(R.id.amico_name);
        mImageButton = (ImageButton) itemView.findViewById(R.id.add_friend_button);
        itemView.setOnClickListener(this);
    }

    public void bind(User user, int visibility) {
        nome_cognome.setText(user.name + " " + user.surname);
        mImageButton.setVisibility(visibility);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
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
