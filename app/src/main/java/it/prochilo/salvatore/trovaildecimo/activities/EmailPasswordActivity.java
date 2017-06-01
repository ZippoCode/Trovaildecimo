package it.prochilo.salvatore.trovaildecimo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.prochilo.salvatore.trovaildecimo.Dati;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.datamodels.User;

public class EmailPasswordActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = EmailPasswordActivity.class.getSimpleName();

    private TextView mStatusTextView;
    private TextView mDetailsTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;

    /**
     * Utilizzata per vedere il caricamento.
     */
    @VisibleForTesting
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailpassword);

        //Views
        mStatusTextView = (TextView) findViewById(R.id.activity_emailpassword_status);
        mDetailsTextView = (TextView) findViewById(R.id.activity_emailpassword_details);
        mEmailField = (EditText) findViewById(R.id.activity_emailpassword_email_field);
        mPasswordField = (EditText) findViewById(R.id.activity_emailpassword_password_field);

        //Buttons
        findViewById(R.id.activity_emailpassword_signin_button).setOnClickListener(this);
        findViewById(R.id.activity_emailpassword_signup_button).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_emailpassword_signup_button:
                signUp(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case R.id.activity_emailpassword_signin_button:
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            default:
                throw new IllegalArgumentException("ID: " + v.getId() + "not found");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    /**
     * Questo metodo viene invocato quando si clicca sul Button per la creazione di un account.
     *
     * @param email
     * @param password
     */
    private void signUp(String email, String password) {
        Log.d(TAG, "create account :" + email);
        //Verifico che i campi inseriti siano corretti
        if (!validateForm()) {
            return;
        }
        //Mostro una finistra di dialogo rappresentante il caricamento
        showProgressDialog();

        //I campi sono corretti, posso creare l'account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Il metodo di callback viene invocato quando l'operazione risulta essere
                        //completata.
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            //Se l'operazione andata a buon fine ricavo le credenziali dell'utente
                            //e mostro un'Activity alla quale passo l'identificativo dell'utente e
                            //la sua email. L'activity avrà anche il compito di salvarlo nel DB di Firebase
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent createAccountIntent = new Intent(EmailPasswordActivity.this,
                                    CreateAccountActivity.class);
                            createAccountIntent.putExtra("ID",user.getUid());
                            createAccountIntent.putExtra("EMAIL",user.getEmail());
                            startActivity(createAccountIntent);
                        } else {
                            Log.w(TAG, "createUserWithEmail:unsuccess ", task.getException());
                            Toast.makeText(EmailPasswordActivity.this,
                                    //Devo convertire il testo in String
                                    "Impossibile creare l'utente", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        //Nascondo la finestra di dialogo
                        hideProgressDialog();
                    }
                });
    }

    /**
     * Il metodo viene invocato quando l'utente clicca sul Bottone di effettuare il login.
     *
     * @param email
     * @param password
     */
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn :" + email);
        //Devo verificare se i parametri che mi vengono passati sono corretti
        if (!validateForm()) {
            return;
        }

        //Mostro una finistra di dialogo rappresentante il caricamento
        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Il login ha avuto successo. Ricavo le informazioni dell'utente e le
                            //dentro la classe stupida Dati
                            Log.d(TAG, "signInEmailPassword:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase.getInstance().getReference("users/"+user.getUid())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User userMatches = dataSnapshot.getValue(User.class);
                                            Dati.user = userMatches;
                                            //Se l'operazione di login è andata a buon fine posso visualizzare
                                            // la schermata delle partita
                                            Intent intent = new Intent(EmailPasswordActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            hideProgressDialog();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        } else {
                            Log.w(TAG, "signInEmailPassword:unsuccess ", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Autentificazione fallita",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText("Login non riuscito");
                        }
                        //L'operazione è conclusa, nascondo la finestra di dialogo
                        hideProgressDialog();
                    }
                });
    }

    /**
     * Verifica che i campi inseriti siano corretti
     *
     * @return true se l'operazione è andata a buon fine, altrimenti false mostrando i messaggi
     * di errore
     */
    private boolean validateForm() {
        boolean valid = true;
        //Controllo l'email. In questo caso verifico solo se il campo è vuoto ma potrei aggiungere
        //altre informazioni come la presenza delal chiocciola
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            //Convertire in una risorsa di tipo String
            mEmailField.setError("Richiesto");
            valid = false;
        } else {
            mEmailField.setError(null);
        }
        //Verifico la password. Momentaneamente verifico solamente se è presente almeno un carattere
        //ma potrei aggiungere altre impostazioni come la presenza di un almeno un numero o la lunghezza minima
        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            //Convertire in una valore di String
            mPasswordField.setError("Richiesta");
            valid = false;
        } else {
            mEmailField.setError(null);
        }
        return valid;
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }


}
