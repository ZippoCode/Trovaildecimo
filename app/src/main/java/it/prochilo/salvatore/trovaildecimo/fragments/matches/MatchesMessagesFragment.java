package it.prochilo.salvatore.trovaildecimo.fragments.matches;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.prochilo.salvatore.trovaildecimo.Dati;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.Message;

public class MatchesMessagesFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_matches_messages, container, false);
        RecyclerView mRecyclerView = (RecyclerView)
                layout.findViewById(R.id.fragment_matches_messages_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new MessageAdapter(Dati.exampleMessage));
        return layout;
    }

    private final class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView mSenderName;
        private TextView mMessageText;

        public MessageViewHolder(View view) {
            super(view);
            mSenderName = (TextView) view.findViewById(R.id.message_sender_name);
            mMessageText = (TextView) view.findViewById(R.id.message_text);
        }

        public void bind(Message message) {
            mSenderName.setText(message.mUser.mName + " " + message.mUser.mSurname);
            mMessageText.setText(message.mText);
        }
    }


    private final class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

        private List<Message> mModel;

        public MessageAdapter(final List<Message> model) {
            this.mModel = model;
        }

        @Override
        public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_layout, parent, false);
            return new MessageViewHolder(layout);

        }

        @Override
        public void onBindViewHolder(MessageViewHolder holder, int position) {
            holder.bind(mModel.get(position));
        }

        @Override
        public int getItemCount() {
            return mModel.size();
        }
    }
}
