package edu.zhuoxin.feicui.news.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.zhuoxin.feicui.news.R;

/**
 * Created by Administrator on 2017/1/5.
 */

public class AsyncTaskActivity extends AppCompatActivity {

    @BindView(R.id.asynctask_pb)
    ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asynctask_activity);
        ButterKnife.bind(this);
//        pb = (ProgressBar) findViewById(R.id.asynctask_pb);
        //创建异步任务对象，并启动异步任务，让异步任务帮我们更新UI
        AsyncTaskDemo task = new AsyncTaskDemo(pb,this);
        task.execute();
    }
}
