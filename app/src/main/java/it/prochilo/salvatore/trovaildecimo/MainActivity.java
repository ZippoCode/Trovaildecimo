package it.prochilo.salvatore.trovaildecimo;

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

import it.prochilo.salvatore.trovaildecimo.fragments.friends.FriendsMainFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.AreaMessaggiFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.ImpostazioniFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.matchs.PartiteFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.profilo.ProfiloFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.ContattamiFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.InformazioniFragment;
import it.prochilo.salvatore.trovaildecimo.models.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            final PartiteFragment partiteFragment = new PartiteFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.anchor_point, partiteFragment, PartiteFragment.TAG)
                    .commit();
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
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
                nextFragment = new PartiteFragment();
                break;
            case R.id.profilo_menu:
                nextFragment = new ProfiloFragment();
                break;
            case R.id.amici_menu:
                nextFragment = new FriendsMainFragment();
                break;
            case R.id.area_messaggi_menu:
                nextFragment = new AreaMessaggiFragment();
                break;
            case R.id.impostazioni_menu:
                nextFragment = new ImpostazioniFragment();
                break;
            case R.id.contattami_menu:
                nextFragment = new ContattamiFragment();
                break;
            case R.id.informazioni_menu:
                nextFragment = new InformazioniFragment();
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

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(PartiteFragment.TAG)
                .replace(R.id.anchor_point, fragment)
                .commit();
    }

}
