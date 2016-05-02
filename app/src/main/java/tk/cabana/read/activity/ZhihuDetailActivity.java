package tk.cabana.read.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import tk.cabana.read.Constants;
import tk.cabana.read.R;
import tk.cabana.read.Utils;
import tk.cabana.read.bean.ZhihuDetailBean;
import tk.cabana.read.custom.MyActivity;
import tk.cabana.read.custom.ParallaxScrollView;

/**
 * Created by k on 2016/2/18.
 */
public class ZhihuDetailActivity extends MyActivity {

    private int articleID;
    private String responseString;
    private ZhihuDetailBean mDatas;


    private ParallaxScrollView mZhihudetialContent;
    private ImageView mZhihudetialImg;
    private WebView mZhihudetialWebview;
    private RelativeLayout mZhihudetialLoading;
    private TextView mZhihudetialImgtitle;
    private Bitmap mBitmap;

    @Override
    public void setMyContentView() {
        setContentView(R.layout.activity_zhihudetail);
        final Intent intent = getIntent();
        articleID = intent.getIntExtra("ArticleID", -1);
    }

    @Override
    public void init() {
        initView();
        initData();
    }

    private void initView() {
        mZhihudetialContent = (ParallaxScrollView) findViewById(R.id.zhihudetial_content);
        mZhihudetialImg = (ImageView) findViewById(R.id.zhihudetial_img);
        mZhihudetialWebview = (WebView) findViewById(R.id.zhihudetial_webview);
        mZhihudetialLoading = (RelativeLayout) findViewById(R.id.zhihudetial_loading);
        mZhihudetialImgtitle = (TextView) findViewById(R.id.zhihudetial_imgtitle);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Log.d("kaka", "setMyContentView: "+fab.toString());
        fab.attachToScrollView(mZhihudetialContent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Intent.ACTION_SEND);
                it.putExtra(Intent.EXTRA_TEXT, "分享你一篇文章:\n"+"    "+mDatas.title+"\n"+mDatas.share_url);
                it.setType("text/*");
                startActivity(Intent.createChooser(it, "选择你要分享的应用："));
            }
        });
    }

    private void initData() {
        String url = Constants.GET_ZHIHU_CONTENT_URL + articleID;
        Utils.netRequest(url, new Utils.netRequestListener() {
            @Override
            public void response(String response) {
                Gson gson = new Gson();
                mDatas = gson.fromJson(response,ZhihuDetailBean.class);

                Utils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mZhihudetialLoading.setVisibility(View.GONE);

                        mZhihudetialImgtitle.setText(mDatas.title);

                        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                        Display display = windowManager.getDefaultDisplay();
                        int width = display.getWidth();
                        Picasso.with(ZhihuDetailActivity.this).load(mDatas.image).resize(width, Utils.dp2px(ZhihuDetailActivity.this, 200)).centerCrop().into(mZhihudetialImg);

                        mZhihudetialImg.getDrawingCache(true);

                        //直接使用正则替换也可以实现
                        String HtmlContent = "<head>  </head>"+mDatas.body;
                        //去除html中为头图预留的位置
                        HtmlContent = HtmlContent.replace("<div class=\"img-place-holder\"></div>\n","");
                        Log.d("kaka", "run: " + HtmlContent);
                        if(mDatas.js.size()!=0){
                            HtmlContent = HtmlContent.replace("<head> ","<head> <script src=\""+mDatas.js.get(0) +"+\"></script>");
                        }
                        if(mDatas.css.size()!=0){
                            HtmlContent = HtmlContent.replace("<head> ","<head> <link rel=\"stylesheet\" type=\"text/css\" href=\""+mDatas.css.get(0)+"\">");
                        }
                        System.out.print("run: " + HtmlContent);
                        mZhihudetialWebview.loadData(HtmlContent,"text/html; charset=UTF-8", null);
                    }
                });
            }

            @Override
            public void erro() {

            }
        });
    }

}
