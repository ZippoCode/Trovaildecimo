package it.prochilo.salvatore.trovaildecimo.fragments.friends;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.prochilo.salvatore.trovaildecimo.activities.MainActivity;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.util.Utils;

public class FriendsMainFragment extends Fragment {

    private static final String TAG = FriendsMainFragment.class.getSimpleName();

    private Toolbar mToolbar;

    private MainActivity mMainActivity;

    public static ViewPager mViewPager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_friends_main, container, false);
        //Otteniamo le referenze della Toolbar
        mToolbar = (Toolbar) layout.findViewById(R.id.fragment_friends_toolbar);
        mToolbar.inflateMenu(R.menu.amici_toolbar_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "Search icon selected!");
                return false;
            }
        });

        //Otteniamo le referenze della SearchView
        setSearchView();
        Utils.setActionBarDrawerToggle(mMainActivity, mToolbar);

        //Set TabLayout e ViewPager
        mViewPager = (ViewPager) layout.findViewById(R.id.fragment_friend_viewpager);
        final TabLayout mTabLayout = (TabLayout) layout.findViewById(R.id.fragment_friends_tablayout);
        AdapterFragmentsTab mAdapter = new AdapterFragmentsTab(getChildFragmentManager());
        mAdapter.addFragment(new FriendsOthersFragment(), getString(R.string.fragment_friends_tab1));
        mAdapter.addFragment(new FriendsFollowingFragment(), getString(R.string.fragment_friends_tab2));
        mAdapter.addFragment(new FriendsFollowersFragment(), getString(R.string.fragment_friends_tab3));
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        return layout;
    }

    /**
     * Utilizzato per settare la Toolbar e viene utilizzata per cercare gli utenti
     */
    private void setSearchView() {
        MenuItem searchItem = mToolbar.getMenu().findItem(R.id.action_cerca);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d(TAG, "Query Text Submit! " + query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d(TAG, "Query Text Changed! " + newText);
                    return false;
                }
            });
        }
    }

    private final class AdapterFragmentsTab extends FragmentPagerAdapter {

        private List<Fragment> mFragmentTabList;
        private List<String> mFragmentTabNameList;

        private AdapterFragmentsTab(FragmentManager fm) {
            super(fm);
            mFragmentTabList = new ArrayList<>();
            mFragmentTabNameList = new ArrayList<>();
        }

        private void addFragment(Fragment fragment, String tabName) {
            mFragmentTabList.add(fragment);
            mFragmentTabNameList.add(tabName);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentTabList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTabNameList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentTabList.size();
        }

    }
}
