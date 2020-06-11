package com.example.lyfestyletracker.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.lyfestyletracker.ExerciseLog;
import com.example.lyfestyletracker.ExerciseWorkoutPlans;
import com.example.lyfestyletracker.R;
import com.example.lyfestyletracker.SleepTab1;
import com.example.lyfestyletracker.SleepTab2;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SleepSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.sleep_tab_text_1, R.string.sleep_tab_text_2};
    private final Context mContext;

    public SleepSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new SleepTab1();
                break;
            case 1:
                fragment = new SleepTab2();
                break;
            default:
                fragment = null;
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}