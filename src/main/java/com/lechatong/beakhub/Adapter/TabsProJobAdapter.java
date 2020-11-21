package com.lechatong.beakhub.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.lechatong.beakhub.Fragments.AddressFragment;
import com.lechatong.beakhub.Fragments.CommentFragment;
import com.lechatong.beakhub.Fragments.JobFragment;

public class TabsProJobAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    public TabsProJobAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mNumOfTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AddressFragment();
            case 1:
                return new JobFragment();
            case 2:
                return new CommentFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
