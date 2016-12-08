package me.chen_wei.uidemo.viewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * author: Chen Wei time: 16/12/1 desc: 描述
 */

public class ChildPagerAdapter extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 4;
    private Context context;

    public ChildPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return ChildPageFragment.newInstance(position + 1);
    }

}
