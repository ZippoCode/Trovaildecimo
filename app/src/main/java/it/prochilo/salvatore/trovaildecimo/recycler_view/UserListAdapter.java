package it.prochilo.salvatore.trovaildecimo.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.User;


public class UserListAdapter extends RecyclerView.Adapter<UserListViewHolder>
        implements UserListViewHolder.OnItemClickListener {

    protected final List<User> mModel;

    private WeakReference<OnUserClickedListener> mOnListUserListener;

    public interface OnUserClickedListener {
        void onUserClicked(User user, int position);
    }

    public UserListAdapter(final List<User> model) {
        this.mModel = model;
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, int position) {
        holder.bind(mModel.get(position), View.INVISIBLE);
        holder.setOnItemClickListener(this);
    }

    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amici_layout, parent, false);
        return new UserListViewHolder(layout);
    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }

    public void setOnUserListListener(final OnUserClickedListener onUserClickedListener) {
        this.mOnListUserListener = new WeakReference<>(onUserClickedListener);
    }

    @Override
    public void onItemClicked(int position) {
        OnUserClickedListener listener;
        if (mOnListUserListener != null &&
                (listener = mOnListUserListener.get()) != null)
            listener.onUserClicked(mModel.get(position), position);
    }
}

