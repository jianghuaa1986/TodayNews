package com.example.xw.todaynews.Domain;

import java.util.ArrayList;

/**
 * Created by xw on 2016/8/27.
 */
public class NewsMenuData {
    public int retcode;
    public ArrayList<String> extend;
    public ArrayList<NewsData> data;

    public class NewsData {
        public String id;
        public String title;
        public int type;
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsData [title=" + title + ", children=" + children + "]";
        }
    }

    // 页签信息封装
    public class NewsTabData {
        public String id;
        public String title;
        public String url;
        public int type;

        @Override
        public String toString() {
            return "NewsTabData [title=" + title + "]";
        }
    }

    @Override
    public String toString() {
        return "NewsMenuData [data=" + data + "]";
    }
}
