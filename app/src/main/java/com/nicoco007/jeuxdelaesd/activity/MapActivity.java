/*
 * Copyright © 2016 Nicolas Gnyra
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

package com.nicoco007.jeuxdelaesd.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.nicoco007.jeuxdelaesd.AesdViewPager;
import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.events.ShowMapCoordsEvent;
import com.nicoco007.jeuxdelaesd.fragment.BuildingsFragment;
import com.nicoco007.jeuxdelaesd.fragment.MapFragment;
import com.nicoco007.jeuxdelaesd.fragment.ScheduleFragment;
import com.nicoco007.jeuxdelaesd.adapter.ViewPagerAdapter;
import com.nicoco007.jeuxdelaesd.helper.APICommunication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MapActivity extends AesdActivity {

    private AesdViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        APICommunication.loadLocations(true);

        // set up activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        super.initDrawer();

        // set action bar title
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.title_activity_map));

        // get viewpager
        viewPager = (AesdViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs_activities);

        if (viewPager != null && tabLayout != null) {

            // create fragments for viewpager
            Fragment mapFragment = new MapFragment();
            Fragment scheduleFragment = new ScheduleFragment();
            Fragment buildingsFragment = new BuildingsFragment();

            // create viewpager adapter and add fragments
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(mapFragment, "Carte");
            adapter.addFragment(scheduleFragment, "Horaire");
            adapter.addFragment(buildingsFragment, "Lieux");

            // set adapter
            viewPager.setAdapter(adapter);

            // prevent unloading of map
            viewPager.setOffscreenPageLimit(2);

            viewPager.setPagingEnabled(false);

            // get TabLayout and set it up with ViewPager
            tabLayout.setupWithViewPager(viewPager);

        }

        // register EventBus
        EventBus eventBus = EventBus.getDefault();
        eventBus.register(this);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        View v = getCurrentFocus();

        if ((ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && v instanceof EditText) {

            int viewCoords[] = new int[2];
            v.getLocationOnScreen(viewCoords);
            float x = ev.getRawX() + v.getLeft() - viewCoords[0];
            float y = ev.getRawY() + v.getTop() - viewCoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);

        }

        return super.dispatchTouchEvent(ev);

    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Subscribe
    public void onEvent(ShowMapCoordsEvent e) {

        // switch to map view
        viewPager.setCurrentItem(0);

    }

}
