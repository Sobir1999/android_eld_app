package com.iosix.eldblesample.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iosix.eldblesample.fragments.DVIRFragment;
import com.iosix.eldblesample.fragments.DocsFragment;
import com.iosix.eldblesample.fragments.GeneralFragment;
import com.iosix.eldblesample.fragments.LogFragment;

import java.util.ArrayList;

public class LGDDFragmentAdapter extends FragmentPagerAdapter {
    private String curr;
    private ArrayList<String> mParams;
    private String[] tabs = new String[]{"Log", "General", "Docs", "DVIR"};

    public LGDDFragmentAdapter(@NonNull FragmentManager fm, int behavior, String curr, ArrayList<String> params) {
        super(fm, behavior);
        this.curr = curr;
        this.mParams = params;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LogFragment log = new LogFragment();
                return log;
            case 1:
                GeneralFragment general = new GeneralFragment();
                return general;
            case 2:
//                DocsFragment doc = new DocsFragment();
                return DocsFragment.newInstance(curr);
            case 3:
                return DVIRFragment.newInstance(mParams,curr);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}
