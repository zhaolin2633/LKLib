package com.lklib;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lklib.http.ApiService;

import java.io.File;

import cn.app.library.base.BaseAppCompatActivity;
import cn.app.library.dialog.ChooseImageDialog;
import cn.app.library.dialog.picker.PickImageHelper;
import cn.app.library.dialog.picker.constant.Extras;
import cn.app.library.dialog.styleddialog.StyledDialog;
import cn.app.library.dialog.styleddialog.interfaces.MyDialogListener;
import cn.app.library.http.HttpResult;
import cn.app.library.http.HttpResultSubscriber;
import cn.app.library.picture.lib.compress.OnCompressListener;
import cn.app.library.utils.PictureUtils;
import cn.app.library.widget.glideimageview.GlideImageLoader;


public class MainActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        httpGet();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            ChooseImageDialog dialog = ChooseImageDialog.newInstance();
            dialog.setOperator(new ChooseImageDialog.Operator() {
                @Override
                public void onGetImage(final String path) {
                    PictureUtils.getInstance().CompressImage(MainActivity.this, path, new OnCompressListener() {
                        @Override
                        public void onStart() {
                            showLoding("压缩中...");
                        }

                        @Override
                        public void onSuccess(File file) {
                              dismissLoading();
//                            ImageLoaderProxy.getInstance().loadImage("file://" + file.getPath(), mIvBusinessImg, R.drawable.icon_add, R.drawable.icon_add);
//                            imagePath = file.getPath();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            dismissLoading();
//                            ImageLoaderProxy.getInstance().loadImage("file://" + path, mIvBusinessImg, R.drawable.icon_add, R.drawable.icon_add);
//                            imagePath = path;
                        }
                    });
                }
            });
            dialog.show(getSupportFragmentManager(), null);
        } else if (id == R.id.nav_gallery) {
            chooseImage();
        } else if (id == R.id.nav_slideshow) {
            StyledDialog.buildNormalInput("Title", "输入名字", "确定", "取消", new MyDialogListener() {
                @Override
                public void onFirst() {

                }

                @Override
                public void onSecond() {

                }
            }).show();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
                        showLoding("SS");
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * 选择头像
     */
    private void chooseImage() {
        PickImageHelper.PickImageOption option = new PickImageHelper.PickImageOption();
        option.titleResId=R.string.picture_set_header;
        option.crop = true;
        option.multiSelect = false;
        option.cropOutputImageWidth = 400;
        option.cropOutputImageHeight = 400;
        PickImageHelper.pickImage(this, PickImageHelper.PICK_AVATAR_REQUEST, option);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PickImageHelper.PICK_AVATAR_REQUEST) {
            String path = data.getStringExtra(Extras.EXTRA_FILE_PATH);

        }
    }
    public void httpGet() {
        HttpMnager.getInstance()
                .createService(ApiService.class)
                .getString()
                .compose(transformer(this.<HttpResult<String>>bindToLifecycle()))
                .subscribe(new HttpResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void _onError(String msg, int code) {
                           showToast(msg);
                    }

                    @Override
                    public void onFinished() {

                    }
                });
    }

}
