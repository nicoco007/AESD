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

package com.nicoco007.jeuxdelaesd.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nicoco007.jeuxdelaesd.R;
import com.nicoco007.jeuxdelaesd.adapter.DrawerListAdapter;
import com.nicoco007.jeuxdelaesd.model.NavItem;

import java.util.ArrayList;

public class AesdActivity extends AppCompatActivity {

    protected ListView mDrawerList;
    protected RelativeLayout mDrawerPane;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout mDrawerLayout;
    protected ArrayList<NavItem> mNavItems = new ArrayList<>();

    protected void initDrawer() {

        // add navigation items
        mNavItems.add(new NavItem(getString(R.string.title_activity_map), "Carte du camp Robin Hood", R.drawable.ic_map));
        mNavItems.add(new NavItem(getString(R.string.title_activity_countdown), "Décompte jusqu'aux 13es jeux de l'AESD", R.drawable.ic_timer));

        // get drawer pane
        mDrawerPane = (RelativeLayout)findViewById(R.id.drawer_pane);

        // get drawer layout
        mDrawerLayout = (DrawerLayout)mDrawerPane.getParent();

        // get navigation list from drawer
        mDrawerList = (ListView)findViewById(R.id.nav_list);

        // create drawer adapter
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);

        // set drawer adapter
        mDrawerList.setAdapter(adapter);

        // create item click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // call selectItem()
                selectItem(position);

            }

        });

        // create action bar toggle listener
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {

                // call super method
                super.onDrawerOpened(drawerView);

                // invalidate options (tell app that things have changed)
                invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {

                // call super method
                super.onDrawerClosed(drawerView);

                // invalidate options
                invalidateOptionsMenu();

            }

        };

        // set drawer listener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // set action bar
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        // show home (hamburger) icon in action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // inflate menu (this adds items to action bar)
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerPane);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);  // hide options when menu is open
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return mDrawerToggle.onOptionsItemSelected(item);

        /*if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);*/

    }

    private void selectItem(int position) {

        Intent intent;

        switch(position) {

            case 0:
                if(this.getClass() != MapActivity.class) {
                    intent = new Intent(this, MapActivity.class);
                    startActivity(intent);
                }

                break;

            case 1:
                if(this.getClass() != CountdownActivity.class) {
                    intent = new Intent(this, CountdownActivity.class);
                    startActivity(intent);
                }

                break;
        }

        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerPane);

    }

    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);

    }
}
