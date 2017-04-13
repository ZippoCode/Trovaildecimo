package it.prochilo.salvatore.trovaildecimo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import it.prochilo.salvatore.trovaildecimo.fragments.MainFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.ProfiloFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.SecondoFragment;
import it.prochilo.salvatore.trovaildecimo.fragments.TerzoFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            final MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.anchor_point, mainFragment, MainFragment.TAG)
                    .commit();
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                showFragment(item);
                Log.d(TAG, "Selected: " + item);
                mDrawerLayout.closeDrawer(mNavigationView);
                return false;
            }
        });
    }

    private void showFragment(final MenuItem menuItem) {
        final int itemId = menuItem.getItemId();
        final Fragment nextFragment;
        switch (itemId) {
            case R.id.profilo_menu:
                nextFragment = new ProfiloFragment();
                break;
            case R.id.contattami_menu:
                nextFragment = new SecondoFragment();
                break;
            case R.id.informazioni_menu:
                nextFragment = new TerzoFragment();
                break;
            default:
                throw new IllegalArgumentException();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.anchor_point, nextFragment)
                .commit();
    }
}
