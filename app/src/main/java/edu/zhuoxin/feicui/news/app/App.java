package edu.zhuoxin.feicui.news.app;

import android.app.Application;

/**
 * Created by Administrator on 2017/1/5.
 *  全局唯一，并且优先于所有人初始化
 */

public class App extends Application {
    //网络访问接口
    public static final String BASE_URL = "http://v.juhe.cn/toutiao/index?type=";
    public static final String APP_KRY = "&key=db3cab4bb2c89b9bb03ad01e5f1c9143";
    //接口回调的状态码
    public static  final  int SUCCEED = 0x10;
    public static  final  int FALILER = 0x12;
    public static  final  int EXCEPTION = 0x14;


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
