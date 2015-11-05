package com.tesmple.crowdsource.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.adapter.ViewPagerAdapter;
import com.tesmple.crowdsource.fragment.AcceptableBillFragment;
import com.tesmple.crowdsource.fragment.AcceptedBillFragment;
import com.tesmple.crowdsource.fragment.MyPublishFragment;
import com.tesmple.crowdsource.object.Notification;
import com.tesmple.crowdsource.object.NotificationLab;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.ActivityCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import im.fir.sdk.FIR;
import im.fir.sdk.callback.VersionCheckCallback;
import im.fir.sdk.version.AppVersion;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Observer {

    /**
     * 主界面的tablayout
     */
    private TabLayout tlMain;

    /**
     * 主界面的viewpager
     */
    public static ViewPager vpMain;

    /**
     * 装viewpager的每一个界面的list
     */
    private List<String> titleList;

    /**
     * 装fragment的每一个对象的list
     */
    private List<Fragment> fragmentList;

    /**
     * dev里面的头像
     */
    private SimpleDraweeView sdvHeadPortrait;

    /**
     * dev里面的名字
     */
    private TextView tvName;

    /**
     * dev里面的名字
     */
    private TextView tvDepartment;

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

        sdvHeadPortrait = (SimpleDraweeView) findViewById(R.id.nav_sdv_head_portrait);
        tvName = (TextView) findViewById(R.id.nav_tv_name);
        tvDepartment = (TextView) findViewById(R.id.nav_tv_department);

        sdvHeadPortrait.setImageURI(Uri.parse(AVUser.getCurrentUser().getAVFile("head_portrait").getUrl()));
        tvName.setText(User.getInstance().getNickName());
        tvDepartment.setText(User.getInstance().getDepartment());
    }

    /**
     * 初始化各个控件
     */
    private void init() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.clearAnimation();
        fab.cancelLongPress();
        fab.setAnimation(null);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                int[] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);
                startingLocation[0] += view.getWidth() / 2;
                PostRequestActivity.startUserProfileFromLocation(startingLocation, MainActivity.this);
                overridePendingTransition(0, 0);
//                Intent intent = new Intent(MainActivity.this, PostRequestActivity.class);
//                startActivity(intent);
//                NotificationLab.getInstance().addNotification(new Notification());
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
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            AVUser.logOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_version) {
//            showVersionDialog();
            checkForNewVersion();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 检查新版本的方法
     */
    private void checkForNewVersion() {
        FIR.checkForUpdateInFIR("a317113b67b6856ebbcf9dc4745aae21", new VersionCheckCallback() {
            @Override
            public void onSuccess(AppVersion appVersion, boolean b) {
                try {
                    if (appVersion.getVersionName().equals(getVersionName())) {
                        showVersionDialog();
                    } else {
                        showNewVersionDialog(appVersion.getChangeLog(), appVersion.getUpdateUrl());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String s, int i) {
                Snackbar.make(tvName, R.string.error_someting_worng, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Log.e("CheckNewVersionError", e.getMessage() + "===" + e.getCause());
                Snackbar.make(tvName, R.string.please_check_your_network, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 显示关于版本号的dialog
     */
    private void showVersionDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.title_remind)
                .setMessage(R.string.prompt_new_version_now)
                .setPositiveButton(R.string.prompt_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    /**
     * 显示有新版本的方法
     *
     * @param updateLog 新版本打印的log
     * @param url       下载新版本的url
     */
    private void showNewVersionDialog(String updateLog, final String url) {
        new AlertDialog.Builder(this).setTitle(R.string.title_have_new_version)
                .setMessage(updateLog)
                .setPositiveButton(R.string.prompt_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.prompt_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 外部修改viewpager的页面
     *
     * @param i
     */
    public static void changeViewpagerItem(int i) {
        vpMain.setCurrentItem(i);
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
        } else {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().getItem(0).setTitle(R.string.prompt_notification);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (NotificationLab.getInstance().isExistNotRead()) {
            navigationView.getMenu().getItem(0).setTitle(R.string.prompt_new_notification);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_new_24dp);
        } else {
            navigationView.getMenu().getItem(0).setTitle(R.string.prompt_notification);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }

        FIR.checkForUpdateInFIR("a317113b67b6856ebbcf9dc4745aae21", new VersionCheckCallback() {
            @Override
            public void onSuccess(AppVersion appVersion, boolean b) {
                try {
                    if (!appVersion.getVersionName().equals(getVersionName())) {
                        navigationView.getMenu().getItem(4).setTitle(getString(R.string.prompt_versoin) + getVersionName() +
                                "\t\t(有新版本)");
                        toolbar.setNavigationIcon(R.drawable.ic_menu_white_new_24dp);
                    } else {
                        navigationView.getMenu().getItem(4).setTitle(getString(R.string.prompt_versoin) + "\t(" + getVersionName() + ")");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String s, int i) {
                try {
                    navigationView.getMenu().getItem(4).setTitle(getString(R.string.prompt_versoin) + "\t(" + getVersionName() + ")");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("CheckNewVersionError", e.getMessage() + "===" + e.getCause());
                try {
                    navigationView.getMenu().getItem(4).setTitle(getString(R.string.prompt_versoin) + "\t(" + getVersionName() + ")");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });

        sdvHeadPortrait.setImageURI(Uri.parse(AVUser.getCurrentUser().getAVFile("head_portrait").getUrl()));
        tvName.setText(AVUser.getCurrentUser().get("nickname").toString());
        tvDepartment.setText(User.getInstance().getDepartment());
    }

    /**
     * 获得版本号的方法
     *
     * @return 当前应用的版本号
     * @throws Exception
     */
    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

}
