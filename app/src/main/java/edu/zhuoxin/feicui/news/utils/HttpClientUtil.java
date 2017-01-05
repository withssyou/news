package edu.zhuoxin.feicui.news.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.zhuoxin.feicui.news.api.HttpClientListener;

/**
 * Created by Administrator on 2017/1/5.
 *  网络连接获取结果的工具类
 *      HttpUrlConnection:
 *          url:
 */
public class HttpClientUtil {
    /**
     *   获取服务器端数据
     * @param targetUrl   请求的url
     * @param listener     执行完下载操作之后的回调接口
     */
    public static void getResult(URL targetUrl , HttpClientListener listener)  {
        HttpURLConnection conn = null;
        BufferedReader br = null;
        try {
            conn = (HttpURLConnection) targetUrl.openConnection();
            conn.setRequestMethod("GET");//设置请求方式
            conn.setReadTimeout(15000);//设置读取超时
            conn.setConnectTimeout(8000);//设置连接超时
//            conn.setRequestProperty(); //设置请求头信息
            //网络连接
            conn.connect();
            if (conn.getResponseCode() == 200){//如果状态码等于200，连接成功
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String str = "";
                StringBuffer sb = new StringBuffer();
                while ((str = br.readLine()) != null){
                    sb.append(str);
                }
                //最终结果就是sb.toString();
                listener.getResultSucceed(sb.toString());
            }
            else {
                listener.getResultFailer("网络连接失败，请检查网络设置");
            }
        } catch (IOException e) {
            listener.getResultExctption(e);
        }finally {
            if (br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null){
                conn.disconnect();//关闭连接
            }
        }
    }
}