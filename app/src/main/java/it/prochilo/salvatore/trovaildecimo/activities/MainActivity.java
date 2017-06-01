package it.prochilo.salvatore.trovaildecimo.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.fragments.friends.FriendsMainFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.SearchSoccerFieldFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.SettingsFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.matches.MatchesMainFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.profilo.ProfiloFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            final MatchesMainFragment partiteFragment = new MatchesMainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.anchor_point, partiteFragment, MatchesMainFragment.TAG)
                    .commit();
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            final NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
            mNavigationView.setNavigationItemSelectedListener(this);
        }
    }

    /**
     * Usato dal NavigationDrawer per selezionare i vari Fragment dell'applicazione
     *
     * @param item Identifica l'istenza di MenuItem selezionata dall'utente
     * @return True
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "Selected: " + item);
        final int itemId = item.getItemId();
        final Fragment nextFragment;
        switch (itemId) {
            case R.id.home_menu:
                nextFragment = new MatchesMainFragment();
                break;
            case R.id.profilo_menu:
                nextFragment = new ProfiloFragment();
                break;
            case R.id.amici_menu:
                nextFragment = new FriendsMainFragment();
                break;
            case R.id.cerca_campo_menu:
                nextFragment = new SearchSoccerFieldFragment();
                break;
            case R.id.impostazioni_menu:
                nextFragment = new SettingsFragment();
                break;
            default:
                throw new IllegalArgumentException();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.anchor_point, nextFragment)
                .commit();
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
