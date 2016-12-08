package me.chen_wei.uidemo.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.chen_wei.uidemo.R;

/**
 * author: Chen Wei time: 16/12/1 desc: 描述
 */

public class ViewPagerFragment extends Fragment {

    ViewPager mChildPager;

    public static ViewPagerFragment newInstance() {
        Bundle args = new Bundle();
        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        mChildPager = (ViewPager) view.findViewById(R.id.viewpager_child);
        mChildPager.setAdapter(new ChildPagerAdapter(getChildFragmentManager(), getContext()));
        return view;
    }
}
