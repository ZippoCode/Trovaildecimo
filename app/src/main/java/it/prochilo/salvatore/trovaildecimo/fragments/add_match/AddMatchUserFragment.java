package it.prochilo.salvatore.trovaildecimo.fragments.add_match;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.prochilo.salvatore.trovaildecimo.Dati;
import it.prochilo.salvatore.trovaildecimo.R;
import it.prochilo.salvatore.trovaildecimo.activities.MainActivity;
import it.prochilo.salvatore.trovaildecimo.models.Partita;
import it.prochilo.salvatore.trovaildecimo.models.User;
import it.prochilo.salvatore.trovaildecimo.recycler_view.UserListAdapter;
import it.prochilo.salvatore.trovaildecimo.recycler_view.UserNewMatchAdapter;

public class AddMatchUserFragment extends Fragment {

    private static final String TAG = AddMatchUserFragment.class.getSimpleName();

    private static Partita mPartita;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_add_match_user, container, false);
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            mPartita = arguments.getParcelable("partita");
        }
        final TabLayout mTabLayout = (TabLayout) layout
                .findViewById(R.id.fragment_add_match_user_tablayout);
        final ViewPager mViewPager = (ViewPager) layout
                .findViewById(R.id.fragment_add_match_user_viewpager);

        final TabFragmentAdapter mAdapter = new TabFragmentAdapter(getChildFragmentManager());

        //Creo i miei fragment
        UserToAddFragment mUserToAddFragment = new UserToAddFragment();
        UserAddedFragment mUserAddedFragment = new UserAddedFragment();
        //Aggiungo i fragment all'adapter
        mAdapter.addFragment(mUserToAddFragment, "Aggiungi Amici");
        mAdapter.addFragment(mUserAddedFragment, "Amici aggiunti");
        //Setto gli Adapter
        mViewPager.setAdapter(mAdapter);
        mUserToAddFragment.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        final FloatingActionButton mFloatingActionButton =
                (FloatingActionButton) layout.findViewById(R.id.fragment_add_match_fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Aggiungo la partita a Firebase e successivamente devo aggingerla anche sul
                //al database SQL
                mPartita.writeToDatabaseReference();
                //Ritorno alla schermata principale
                Context context = getContext();
                Intent backToMainActivity = new Intent(context, MainActivity.class);
                backToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(backToMainActivity);
            }
        });
        return layout;
    }

    private static final class TabFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragmentList = null;
        private List<String> mFragmentNameList = null;

        private TabFragmentAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            mFragmentList = new ArrayList<>();
            mFragmentNameList = new ArrayList<>();
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
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentNameList.get(position);
        }

        private void addFragment(Fragment fragment, String nameFragment) {
            mFragmentList.add(fragment);
            mFragmentNameList.add(nameFragment);
        }
    }


    public static final class UserAddedFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            final View layout = inflater.inflate(R.layout.fragment_add_match_user_added, container,
                    false);
            final RecyclerView mRecyclerView = (RecyclerView) layout
                    .findViewById(R.id.fragment_add_match_user_added_recyclerview);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(new UserListAdapter(mPartita.listaPartecipanti));
            return layout;
        }
    }

    public static final class UserToAddFragment extends Fragment {

        private PagerAdapter mAdapterViewPager;

        private List<User> customModel;

        public UserToAddFragment() {
            customModel = new ArrayList<>();
            for (int i = 0; i < Dati.user.mFriendsList.size(); i++) {
                customModel.add(Dati.user.mFriendsList.get(i));
            }
        }

        public void setAdapter(PagerAdapter adapter) {
            this.mAdapterViewPager = adapter;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_add_match_user_toadd, container,
                    false);
            final RecyclerView mRecyclerView = (RecyclerView) layout
                    .findViewById(R.id.fragment_add_match_user_toadd_recyclerview);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            final UserNewMatchAdapter mAdapter = new UserNewMatchAdapter(customModel);
            mAdapter.setOnUserListListener(new UserListAdapter.OnUserClickedListener() {
                @Override
                public void onUserClicked(User user, int position) {
                    // Quando viene eseguito un operazione di click su un utente della lista
                    // questo viene aggiunto all'elenco degli utenti della partita. Successivamente
                    // viene notificato l'Adapter del ViewPager poichè questo venga aggiunto
                    // all'elenco. Infine visualizzato un Toast per informare l'utente
                    // dell'aggiunta
                    Log.d(TAG, "User: " + user.mName + " aggiunto");
                    mPartita.addPartecipante(user);
                    mAdapterViewPager.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Utente aggiunto", Toast.LENGTH_SHORT)
                            .show();
                }
            });
            mRecyclerView.setAdapter(mAdapter);
            return layout;
        }
    }
}
