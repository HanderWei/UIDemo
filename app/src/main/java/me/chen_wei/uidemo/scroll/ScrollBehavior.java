package me.chen_wei.uidemo.scroll;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.LinearLayout;

/**
 * author: Chen Wei time: 16/11/23 desc: 描述
 */

public class ScrollBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        return dependency instanceof LinearLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        int offset = dependency.getTop() - child.getTop();
        ViewCompat.offsetTopAndBottom(child, offset);
        return true;
    }
}
