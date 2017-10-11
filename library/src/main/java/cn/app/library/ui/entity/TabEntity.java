package cn.app.library.ui.entity;


import cn.app.library.widget.tablayout.listener.CustomTabEntity;

public class TabEntity implements CustomTabEntity {
    public String title;
    public String firstTitle;
    public int selectedIcon;
    public int unSelectedIcon;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }
    public TabEntity(String firstTitle,String title, int selectedIcon, int unSelectedIcon) {
        this.firstTitle = firstTitle;
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }
    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public String getTabFirstTitle() {
        return firstTitle;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
