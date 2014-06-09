package com.jewelzqiu.sjtubbs.main;

import com.jewelzqiu.sjtubbs.R;
import com.jewelzqiu.sjtubbs.sections.SectionsFragment;
import com.jewelzqiu.sjtubbs.settings.SettingsFragment;
import com.jewelzqiu.sjtubbs.support.GetSectionsTask;
import com.jewelzqiu.sjtubbs.support.OnSectionsGetListener;
import com.jewelzqiu.sjtubbs.support.Section;
import com.jewelzqiu.sjtubbs.topten.TopTenFragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;

import java.util.ArrayList;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnSectionsGetListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public static String[] drawerListTitles;

    private TopTenFragment mTopTenFragment;

    private SectionsFragment mSectionsFragment;

    private SettingsFragment mSettingsFragment;

    private int mCurrentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerListTitles = getResources().getStringArray(R.array.drawer_list_title);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        new GetSectionsTask(this).execute();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mCurrentItem = position;
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        switch (position) {
            case 0:
                if (mTopTenFragment == null) {
                    mTopTenFragment = new TopTenFragment();
                }
                fragment = mTopTenFragment;
                break;
            case 1:
                if (mSectionsFragment == null) {
                    mSectionsFragment = new SectionsFragment();
                }
                fragment = mSectionsFragment;
                break;
            case 2:
                if (mSettingsFragment == null) {
                    mSettingsFragment = new SettingsFragment();
                }
                fragment = mSettingsFragment;
                break;
        }
        mTitle = drawerListTitles[position];
        setTitle(mTitle);
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mTitle = getString(R.string.app_name);
        } else {
            mTitle = drawerListTitles[mCurrentItem];
        }
        restoreActionBar();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onSectionsGet(ArrayList<Section> list) {
        BBSApplication.sectionList = list;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            restoreActionBar();
//            return true;
//        }
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


}
