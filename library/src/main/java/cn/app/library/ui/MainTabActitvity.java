package cn.app.library.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import cn.app.library.R;
import cn.app.library.base.BaseAppCompatActivity;
import cn.app.library.ui.entity.TabEntity;
import cn.app.library.widget.tablayout.CommonTabLayout;
import cn.app.library.widget.tablayout.listener.CustomTabEntity;
import cn.app.library.widget.tablayout.listener.OnTabSelectListener;

/**
 * <pre>
 * author : zhaolin
 * time : 2017/09/18
 * desc :底部tab
 * </pre>
 */
public abstract class MainTabActitvity extends BaseAppCompatActivity {
    protected ViewPager viewPager;
    protected CommonTabLayout mainTabLayout;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"首页", "消息", "联系人", "更多"};
    private int[] mIconUnselectIds = {
            R.drawable.tab_home_unselect, R.drawable.tab_speech_unselect,
            R.drawable.tab_contact_unselect, R.drawable.tab_more_unselect};
    private int[] mIconSelectIds = {R.drawable.tab_home_select, R.drawable.tab_speech_select,
            R.drawable.tab_contact_select, R.drawable.tab_more_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_main_tab;
    }

    public abstract ArrayList<Fragment> initFragments();

    public abstract String[] initTitles();

    public abstract int[] iconUnselectIds();

    public abstract int[] iconSelectIds();

    @Override
    protected void initView() {
        super.initView();
        viewPager = findView(R.id.viewPager);
        mainTabLayout = findView(R.id.mainTabLayout);
    }

    @Override
    protected void initData() {
        super.initData();
        if (initTitles() != null && initTitles().length > 0) {
            mFragments = initFragments();
            mTitles = initTitles();
            mIconUnselectIds = iconUnselectIds();
            mIconSelectIds = iconSelectIds();
        }
        initTabViewPager();
    }

    private void initTabViewPager() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        viewPager.setOffscreenPageLimit(mTitles.length);
        viewPager.setAdapter(new TabViewPagerAdapter(getSupportFragmentManager()));
        mainTabLayout.setTabData(mTabEntities);
        mainTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mainTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class TabViewPagerAdapter extends FragmentPagerAdapter {
        public TabViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
