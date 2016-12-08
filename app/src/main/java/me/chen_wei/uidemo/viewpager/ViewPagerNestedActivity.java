package me.chen_wei.uidemo.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.chen_wei.uidemo.R;

/**
 * author: Chen Wei time: 16/12/1 desc: 描述
 */

public class ViewPagerNestedActivity extends AppCompatActivity {

    @BindView(R.id.nested_view_pager_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewpager_parent)
    ViewPager mViewPagerParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_pager_nested);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.ui_view_pager_nested);
        actionBar.setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        mViewPagerParent.setAdapter(new ParentPagerAdapter(getSupportFragmentManager(), ViewPagerNestedActivity.this));
        mTabs.setupWithViewPager(mViewPagerParent);
    }
}
