package tk.cabana.read.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import tk.cabana.read.Constants;
import tk.cabana.read.R;
import tk.cabana.read.Utils;
import tk.cabana.read.bean.ZhihuBean;

/**
 * Created by   KY on 2016/1/28.
 * 项目名:      Read
 * 包名:       tk.cabana.read.fragment
 * 类名:	      ZhihuFragment
 * 创建者:	   康阳
 * 创建时间:   2016/1/28	22:06
 * 描述:	TODO
 * <p/>
 * svn版本:   $$Rev$
 * 更新人:     $$Author$
 * 更新时间:    $$Date$
 * 更新描述:    TODO
 */
public class ZhihuFragment extends android.support.v4.app.Fragment {
    private SwipeRefreshLayout mRootView;

    private LinearLayout mZhihuView;
    private ViewPager mZhihuTop;
    private GridView mZhihuGridview;
    private RelativeLayout mZhihuLoading;

    private boolean flag;
    private ZhihuBean mData;
    private ZhihuGridViewAdapter mGridAdapter;
    private String TAG = "ZhihuFragment";
    private ZhihuViewPagerAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = (SwipeRefreshLayout) View.inflate(Utils.getContext(), R.layout.fragment_zhihu, null);

        mRootView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (flag != true) {
                    refreshdata();
                } else {
                    mRootView.setRefreshing(false);
                }
            }
        });

        mZhihuView = (LinearLayout) mRootView.findViewById(R.id.zhihu_view);
        mZhihuTop = (ViewPager) mRootView.findViewById(R.id.zhihu_top);
        mZhihuGridview = (GridView) mRootView.findViewById(R.id.zhihu_gridview);
        mZhihuLoading = (RelativeLayout) mRootView.findViewById(R.id.zhihu_loading);

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.netRequest(Constants.GET_ZHIHU_NEWS_URL, new Utils.netRequestListener() {
            @Override
            public void response(String response) {
                Gson gson = new Gson();
                Log.d(TAG, "response: " + response);
                mData = gson.fromJson(response, ZhihuBean.class);
                Utils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        //绑定数据

                        //给gridview设置数据
                        mGridAdapter = new ZhihuGridViewAdapter();
                        mZhihuGridview.setAdapter(mGridAdapter);

                        //给viewpager设置数据
                        mPagerAdapter = new ZhihuViewPagerAdapter();
                        mZhihuTop.setAdapter(mPagerAdapter);

                        refreshdata();

                    }
                });
            }
        });
    }

    private void refreshdata() {
        mZhihuLoading.setVisibility(View.GONE);
    }


    private class ZhihuGridViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (mData != null) {
                return mData.stories.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mData != null) {
                return mData.stories.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            class ViewHolder {
                ImageView zhihuListviewImg;
                TextView zhihuListviewTitle;
            }
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_list_zhihu, null);
                viewHolder = new ViewHolder();
                viewHolder.zhihuListviewImg = (ImageView) convertView.findViewById(R.id.zhihu_listview_img);
                viewHolder.zhihuListviewTitle = (TextView) convertView.findViewById(R.id.zhihu_listview_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ZhihuBean.StoriesEntity storiesEntity = mData.stories.get(position);
            Picasso.with(Utils.getContext()).load(storiesEntity.images.get(0)).into(viewHolder.zhihuListviewImg);
            viewHolder.zhihuListviewTitle.setText(storiesEntity.title);

            return convertView;
        }
    }

    private class ZhihuViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (mData != null) {
                return mData.top_stories.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==(View)object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getContext());
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            int width = display.getWidth();
            Picasso.with(getContext()).load(""+mData.top_stories.get(position).image).resize(width,Utils.dp2px(getContext(),200)).into(imageView);
            container.addView(imageView);
            return imageView;
        }



        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
