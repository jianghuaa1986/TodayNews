package com.example.xw.todaynews.Domain;

import java.util.ArrayList;

/**
 * Created by xw on 2016/9/3.
 */
public class NewsData {
    public NewTab data;
    public class NewTab{


        public ArrayList<TopNews> topnews;
        public ArrayList<News> news;
        public String title;
        public String more;
}
    public  int retcode;

    public class TopNews{
        public String id;
        public String pubdate;
        public String title;
        public String topimage;
        public String url;

    }
    public class News{
        public String id;
        public String pubdate;
        public String title;
        public String listimage;
        public String url;

    }
}
