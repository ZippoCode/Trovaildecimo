package it.prochilo.salvatore.trovaildecimo.fragments.matches;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.models.Partita;

public class MatchesDetailsFragment extends Fragment {


    private Partita mPartita;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.
                inflate(R.layout.fragment_matches_details, container, false);
        final Toolbar mToolbar = (Toolbar)
                layout.findViewById(R.id.fragment_matches_details_toolbar);
        mToolbar.setTitle("Dettagli partita");

        //Set the ViewPager and TabLayout
        final ViewPager mViewPager = (ViewPager)
                layout.findViewById(R.id.fragment_matches_details_viewpager);
        final TabLayout mTabLayout = (TabLayout)
                layout.findViewById(R.id.fragment_matches_details_tablayout);
        final AdapterFragmentsTab mAdapter = new AdapterFragmentsTab(getChildFragmentManager());
        mAdapter.addFragment(new MatchesInfoFragment().setPartita(mPartita), "Informazioni");
        mAdapter.addFragment(new MatchesMessagesFragment(), "Messaggi");
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        return layout;
    }

    public void setPartita(Partita partita) {
        this.mPartita = partita;
    }


    private final class AdapterFragmentsTab extends FragmentPagerAdapter {

        private List<Fragment> mDetailsMatchesTab;
        private List<String> mDetailsMatchesTabListName;

        private AdapterFragmentsTab(FragmentManager fm) {
            super(fm);
            mDetailsMatchesTab = new ArrayList<>();
            mDetailsMatchesTabListName = new ArrayList<>();
        }

        private void addFragment(Fragment detailsMatchesFragment, String title) {
            mDetailsMatchesTab.add(detailsMatchesFragment);
            mDetailsMatchesTabListName.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mDetailsMatchesTabListName.get(position);
        }

        @Override
        public int getCount() {
            return mDetailsMatchesTab.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mDetailsMatchesTab.get(position);
        }
    }
}
