package com.tesmple.crowdsource.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVUser;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.adapter.ViewPagerAdapter;
import com.tesmple.crowdsource.fragment.AcceptableBillFragment;
import com.tesmple.crowdsource.fragment.AcceptedBillFragment;
import com.tesmple.crowdsource.fragment.MyPublishFragment;
import com.tesmple.crowdsource.object.Notification;
import com.tesmple.crowdsource.object.NotificationLab;
import com.tesmple.crowdsource.utils.ActivityCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Observer {

    /**
     * 主界面的tablayout
     */
    private TabLayout tlMain;

    /**
     * 主界面的viewpager
     */
    private ViewPager vpMain;

    /**
     * 装viewpager的每一个界面的list
     */
    private List<String> titleList;

    /**
     * 装fragment的每一个对象的list
     */
    private List<Fragment> fragmentList;

    /**
     * 这个页面的toolbar
     */
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        ActivityCollector.finishAllExceptNow(this);

        initToolbarAndDrawer();

        NotificationLab.getInstance().addObserver(this);

        init();
    }

    /**
     * 初始化toolbar的方法
     */
    private void initToolbarAndDrawer() {

        toolbar = (Toolbar) findViewById(R.id.tb_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setTitle(R.string.real_app_name);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

    }

    /**
     * 初始化各个控件
     */
    private void init() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, PostRequestActivity.class);
//                startActivity(intent);
                NotificationLab.getInstance().addNotification(new Notification());
            }
        });

        initList();

        tlMain = (TabLayout) findViewById(R.id.tl_main);
        vpMain = (ViewPager) findViewById(R.id.vp_main);

        vpMain.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titleList, fragmentList));
        tlMain.setupWithViewPager(vpMain);
        tlMain.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 初始化两个list的方法
     */
    private void initList() {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();

        titleList.add(getString(R.string.acceptable_bill));
        titleList.add(getString(R.string.my_publish));
        titleList.add(getString(R.string.accepted_bill));

        fragmentList.add(new AcceptableBillFragment());
        fragmentList.add(new MyPublishFragment());
        fragmentList.add(new AcceptedBillFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notification) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            AVUser.logOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (NotificationLab.getInstance().isExistNotRead()) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().getItem(0).setTitle(R.string.prompt_new_notification);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_new_24dp);
        }else {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().getItem(0).setTitle(R.string.prompt_notification);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }
    }
}
