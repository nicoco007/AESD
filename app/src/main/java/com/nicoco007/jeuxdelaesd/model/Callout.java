/*
 * Copyright 2016–2017 Nicolas Gnyra
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nicoco007.jeuxdelaesd.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Callout extends RelativeLayout {

    private TextView infoText;

    public Callout(Context context) {

        super(context);

        // children
        LinearLayout bubble = new LinearLayout(context);
        bubble.setId(View.generateViewId());
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(0xFF313231);
        bubble.setBackground(drawable);
        int padding = getDip(context, 17);
        bubble.setPadding(padding, padding, padding, padding);
        LayoutParams bubbleLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(bubble, bubbleLayout);

        Nub nub = new Nub(context);
        nub.setId(View.generateViewId());
        LayoutParams nubLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        nubLayout.addRule(RelativeLayout.BELOW, bubble.getId());
        nubLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(nub, nubLayout);

        LinearLayout labels = new LinearLayout(context);
        labels.setOrientation(LinearLayout.VERTICAL);
        LayoutParams labelsLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        bubble.addView(labels, labelsLayout);

        int maxWidth = getDip(context, 230);
        infoText = new TextView(context);
        infoText.setTextColor(0xFFFFFFFF);
        infoText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
        infoText.setMaxWidth(maxWidth);
        infoText.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        LinearLayout.LayoutParams titleLayout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        labels.addView(infoText, titleLayout);

        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

            }
        });

    }

    private static int getDip(Context context, int pixels) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, context.getResources().getDisplayMetrics());
    }

    public void setTitle(CharSequence text) {
        infoText.setText(text);
    }

    public void transitionIn() {

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        scaleAnimation.setInterpolator(new OvershootInterpolator(1.2f));
        scaleAnimation.setDuration(250);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1f);
        alphaAnimation.setDuration(200);

        AnimationSet animationSet = new AnimationSet(false);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        startAnimation(animationSet);

    }

    private static class Nub extends View {

        private Paint paint = new Paint();
        private Path path = new Path();

        public Nub(Context context) {

            super(context);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xFF313231);
            paint.setAntiAlias(true);

            path.lineTo(getDip(context, 20), 0);
            path.lineTo(getDip(context, 10), getDip(context, 15));
            path.close();

        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(getDip(getContext(), 20), getDip(getContext(), 15));
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
            super.onDraw(canvas);
        }
    }

}