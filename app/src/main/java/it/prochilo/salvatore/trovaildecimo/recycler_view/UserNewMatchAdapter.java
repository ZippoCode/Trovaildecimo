package it.prochilo.salvatore.trovaildecimo.recycler_view;

import android.view.View;

import java.util.List;

import it.prochilo.salvatore.datamodels.User;

public class UserNewMatchAdapter extends UserListAdapter {

    public UserNewMatchAdapter(final List<User> model) {
        super(model);
    }

    //Ridefinisce il metodo di per permettere la visualizzazione dell'icona
    @Override
    public void onBindViewHolder(UserListViewHolder holder, int position) {
        holder.bind(mModel.get(position), View.VISIBLE);
        holder.setOnItemClickListener(this);
    }

    @Override
    public void onItemClicked(int position) {
        super.onItemClicked(position);
        //Quando clicco su un elemento lo posso eliminare
        mModel.remove(position);
        notifyItemRemoved(position);
    }
}
