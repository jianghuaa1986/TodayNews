package com.example.xw.todaynews.Domain;

import java.util.ArrayList;

/**
 * Created by xw on 2016/9/5.
 */
public class PhotoBean {
    public int retcode;
    public PhotoData data;

    public class PhotoData {
        public ArrayList<PhotoNewsData> news;
    }

    public class PhotoNewsData {
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String url;
    }
}
