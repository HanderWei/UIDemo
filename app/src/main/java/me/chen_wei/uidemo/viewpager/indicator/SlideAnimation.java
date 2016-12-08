package me.chen_wei.uidemo.viewpager.indicator;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;

/**
 * author: Chen Wei time: 16/12/2 desc: 描述
 */

class SlideAnimation {
    private static final String ANIMATION_X_COORDINATE = "X_COORDINATE";
    static final int ANIMATION_DURATION = 300;

    private long mAnimationDuration = ANIMATION_DURATION;
    private ValueAnimator mAnimator;
    private UpdateListener mListener;

    private int xStartCoordinate;
    private int xEndCoordinate;

    interface UpdateListener {
        void onSlideAnimationUpdated(int xCoordinate);
    }

    SlideAnimation(UpdateListener listener) {
        mListener = listener;
        mAnimator = createAnimator();
    }

    private ValueAnimator createAnimator() {
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(mAnimationDuration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                onAnimateUpdated(valueAnimator);

            }
        });
        return animator;
    }

    SlideAnimation progress(float progress) {
        if (mAnimator != null) {
            long playTime = (long) (progress * mAnimationDuration);
            mAnimator.setCurrentPlayTime(playTime);
        }
        return this;
    }

    public SlideAnimation duration(long duration) {
        mAnimationDuration = duration;
        return this;
    }

    public void start() {
        if (mAnimator != null && !mAnimator.isRunning()) {
            mAnimator.start();
        }
    }

    public void end() {
        if (mAnimator != null && mAnimator.isStarted()) {
            mAnimator.end();
        }
    }

    SlideAnimation slide() {
        return this;
    }

    public SlideAnimation with(int startValue, int endValue) {
        if (mAnimator != null && hasChanges(startValue, endValue)) {
            xStartCoordinate = startValue;
            xEndCoordinate = endValue;
            PropertyValuesHolder holder = createColorPropertyHolder();
            mAnimator.setValues(holder);
        }
        return this;
    }

    private PropertyValuesHolder createColorPropertyHolder() {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(ANIMATION_X_COORDINATE, xStartCoordinate, xEndCoordinate);
        holder.setEvaluator(new IntEvaluator());
        return holder;
    }

    private void onAnimateUpdated(ValueAnimator animator) {
        int xCoordinate = (int) animator.getAnimatedValue(ANIMATION_X_COORDINATE);

        if (mListener != null) {
            mListener.onSlideAnimationUpdated(xCoordinate);
        }
    }


    private boolean hasChanges(int startValue, int endValue) {
        if (xStartCoordinate != startValue) {
            return true;
        } else if (xEndCoordinate != endValue) {
            return true;
        }
        return false;
    }
}
