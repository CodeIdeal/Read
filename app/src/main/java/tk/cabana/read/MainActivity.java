package tk.cabana.read;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import tk.cabana.read.custom.MyActivity;

public class MainActivity extends MyActivity {

    private TabLayout mTab;
    private ViewPager mViewpager;


    @Override
    public void setMyContentView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void init() {
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mTab = (TabLayout) findViewById(R.id.tab);
        mViewpager = (ViewPager) findViewById(R.id.viewpager);

    }

    private void initData() {

        MyAdapter myadapter = new MyAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(myadapter);

        mTab.setupWithViewPager(mViewpager);
        mTab.setTabsFromPagerAdapter(myadapter);
    }

    private void initEvent() {


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_feedback) {
            //获取客户端环境
            int sdk = Build.VERSION.SDK_INT; // SDK号
            String model = android.os.Build.MODEL; // 手机型号
            String release = android.os.Build.VERSION.RELEASE; // android系统版本号


            Intent data = new Intent(Intent.ACTION_SENDTO);
            data.setData(Uri.parse("mailto:kanniu@163.com"));
            data.putExtra(Intent.EXTRA_SUBJECT, "额...我遇到了一些问题");
            data.putExtra(Intent.EXTRA_TEXT,
                    "『App运行环境』：\n\n" +
                            "       SDK Version:        " + sdk + "\n" +
                            "       model:              " + model + "\n" +
                            "       Android Release:    " + release + "\n\n" +
                            "『bug描述』：\n\n" +
                            "        ");
            startActivity(data);
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public int getCount() {
            // TODO:暂时只做 '知乎' , 'cnbeat' 和 'News(具体内容待定)'
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.getFragmenty(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] tabtitle = new String[]{"Cnbeta", "知乎", "News"};
            return tabtitle[position];
        }
    }
}
