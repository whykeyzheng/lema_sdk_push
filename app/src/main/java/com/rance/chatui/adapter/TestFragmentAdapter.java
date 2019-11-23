package com.rance.chatui.adapter;


import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rance.chatui.ui.fragment.AddFriendFragment;
import com.rance.chatui.ui.fragment.ChatFragment;
import com.rance.chatui.ui.fragment.FriendManagementFragment;
import com.rance.chatui.ui.fragment.HomeFragment;
import com.rance.chatui.ui.fragment.TestFragment;

/**
 * Created by wuyexiong on 4/25/15.
 */
public class TestFragmentAdapter extends FragmentPagerAdapter {

    protected static final String[] CONTENT = new String[]{"This", "Is", "A", "Test",};
    private int mCount = CONTENT.length;

    public TestFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("whykey", "===" + CONTENT[position % CONTENT.length]);
        if (position == 1) {
            return FriendManagementFragment.Companion.newInstance();
        } else if (position == 0) {
            return ChatFragment.Companion.newInstance();
        } else if (position == 2) {
            return AddFriendFragment.newInstance();
        } else if (position == 3) {
            return HomeFragment.Companion.newInstance();
        } else {
            return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
        }

    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TestFragmentAdapter.CONTENT[position % CONTENT.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}
