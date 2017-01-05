package edu.zhuoxin.feicui.news.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import edu.zhuoxin.feicui.news.R;
import edu.zhuoxin.feicui.news.api.HttpClientListener;
import edu.zhuoxin.feicui.news.app.App;
import edu.zhuoxin.feicui.news.entity.NewstoJuhe;
import edu.zhuoxin.feicui.news.utils.HttpClientUtil;

/**
 * Created by Administrator on 2017/1/5.
 */

public class TestActivity extends AppCompatActivity {
    public static final String TYPE_TOP = "top";
    public static final String TYPE_GUOJI= "guoji";
    public static final String TYPE_KEJI = "keji";
    //.....
    private HttpClientListener listener = new HttpClientListener() {
        @Override
        public void getResultSucceed(String result) {
             System.out.println(result);
            //Json解析
            Gson gson = new Gson();
            //Gson解析
            NewstoJuhe newstoJuhe =  gson.fromJson(result,NewstoJuhe.class);
            //获取解析结果
            List<NewstoJuhe.NewsData> datas = newstoJuhe.getResult().getData();
            for (NewstoJuhe.NewsData info : datas){
                System.out.println(info);
                //将解析好的数据添加到数据源中

            }
        }

        @Override
        public void getResultFailer(final String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TestActivity.this,result,Toast.LENGTH_LONG).show();
                }
            });
        }
        @Override
        public void getResultExctption(Exception e) {
//            Toast.makeText(TestActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asynctask_activity);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClientUtil.getResult(new URL(App.BASE_URL+TYPE_GUOJI+App.APP_KRY),listener);
                } catch (MalformedURLException e) {}
            }
        }).start();
    }
}
