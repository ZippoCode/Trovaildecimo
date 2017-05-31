package it.prochilo.salvatore.trovaildecimo.fragments.matches;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.Message;
import it.prochilo.salvatore.trovaildecimo.models.Partita;

public class MatchesMessagesFragment extends Fragment {

    private Partita mPartita;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_matches_messages, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mPartita = arguments.getParcelable(MatchesMainFragment.KEY_PARTITA_TAG);
        }

        final RecyclerView mRecyclerView = (RecyclerView)
                layout.findViewById(R.id.fragment_matches_messages_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final MessageAdapter mMessageAdapter = new MessageAdapter(mPartita.mMessageList);
        mRecyclerView.setAdapter(mMessageAdapter);

         final ImageButton mImageButton = (ImageButton)
                layout.findViewById(R.id.fragment_matches_messages_imagebutton);
        final EditText mEditText = (EditText)
                layout.findViewById(R.id.fragment_matches_messages_edittext);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Leggo il messaggio e lo elimino dal EditText, successivamente creo un Messagge
                 * e lo aggiungo alla lista dei messaggi della partita e lo scrivo sul Database.
                 * E successivamente notifico l'adapter affinché aggiorni l'elenco dei messaggi
                 */
                final String testoMessaggio = mEditText.getText().toString();
                mEditText.setText("");
                final Message mNuovoMessaggio = new Message
                        (String.valueOf(new Random().nextInt(Integer.MAX_VALUE)),
                                mPartita.mUser, testoMessaggio);
                mPartita.addMessage(mNuovoMessaggio);
                mPartita.writeToDatabaseReference();
                mMessageAdapter.notifyDataSetChanged();
            }
        });
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
