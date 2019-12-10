package com.jibres.android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jibres.android.Adaptor.Adaptor_Main;
import com.jibres.android.Item.item_Main;
import com.jibres.android.R;
import com.jibres.android.Static.url;
import com.jibres.android.api.apiV6;
import com.jibres.android.utility.Dialog;
import com.jibres.android.utility.SaveManager;
import com.jibres.android.utility.set_language_device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListNews extends AppCompatActivity {

    RecyclerView recylerview_listNews;
    Adaptor_Main adaptor_main_list;
    LinearLayoutManager LayoutManager_list;
    ArrayList<item_Main> itemMains;

    @Override
    protected void onResume() {
        new set_language_device(this);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        RelativeLayout news_mainLayout = findViewById(R.id.listNews_mainLayout);
        String AppLanguage = SaveManager.get(this).getstring_appINFO().get(SaveManager.appLanguage);
        if (AppLanguage.equals("fa") || AppLanguage.equals("ar")){
            ViewCompat.setLayoutDirection(news_mainLayout,ViewCompat.LAYOUT_DIRECTION_RTL);
        }else {
            ViewCompat.setLayoutDirection(news_mainLayout,ViewCompat.LAYOUT_DIRECTION_LTR);
        }


        String urlApp = SaveManager.get(getApplication()).getstring_appINFO().get(SaveManager.apiV6_URL)+ url.posts;
        itemMains = new ArrayList<>();
        recylerview_listNews = findViewById(R.id.recyclerview_listnews);

        adaptor_main_list = new Adaptor_Main(itemMains, this);
        LayoutManager_list = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recylerview_listNews.setAdapter(adaptor_main_list);

        apiV6.listNews(urlApp,"50", new apiV6.listNewsListener() {

            @Override
            public void lestener_news(String newsArray) {
                news(newsArray);
                hiddenProgress();
            }

            @Override
            public void error() {
                Intent getintent = getIntent();
                new Dialog(ListNews.this,getString(R.string.errorNet_title_snackBar),"",getString(R.string.errorNet_button_snackBar),false,getintent);
            }
        });
    }

    private void news(String responeArray){
        try {
            JSONArray newsArray = new JSONArray(responeArray);

            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject object_news = newsArray.getJSONObject(i);
                String title_news = object_news.getString("title");
                String content_news = object_news.getString("content");
                content_news.replace("\n -  ","");
                String id_news = object_news.getString("id");
                String url_news = object_news.getString("curl");

                JSONObject meta = object_news.getJSONObject("meta");
                String image_news = meta.getString("thumb");

                Spanned html_contentNews = Html.fromHtml(content_news);
                String text_news = String.valueOf(html_contentNews);
                if (text_news.length() > 110){
                    text_news = text_news.substring(0,110) + " ...";
                }

                itemMains.add(new item_Main(item_Main.NEWS,null,null,null,
                        null,null,null,
                        null,null,null,null,null,null,
                        null,null,null,null,null,
                        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
                        null,null,null,null,
                        null,
                        null,null,null,
                        null,null,null,
                        image_news,title_news,text_news,id_news,
                        null,
                        null,null,null,null,
                        null));
                recylerview_listNews.setLayoutManager(LayoutManager_list);
                recylerview_listNews.setItemAnimator(new DefaultItemAnimator());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void hiddenProgress(){
        try {
            ProgressBar progressBar = findViewById(R.id.listNews_progress);
            if (progressBar.getVisibility() == View.VISIBLE){
                progressBar.setVisibility(View.GONE);
            }
        }catch (Exception e){}
    }
}