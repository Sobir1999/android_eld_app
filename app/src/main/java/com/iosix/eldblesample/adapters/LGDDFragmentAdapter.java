package com.iosix.eldblesample.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.iosix.eldblesample.fragments.DVIRFragment;
import com.iosix.eldblesample.fragments.DocsFragment;
import com.iosix.eldblesample.fragments.GeneralFragment;
import com.iosix.eldblesample.fragments.LogFragment;

import java.util.ArrayList;

public class LGDDFragmentAdapter extends FragmentStateAdapter {
    private final String[] tabs = new String[]{"Log", "General", "Sign", "DVIR"};
    private boolean isStatisfactory;

    public LGDDFragmentAdapter(@NonNull FragmentManager fm,boolean isStatisfactory, Lifecycle lifecycle) {
        super(fm,lifecycle);
        this.isStatisfactory=isStatisfactory;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return LogFragment.newInstance();
        else if (position == 1)
            return GeneralFragment.newInstance();
        else if (position == 2)
            return DocsFragment.newInstance();
        else
            return DVIRFragment.newInstance(isStatisfactory);
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
