package it.prochilo.salvatore.trovaildecimo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import it.prochilo.salvatore.trovaildecimo.fragments.AmiciFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.AreaMessaggiFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.ImpostazioniFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.PartiteFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.ProfiloFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.ContattamiFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.InformazioniFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private PartiteFragment partiteFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            partiteFragment = new PartiteFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.anchor_point, partiteFragment, PartiteFragment.TAG)
                    .commit();
        }
        mToolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int itemId = item.getItemId();
        final Fragment nextFragment;
        switch (itemId) {
            case R.id.home_menu:
                nextFragment = partiteFragment;
                break;
            case R.id.profilo_menu:
                nextFragment = new ProfiloFragment();
                break;
            case R.id.amici_menu:
                nextFragment = new AmiciFragment();
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

    public void setupToolbarWithDrawerLayout(Toolbar toolbar, int idTitle){
        setVisibityToolbar(false);
        toolbar.setTitle(getString(idTitle));
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    public void setVisibityToolbar(boolean flag){
        if(flag)
            getSupportActionBar().show();
        else
            getSupportActionBar().hide();
    }

}
