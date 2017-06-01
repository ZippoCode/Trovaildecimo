package it.prochilo.salvatore.trovaildecimo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.prochilo.salvatore.trovaildecimo.Dati;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.datamodels.User;

/**
 * Questa è la prima classe che viene avviata quando l'utente effettua l'accesso e permette di
 * scegliere con quale metodo l'utente vuoi registrarsi all'applicazione. In questa prima versione
 * sono settati solamente due edittext e un bottone per prova, successivamente saranno implementate
 * le varie opzioni che permettono di accedere in modo diverso
 *
 * @author Zippo
 * @version 1.0
 */
public class ChooserActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.activity_chooser_toolbar);
        mToolbar.setTitle("Trova il decimo");
        final Button emailButton = (Button) findViewById(R.id.activity_chooser_email_button);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooserActivity.this, EmailPasswordActivity.class));
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Se l'utente si è già loggato passo alla subito alla schermata contenente le partite.
        FirebaseUser currUser = mAuth.getCurrentUser();
        if (currUser != null) {
            FirebaseDatabase.getInstance().getReference("users/".concat(currUser.getUid()))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Intent intent = new Intent(ChooserActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            User user = dataSnapshot.getValue(User.class);
                            Dati.user = user;
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }


}