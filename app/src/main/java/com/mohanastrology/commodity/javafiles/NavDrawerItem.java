package com.mohanastrology.commodity.javafiles;

/**
 * Created by niraj on 11/12/2015.
 */
public class NavDrawerItem  {
    private boolean showNotify;
    private String title;
    private int icon;


        public NavDrawerItem() {

        }
        public NavDrawerItem(String title, int icon) {
            this.title = title;
            this.icon = icon;
        }
        public NavDrawerItem(boolean showNotify, String title, int icon) {
            this.showNotify = showNotify;
            this.title = title;
            this.icon = icon;
        }

        public boolean isShowNotify() {
            return showNotify;
        }

        public void setShowNotify(boolean showNotify) {
            this.showNotify = showNotify;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

}