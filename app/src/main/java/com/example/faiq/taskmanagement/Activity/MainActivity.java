package com.example.faiq.taskmanagement.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

import com.example.faiq.taskmanagement.Fragments.AllTasksFragment;
import com.example.faiq.taskmanagement.Fragments.CompletedTasksFragment;
import com.example.faiq.taskmanagement.Fragments.MessagesFragment;
import com.example.faiq.taskmanagement.Fragments.RemainingTasksFragment;
import com.example.faiq.taskmanagement.R;

public class MainActivity extends AppCompatActivity
         {
    public static int navItemIndex = 0;

    private NavigationView navigationView;
    DrawerLayout drawer;
    // tags used to attach the fragments
    private static final String TAG_ALL_TASKS = "all_tasks";
    private static final String TAG_COMPLETED_TASKS = "completed_tasks";
    private static final String TAG_REMAINING_TASKS = "remaining_tasks";
    private static final String TAG_MESSAGES = "messages";

    public static String CURRENT_TAG = TAG_ALL_TASKS;
    private boolean shouldLoadDashboardFragOnBackPress = true;
    private Handler mHandler;
    private String[] activityTitles=new String[]{"All Tasks" , "Completed Tasks" , "Remaining Tasks" , "Messages"};
    Activity activity;
    Toolbar toolbar;
    TextView toolbar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar_title=(TextView)findViewById(R.id.toolbar_title);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mHandler = new Handler();


        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_ALL_TASKS;
            loadHomeFragment();
        }
    }
             private void loadHomeFragment() {
                 // selecting appropriate nav menu item
                 selectNavMenu();

                 setToolbarTitle();

                 if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
                     drawer.closeDrawers();

                     return;
                 }


                 Runnable mPendingRunnable = new Runnable() {
                     @Override
                     public void run() {


                         Fragment fragment = getHomeFragment();
                         FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                         fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                 android.R.anim.fade_out);
                         fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                         fragmentTransaction.commitAllowingStateLoss();


                     }
                 };

                 if (mPendingRunnable != null) {
                     mHandler.post(mPendingRunnable);
                 }

                 drawer.closeDrawers();

                 invalidateOptionsMenu();
             }
             private Fragment getHomeFragment() {
                 switch (navItemIndex) {
                     case 0:
                         // dashboard
                         AllTasksFragment allTasksFragment = new AllTasksFragment();
                         return allTasksFragment;
                     case 1:
                         CompletedTasksFragment completedTasksFragment=new CompletedTasksFragment();
                         return completedTasksFragment;

                     case 2:
                         RemainingTasksFragment remainingTasksFragment=new RemainingTasksFragment();
                         return remainingTasksFragment;
                     case 3:
                         MessagesFragment messagesFragment=new MessagesFragment();
                         return messagesFragment;


                     default:
                         return new AllTasksFragment();

                 }

             }

    private void setToolbarTitle() {
        toolbar_title.setText(activityTitles[navItemIndex]);
    }

             private void selectNavMenu() {
                 navigationView.getMenu().getItem(navItemIndex).setChecked(true);
             }

             private void setUpNavigationView() {
                 //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
                 navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                     // This method will trigger on item Click of navigation menu
                     @Override
                     public boolean onNavigationItemSelected(MenuItem menuItem) {

                         //Check to see which item was being clicked and perform appropriate action
                         switch (menuItem.getItemId()) {
                             //Replacing the main content with ContentFragment Which is our Inbox View;
                             case R.id.nav_all_tasks:
                                 navItemIndex = 0;
                                 CURRENT_TAG = TAG_ALL_TASKS;
                                 break;

                             case R.id.nav_completed_tasks:
                                 navItemIndex=1;
                                 CURRENT_TAG=TAG_COMPLETED_TASKS;
                                 break;

                             case R.id.nav_remaining_tasks:
                                 navItemIndex=2;
                                 CURRENT_TAG=TAG_REMAINING_TASKS;
                                 break;

                             case R.id.nav_messages:
                                 navItemIndex=3;
                                 CURRENT_TAG=TAG_MESSAGES;
                                 break;


                             default:
                                 navItemIndex = 0;
                         }

                         //Checking if the item is in checked state or not, if not make it in checked state
                         if (menuItem.isChecked()) {
                             menuItem.setChecked(false);
                         } else {
                             menuItem.setChecked(true);
                         }

                         menuItem.setChecked(true);

                         loadHomeFragment();

                         return true;
                     }
                 });


                 ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

                     @Override
                     public void onDrawerClosed(View drawerView) {
                         // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                         super.onDrawerClosed(drawerView);
                     }

                     @Override
                     public void onDrawerOpened(View drawerView) {
                         // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                         super.onDrawerOpened(drawerView);
                     }
                 };

                 //Setting the actionbarToggle to drawer layout
                 drawer.setDrawerListener(actionBarDrawerToggle);

                 //calling sync state is necessary or else your hamburger icon wont show up
                 actionBarDrawerToggle.syncState();
             }

             @Override
             public void onBackPressed() {
                 if (drawer.isDrawerOpen(GravityCompat.START)) {
                     drawer.closeDrawers();
                     return;
                 }

                 // This code loads home fragment when back key is pressed
                 // when user is in other fragment than home
                 if (shouldLoadDashboardFragOnBackPress) {
                     // checking if user is on other navigation menu
                     // rather than home
                     if (navItemIndex != 0) {
                         navItemIndex = 0;
                         CURRENT_TAG = TAG_ALL_TASKS;
                         loadHomeFragment();
                         return;
                     }
                 }

                 super.onBackPressed();
             }

             @Override
             public boolean onCreateOptionsMenu(Menu menu) {
                 // Inflate the menu; this adds items to the action bar if it is present.
                 // getMenuInflater().inflate(R.menu.activity_main_navigation_drawer_drawer, menu);
                 return false;
             }



         }
