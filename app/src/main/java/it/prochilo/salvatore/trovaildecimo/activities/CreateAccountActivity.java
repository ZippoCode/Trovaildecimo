package it.prochilo.salvatore.trovaildecimo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import it.prochilo.salvatore.trovaildecimo.Dati;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.datamodels.User;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        final String id = getIntent().getStringExtra("ID");
        final String email = getIntent().getStringExtra("EMAIL");
        final EditText name = (EditText) findViewById(R.id.activity_create_account_name_edittext);
        final EditText surname = (EditText) findViewById(R.id.activity_create_account_surname_edittext);
        final EditText age = (EditText) findViewById(R.id.activity_create_account_age_edittext);
        final EditText city = (EditText) findViewById(R.id.activity_create_account_city_edittext);
        final EditText role = (EditText) findViewById(R.id.activity_create_account_role_edittext);
        final Button confermButton = (Button) findViewById(R.id.activity_create_account_conferm_button);
        confermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = name.getText().toString();
                String surnameText = surname.getText().toString();
                int ageText = Integer.valueOf(age.getText().toString());
                String cityText = city.getText().toString();
                String roleText = role.getText().toString();
                Dati.user = User.Builder.create(id, email, nameText, surnameText)
                        .build();
                FirebaseDatabase.getInstance().getReference("users/" + id + "/")
                        .setValue(Dati.user);
                Intent createAccountIntent = new Intent(CreateAccountActivity.this, MainActivity.class);
                createAccountIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(createAccountIntent);
            }
        });
    }
}
