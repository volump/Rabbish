package com.llw.goodtrash.mainpage;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.llw.goodtrash.MainActivity;
import com.llw.goodtrash.R;
import com.llw.goodtrash.adapter.TrashNewsAdapter;
import com.llw.goodtrash.contract.MainContract;
import com.llw.goodtrash.model.TrashNewsResponse;
import com.llw.goodtrash.ui.HistoryActivity;
import com.llw.goodtrash.ui.ImageInputActivity;
import com.llw.goodtrash.ui.NewsDetailsActivity;
import com.llw.goodtrash.ui.TextInputActivity;
import com.llw.goodtrash.ui.VoiceInputActivity;
import com.llw.goodtrash.utils.AppStartUpUtils;
import com.llw.goodtrash.utils.Constant;
import com.llw.goodtrash.utils.NewsHelper;
import com.llw.mvplibrary.mvp.MvpActivity;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import Login.LoginActivity;
import Login.RegisterActivity;

/**
 * 主页面
 *
 * @author llw
 */
public class Main_pageActivity extends AppCompatActivity {


    ImageView button_1 = null;
    ImageView button_2 = null;
    ImageView button_3 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        button_1 = (ImageView) findViewById(R.id.button1);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main_pageActivity.this, Main_2Activity.class);
                startActivity(intent);
            }
        });
        button_2 = (ImageView) findViewById(R.id.button2);
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main_pageActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        button_3 = (ImageView) findViewById(R.id.button3);
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main_pageActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }
}
