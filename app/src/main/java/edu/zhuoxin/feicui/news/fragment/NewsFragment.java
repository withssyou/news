package edu.zhuoxin.feicui.news.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.zhuoxin.feicui.news.R;
import edu.zhuoxin.feicui.news.adapter.NewsAdapter;
import edu.zhuoxin.feicui.news.api.HttpClientListener;
import edu.zhuoxin.feicui.news.app.App;
import edu.zhuoxin.feicui.news.entity.NewsInfo;
import edu.zhuoxin.feicui.news.entity.NewstoJuhe;
import edu.zhuoxin.feicui.news.utils.HttpClientUtil;

/**
 * Created by Administrator on 2017/1/9.
 */

public class NewsFragment extends Fragment {
    public static final String TYPE_TOUTIAO = "top";
    public static final String TYPE_KEJI = "keji";
    public static final String TYPE_GUOJI = "guoji";
    public static final String TYPE_SHEHUI = "shehui";

    public NewsFragment(String type) {
        this.type = type;
    }

    private String type = "top";
    @BindView(R.id.fragment_news_lv)
    ListView listView;
    @BindView(R.id.fragment_news_flush)
    SwipeRefreshLayout refush;
    List<NewsInfo> newsInfoList = new ArrayList<>();
    NewsAdapter adapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (refush.isRefreshing()){  //如果progressbar当前正在显示
                refush.setRefreshing(false);//设置progressbar隐藏
                Toast.makeText(getContext(),"更新成功",Toast.LENGTH_SHORT).show();
            }
            switch (msg.what) {
                case App.SUCCEED:
                    adapter.notifyDataSetChanged();
                    break;
                case App.FALILER:
                    Toast.makeText(getContext(),msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case App.EXCEPTION:
                    Toast.makeText(getContext(),((Exception)msg.obj).getMessage(),Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    /**
     * 网络访问的回调接口
     */
    HttpClientListener listener = new HttpClientListener() {
        @Override
        public void getResultSucceed(String result) {
            //解析数据
            Gson gson = new Gson();
            NewstoJuhe newstoJuhe = gson.fromJson(result, NewstoJuhe.class);
            //获取解析结果
            List<NewstoJuhe.NewsData> datas = newstoJuhe.getResult().getData();
            for (NewstoJuhe.NewsData info : datas) {
                //将解析好的数据添加到数据源中
                newsInfoList.add(new NewsInfo(info.getTitle(), info.getDate(), info.getAuthor_name(), info.getUrl(), info.getThumbnail_pic_s()));
            }
            Message message = handler.obtainMessage();
            message.what = App.SUCCEED;
            handler.sendMessage(message);
//            handler.sendEmptyMessage(App.SUCCEED);
        }

        @Override
        public void getResultFailer(String result) {
            Message message = handler.obtainMessage();
            message.what = App.FALILER;
            message.obj = result;
            handler.sendMessage(message);
        }

        @Override
        public void getResultExctption(Exception e) {
            Message message = Message.obtain();
            message.what = App.EXCEPTION;
            message.obj = e;
            handler.sendMessage(message);
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        //设置下拉刷新
        refush.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        refush.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initListDatas();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new NewsAdapter(getContext());
        adapter.setData(newsInfoList);
        listView.setAdapter(adapter);
        initListDatas();

    }

    /**
     * 初始化数据源
     */
    private void initListDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClientUtil.getResult(new URL(App.BASE_URL + type + App.APP_KRY), listener);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
