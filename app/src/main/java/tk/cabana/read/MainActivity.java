package tk.cabana.read;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTab;
    private ViewPager mViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init() {
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
        if (id == R.id.action_settings) {
            return true;
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
            String[] tabtitle = new String[]{"Cnbate","知乎","News"};
            return tabtitle[position];
        }
    }
}
