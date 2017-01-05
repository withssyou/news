package edu.zhuoxin.feicui.news.demo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/1/5.
 *  异步任务：
 *      说android两点约束
 *      专门用来做子线程更新UI的操作
 *
 *  异步任务中封装了子线程和线程池
 *  AsyncTask的三个泛型参数：
 *      参数一：doInBackground方法的传入参数
 *      参数二：onProgressUpdate方法的传入参数
 *      参数三：doInBackground方法的返回值类型
 *              onPostExecute方法的传入参数
 *
 *
 */

public class AsyncTaskDemo extends AsyncTask<String,Integer,String>{

    private ProgressBar pb;
    private Context context;

    public AsyncTaskDemo(ProgressBar pb,Context context) {
        this.pb = pb;
        this.context = context;
    }

    /**
     *  预先执行的方法
     *      在异步任务开始之前执行的方法
     *   所有初始化的操作，都可以放在该方法中
     *   主线程的方法
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    /**
     *  异步任务后台执行的方法
     *      是子线程执行的方法，可以做耗时的操作
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {
        Log.i("Tag", "doInBackground");
        //网络请求----> json字符串
        for (int i = 1; i <= 100 ; i++){
            try {
                Thread.sleep(1000);
                //发布进度的方法
                publishProgress(i,19,29,49);  //发消息
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "更新完毕";
    }

    /**
     *  进度更新的方法
     *      只有在异步任务的doInBackGround方法中publishProgress发布进度，才会调用该方法
     * @param v
     */
    @Override
    protected void onProgressUpdate(Integer... v) {  // Integer... values  , 可变参数 把可变参数当作数组来使用
        super.onProgressUpdate(v);
        pb.setProgress(v[0]);

    }

    /**
     *  用来处理耗时操作结果的方法
     *      当后台耗时操作执行完之后，来处理结果
     *
     * @param aVoid
     */
    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        //处理网络访问返回的json数据
        Toast.makeText(context,aVoid,Toast.LENGTH_LONG).show();
    }
}
