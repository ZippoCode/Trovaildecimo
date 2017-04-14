package it.prochilo.salvatore.trovaildecimo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.prochilo.salvatore.trovaildecimo.MainActivity;
import it.prochilo.salvatore.trovaildecimo.R;

public class ProfiloFragment extends Fragment {

    private static final String TAG = ProfiloFragment.class.getSimpleName();

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    //private ActionBar mActionBar;
    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_profilo, container, false);
        //mActionBar = ((MainActivity) getActivity()).getSupportActionBar();
        //mActionBar.hide();
        //mToolbar = (Toolbar) layout.findViewById(R.id.profilo_toolbar);
        //mToolbar.setTitle(R.string.profilo_menu_text);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.profilo_menu_text);
        mViewPager = (ViewPager) layout.findViewById(R.id.profilo_view_pager);
        mTabLayout = (TabLayout) layout.findViewById(R.id.profilo_tab_layout);
        setupViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mActionBar.show();
    }

    private void setupViewPager() {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new ProfiloDetailsFragment(), "Dettagli");
        adapter.addFragment(new ProfiloScoresFragment(), "Punteggio");
        mViewPager.setAdapter(adapter);
    }


    private static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}
