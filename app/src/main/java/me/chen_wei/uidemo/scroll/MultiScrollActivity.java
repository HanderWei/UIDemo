package me.chen_wei.uidemo.scroll;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import me.chen_wei.uidemo.R;

/**
 * author: Chen Wei time: 16/11/22 desc: 描述
 */

public class MultiScrollActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout mHoverLayout;
    private FrameLayout mScrollLayout;
    private Button mHoverPlayBtn;
    private Button mScrollPlayBtn;
    private NestedScrollView mScrollView;

    private static final String TAG = "MultiScrollActivity";

    private boolean isPlay = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_scroll);

        Toolbar toolbar = (Toolbar) findViewById(R.id.multi_scroll_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.ui_slide_label);
        actionBar.setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        mHoverLayout = (FrameLayout) findViewById(R.id.fl_hover);
        mScrollLayout = (FrameLayout) findViewById(R.id.fl_scroll);
        mHoverPlayBtn = (Button) findViewById(R.id.btn_hover_play);
        mHoverPlayBtn.setText("播放");
        mHoverPlayBtn.setOnClickListener(this);
        mScrollPlayBtn = (Button) findViewById(R.id.btn_scroll_play);
        mScrollPlayBtn.setOnClickListener(this);
        mScrollPlayBtn.setText("播放");

        mScrollView = (NestedScrollView) findViewById(R.id.scroll_view);

        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0 && !isPlay) {
                    mHoverLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_hover_play:
                isPlay = !isPlay;
                onHoverPlayStateChanged(isPlay);
                break;
            case R.id.btn_scroll_play:
                isPlay = !isPlay;
                onPlayStateChanged(isPlay);
                break;
        }
    }

    private void onHoverPlayStateChanged(boolean isPlay) {
        setText(isPlay);
        if (mScrollView.getScrollY() == 0 && !isPlay) {
            mHoverLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void onPlayStateChanged(boolean isPlay) {
        setText(isPlay);
        if (isPlay) {
            scrollToTop();
        }
    }

    private void setText(boolean isPlay) {
        if (isPlay) {
            mScrollPlayBtn.setText("暂停");
            mHoverPlayBtn.setText("暂停");
        } else {
            mScrollPlayBtn.setText("播放");
            mHoverPlayBtn.setText("播放");
        }
    }

    public void scrollToTop() {
        int x = 0;
        int y = 0;
        ObjectAnimator xTranslate = ObjectAnimator.ofInt(mScrollView, "scrollX", x);
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(mScrollView, "scrollY", y);

        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(200);
        animators.playTogether(xTranslate, yTranslate);
        animators.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                // TODO Auto-generated method stub
                if (isPlay) {
                    mHoverLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
        animators.start();
    }

}
