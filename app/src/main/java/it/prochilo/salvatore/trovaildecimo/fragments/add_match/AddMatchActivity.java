package it.prochilo.salvatore.trovaildecimo.fragments.add_match;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import it.prochilo.salvatore.trovaildecimo.R;

public class AddMatchActivity extends AppCompatActivity {

    private static final String TAG = AddMatchActivity.class.getCanonicalName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.nuova_partita_toolbar);
        mToolbar.setTitle("Crea una nuova partita");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_add_match_anchor, new AddMatchDetailsFragment())
                .commit();
    }


}