package tk.cabana.read.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tk.cabana.read.Constants;
import tk.cabana.read.R;
import tk.cabana.read.Utils;
import tk.cabana.read.bean.CnbetaDetailBean;

/**
 * Created by k on 2016/2/18.
 */
public class ZhihuDetailActivity extends AppCompatActivity {

    private int articleID;
    private String responseString;
    private CnbetaDetailBean mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cnbetadetail);
        Intent intent = getIntent();
        articleID = intent.getIntExtra("ArticleID", -1);
        Toast.makeText(ZhihuDetailActivity.this, articleID + "", Toast.LENGTH_SHORT).show();
        initView();
        initData();
        initEvent();
    }

    private void initView() {

    }

    private void initData() {
        Utils.newThreadtask(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(Constants.GET_CNBETA_CONTENT_URL + articleID).get().build();

                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    responseString = response.body().string();
                    Log.d("kaka", responseString);
                    Gson gson = new Gson();
//                    mDatas = gson.fromJson(responseString, CnbetaDetailBean.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Utils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("kaka", "run: 刷新内容");


                    }
                });
            }
        });
    }

    private void initEvent() {

    }
}
