package com.nicoco007.jeuxdelaesd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AesdViewPager extends android.support.v4.view.ViewPager {
    private boolean enabled;

    public AesdViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return enabled && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return enabled && super.onInterceptTouchEvent(ev);
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPagingEnabled() {
        return enabled;
    }
}
