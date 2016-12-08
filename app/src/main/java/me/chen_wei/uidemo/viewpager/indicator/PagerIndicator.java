package me.chen_wei.uidemo.viewpager.indicator;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import me.chen_wei.uidemo.R;
import me.chen_wei.uidemo.utils.DensityUtils;

/**
 * author: Chen Wei time: 16/12/3 desc: 描述
 */

public class PagerIndicator extends View implements ViewPager.OnPageChangeListener {

    private static final int COUNT_NOT_SET = -1;   //未设置圆点数

    private static final int DEFAULT_RADIUS_DP = 6;
    private static final int DEFAULT_PADDING_DP = 8;

    private int mRadiusPx; //圆点半径
    private int mPaddingPx; //圆点之间的距离

    private int mCirclesCount; //圆点数量
    private boolean isCountSet; //是否设置的圆点的数量

    private int mUnselectedColor;   //未选中的圆点的颜色
    private int mSelectedColor; //选中的圆点的颜色

    private int mXCoordinate; //滑动圆点的左端偏移量

    private int selectedPosition;  //选中的Pager
    private int selectingPosition; //滑向的Pager
    private int lastSelectedPosition;  //上一次选中的Pager

    private long mAnimationDuration;

    private DataSetObserver mDataSetObserver;   //ViewPager数据观察者

    private Paint mFillPaint = new Paint();
    private RectF mRect = new RectF();

    private SlideAnimation mSlideAnimation;

    private ViewPager mViewPager;
    private int mViewPagerId;

    public PagerIndicator(Context context) {
        super(context);
        init(null);
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PagerIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        findViewPager();
    }

    @Override
    protected void onDetachedFromWindow() {
        unregisterSetObserver();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int circleDiameterPx = mRadiusPx * 2;

        int desireWidth = 0;
        if (mCirclesCount != 0) {
            int diameterSum = circleDiameterPx * mCirclesCount;
            int paddingSum = mPaddingPx * (mCirclesCount - 1);
            desireWidth = diameterSum + paddingSum;
        }

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desireWidth, widthSize);
        } else {
            width = desireWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(circleDiameterPx, heightSize);
        } else {
            height = circleDiameterPx;
        }

        if (width < 0) {
            width = 0;
        }
        if (height < 0) {
            height = 0;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mXCoordinate = getXCoordinate(selectedPosition);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawIndicatorView(canvas);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        onPageScroll(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void onPageScroll(int position, float positionOffset) {
        Pair<Integer, Float> progressPair = getProgress(position, positionOffset);
        int selectingPosition = progressPair.first;
        float selectingProgress = progressPair.second;

        if (selectingProgress == 1) {
            lastSelectedPosition = selectedPosition;
            selectedPosition = selectingPosition;
        }

        setProgress(selectingPosition, selectingProgress);
    }

    private Pair<Integer, Float> getProgress(int position, float positionOffset) {
        boolean isRightOverScrolled = position > selectedPosition;
        boolean isLeftOverScrolled = position + 1 < selectedPosition;

        if (isRightOverScrolled || isLeftOverScrolled) {
            selectedPosition = position;
        }

        boolean isSlideToRightSide = selectedPosition == position && positionOffset != 0;
        int selectingPosition;
        float selectingProgress;

        if (isSlideToRightSide) {
            selectingPosition = position + 1;
            selectingProgress = positionOffset;
        } else {
            selectingPosition = position;
            selectingProgress = 1 - positionOffset;
        }

        if (selectingProgress > 1) {
            selectingProgress = 1;
        } else if (selectingProgress < 0) {
            selectingProgress = 0;
        }

        return new Pair<>(selectingPosition, selectingProgress);
    }

    public void setProgress(int position, float progress) {
        if (position < 0) {
            position = 0;
        } else if (position > mCirclesCount - 1) {
            position = mCirclesCount - 1;
        }

        if (progress < 0) {
            progress = 0;
        } else if (progress > 1) {
            progress = 1;
        }

        this.selectingPosition = position;

        int fromX = getXCoordinate(selectedPosition);
        int toX = getXCoordinate(selectingPosition);
        mSlideAnimation.slide().with(fromX, toX);
        mSlideAnimation.progress(progress);
    }

    private void findViewPager() {
        if (mViewPagerId == 0) {
            return;
        }

        Context context = getContext();
        if (context instanceof Activity) {
            Activity activity = (Activity) getContext();
            View view = activity.findViewById(mViewPagerId);

            if (view != null && view instanceof ViewPager) {
                setViewPager((ViewPager) view);
            }
        }
    }

    public void setViewPager(ViewPager viewPager) {
        releaseViewPager();

        if (viewPager != null) {
            mViewPager = viewPager;
            mViewPager.addOnPageChangeListener(this);

            registerSetObserver();
            if (!isCountSet) {
                setCirclesCount(getViewPagerCount());
            }
        }
    }

    public void releaseViewPager() {
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(this);
            mViewPager = null;
        }
    }

    private void registerSetObserver() {
        if (mDataSetObserver == null && mViewPager != null && mViewPager.getAdapter() != null) {
            mDataSetObserver = new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    if (mViewPager != null && mViewPager.getAdapter() != null) {
                        int count = mViewPager.getAdapter().getCount();
                        setCirclesCount(count);
                    }
                }
            };

            mViewPager.getAdapter().registerDataSetObserver(mDataSetObserver);
        }
    }

    private void unregisterSetObserver() {
        if (mDataSetObserver != null && mViewPager != null && mViewPager.getAdapter() != null) {
            mViewPager.getAdapter().unregisterDataSetObserver(mDataSetObserver);
            mDataSetObserver = null;
        }
    }

    private int getViewPagerCount() {
        if (mViewPager != null && mViewPager.getAdapter() != null) {
            return mViewPager.getAdapter().getCount();
        } else {
            return mCirclesCount;
        }
    }

    private int getXCoordinate(int position) {
        int actualViewWidth = calculateActualViewWidth();
        int x = (getWidth() - actualViewWidth) / 2;

        if (x < 0) {
            x = 0;
        }

        for (int i = 0; i < mCirclesCount; i++) {
            x += mRadiusPx;
            if (position == i) {
                return x;
            }

            x += mRadiusPx + mPaddingPx;
        }

        return x;
    }

    private int calculateActualViewWidth() {
        int width = 0;
        int diameter = mRadiusPx * 2;

        for (int i = 0; i < mCirclesCount; i++) {
            width += diameter;

            if (i < mCirclesCount - 1) {
                width += mPaddingPx;
            }
        }

        return width;
    }

    private void drawIndicatorView(Canvas canvas) {
        int y = getHeight() / 2;

        for (int i = 0; i < mCirclesCount; i++) {
            int x = getXCoordinate(i);
            drawCircle(canvas, i, x, y);
        }
    }

    private void drawCircle(Canvas canvas, int position, int x, int y) {
        boolean selectingItem = (position == selectingPosition || position == selectedPosition);
        boolean isSelectedItem = selectingItem;

        if (isSelectedItem) {
            drawWithAnimationEffect(canvas, position, x, y);
        } else {
            drawWithNoEffect(canvas, position, x, y);
        }
    }

    private void drawWithAnimationEffect(Canvas canvas, int position, int x, int y) {
        drawWithSlideAnimation(canvas, position, x, y);
    }

    private void drawWithNoEffect(Canvas canvas, int position, int x, int y) {
        int color = position == selectedPosition ? mSelectedColor : mUnselectedColor;

        Paint paint = mFillPaint;
        paint.setColor(color);
        canvas.drawCircle(x, y, mRadiusPx, paint);
    }

    private void drawWithSlideAnimation(Canvas canvas, int position, int x, int y) {
        mFillPaint.setColor(mUnselectedColor);
        canvas.drawCircle(x, y, mRadiusPx, mFillPaint);

        if (position == selectingPosition || position == selectedPosition) {
            mFillPaint.setColor(mSelectedColor);
            canvas.drawCircle(mXCoordinate, y, mRadiusPx, mFillPaint);
        }
    }

    private void init(AttributeSet attrs) {
        initAttributes(attrs);
        initAnimation();

        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
    }

    private void initAttributes(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PagerIndicator);
        initCountAttribute(typedArray);
        initColorAttribute(typedArray);
        initAnimationAttribute(typedArray);
        initSizeAttribute(typedArray);
        typedArray.recycle();
    }

    private void initCountAttribute(TypedArray typedArray) {
        registerSetObserver();

        mCirclesCount = typedArray.getInt(R.styleable.PagerIndicator_pi_count, COUNT_NOT_SET);
        if (mCirclesCount != COUNT_NOT_SET) {
            isCountSet = true;
        } else {
            mCirclesCount = 0;
        }

        int position = typedArray.getInt(R.styleable.PagerIndicator_pi_select, 0);
        if (position < 0) {
            position = 0;
        } else if (mCirclesCount > 0 && position > mCirclesCount - 1) {
            position = mCirclesCount - 1;
        }

        selectedPosition = position;
        selectingPosition = position;

        mViewPagerId = typedArray.getResourceId(R.styleable.PagerIndicator_pi_viewpager, 0);
    }

    private void initColorAttribute(TypedArray typedArray) {
        mUnselectedColor = typedArray.getColor(R.styleable.PagerIndicator_pi_unselected_color, getResources().getColor(R.color.unselected_color));
        mSelectedColor = typedArray.getColor(R.styleable.PagerIndicator_pi_selected_color, getResources().getColor(R.color.selected_color));
    }

    private void initAnimationAttribute(TypedArray typedArray) {
        mAnimationDuration = typedArray.getInt(R.styleable.PagerIndicator_pi_animation_duration, SlideAnimation.ANIMATION_DURATION);
    }

    private void initSizeAttribute(TypedArray typedArray) {
        mRadiusPx = (int) typedArray.getDimension(R.styleable.PagerIndicator_pi_radius, DensityUtils.dpToPx(DEFAULT_RADIUS_DP));
        mPaddingPx = (int) typedArray.getDimension(R.styleable.PagerIndicator_pi_padding, DensityUtils.dpToPx(DEFAULT_PADDING_DP));
    }

    private void initAnimation() {
        mSlideAnimation = new SlideAnimation(new SlideAnimation.UpdateListener() {
            @Override
            public void onSlideAnimationUpdated(int xCoordinate) {
                mXCoordinate = xCoordinate;
                invalidate();
            }
        });
    }

    public int getRadiusPx() {
        return mRadiusPx;
    }

    public void setRadiusPx(int radiusPx) {
        if (radiusPx < 0) {
            radiusPx = 0;
        }
        mRadiusPx = DensityUtils.dpToPx(radiusPx);
        invalidate();
    }

    public int getPaddingPx() {
        return mPaddingPx;
    }

    public void setPaddingPx(int paddingPx) {
        if (paddingPx < 0) {
            paddingPx = 0;
        }
        mPaddingPx = DensityUtils.dpToPx(paddingPx);
        invalidate();
    }

    public int getCirclesCount() {
        return mCirclesCount;
    }

    public void setCirclesCount(int count) {
        if (mCirclesCount != count) {
            mCirclesCount = count;
            isCountSet = true;

            requestLayout();
        }
    }

    public boolean isCountSet() {
        return isCountSet;
    }

    public void setCountSet(boolean countSet) {
        isCountSet = countSet;
    }

    public int getUnselectedColor() {
        return mUnselectedColor;
    }

    public void setUnselectedColor(int unselectedColor) {
        mUnselectedColor = unselectedColor;
        invalidate();
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
        invalidate();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectingPosition() {
        return selectingPosition;
    }

    public void setSelectingPosition(int selectingPosition) {
        this.selectingPosition = selectingPosition;
    }

    public int getLastSelectedPosition() {
        return lastSelectedPosition;
    }

    public void setLastSelectedPosition(int lastSelectedPosition) {
        this.lastSelectedPosition = lastSelectedPosition;
    }


    public long getAnimationDuration() {
        return mAnimationDuration;
    }

    public void setAnimationDuration(long animationDuration) {
        mAnimationDuration = animationDuration;
    }

    public DataSetObserver getDataSetObserver() {
        return mDataSetObserver;
    }

    public void setDataSetObserver(DataSetObserver dataSetObserver) {
        mDataSetObserver = dataSetObserver;
    }

    public Paint getFillPaint() {
        return mFillPaint;
    }

    public void setFillPaint(Paint fillPaint) {
        mFillPaint = fillPaint;
    }

    public RectF getRect() {
        return mRect;
    }

    public void setRect(RectF rect) {
        mRect = rect;
    }

    public SlideAnimation getSlideAnimation() {
        return mSlideAnimation;
    }

    public void setSlideAnimation(SlideAnimation slideAnimation) {
        mSlideAnimation = slideAnimation;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public int getViewPagerId() {
        return mViewPagerId;
    }

    public void setViewPagerId(int viewPagerId) {
        mViewPagerId = viewPagerId;
    }
}
