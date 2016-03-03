package tk.cabana.read.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import me.relex.circleindicator.CircleIndicator;
import tk.cabana.read.Constants;
import tk.cabana.read.R;
import tk.cabana.read.Utils;
import tk.cabana.read.activity.ZhihuDetailActivity;
import tk.cabana.read.bean.ZhihuBean;
import tk.cabana.read.custom.HeaderGridView;

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
    private HeaderGridView mZhihuGridview;
    private RelativeLayout mZhihuLoading;

    private RelativeLayout mZhihuTop;
    private ViewPager mZhihuTop_ViewPager;
    private CircleIndicator mZhihuTop_Indicator;
    private TextView mZhihuTop_Title;


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
//        mZhihuTop = (ViewPager) mRootView.findViewById(R.id.zhihu_top);
        mZhihuGridview = (HeaderGridView) mRootView.findViewById(R.id.zhihu_gridview);

        mZhihuTop = (RelativeLayout) View.inflate(getContext(), R.layout.indicatorviewpager, null);
        mZhihuTop_ViewPager = (ViewPager) mZhihuTop.findViewById(R.id.zhihutop_viewpager);
        mZhihuTop_Indicator = (CircleIndicator) mZhihuTop.findViewById(R.id.zhihutop_Indicator);
        mZhihuTop_Title = (TextView) mZhihuTop.findViewById(R.id.zhihutop_title);


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
                Log.d(TAG, "response: 数据加载成功");
                Utils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        //绑定数据

                        //给viewpager设置数据
                        mPagerAdapter = new ZhihuViewPagerAdapter();
                        ViewGroup.LayoutParams params = new ViewPager.LayoutParams();
                        params.height = Utils.dp2px(getContext(), 200);
                        params.width = ViewPager.LayoutParams.MATCH_PARENT;
                        mZhihuTop.setLayoutParams(params);
                        mZhihuTop_ViewPager.setAdapter(mPagerAdapter);
                        mZhihuTop_Indicator.setViewPager(mZhihuTop_ViewPager);

                        mZhihuTop_ViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                mZhihuTop_Title.setText(mData.top_stories.get(position).title);
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                mRootView.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
                            }
                        });
                        mZhihuTop_Title.setText(mData.top_stories.get(0).title);

                        mZhihuGridview.addHeaderView(mZhihuTop);


                        //给gridview设置数据
                        mGridAdapter = new ZhihuGridViewAdapter();
                        mZhihuGridview.setAdapter(mGridAdapter);

                        mZhihuGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), ZhihuDetailActivity.class);
                                intent.putExtra("ArticleID", mData.stories.get(position - 1).id);
                                startActivity(intent);
                            }
                        });

                        //当数据加载完成后,给listview设置监听
                        //只有当listview在最顶端是才能通过下拉来刷新数据
                        mZhihuGridview.setOnScrollListener(new AbsListView.OnScrollListener() {

                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {
                            }

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem,
                                                 int visibleItemCount, int totalItemCount) {
                                boolean enable = false;
                                if (mZhihuGridview != null && mZhihuGridview.getChildCount() > 0) {
                                    // check if the first item of the list is visible
                                    boolean firstItemVisible = mZhihuGridview.getFirstVisiblePosition() == 0;
                                    // check if the top of the first item is visible
                                    boolean topOfFirstItemVisible = mZhihuGridview.getChildAt(0).getTop() == 0;
                                    // enabling or disabling the refresh layout
                                    enable = firstItemVisible && topOfFirstItemVisible;
                                }
                                mRootView.setEnabled(enable);
                            }
                        });

                        refreshdata();

                    }
                });
            }
        });
    }

    private void refreshdata() {
        Log.d(TAG, "response: 刷新UI");
        flag =true;
        Utils.netRequest(Constants.GET_ZHIHU_NEWS_URL, new Utils.netRequestListener() {
            @Override
            public void response(String response) {
                Gson gson = new Gson();
                Log.d(TAG, "response: " + response);
                mData = gson.fromJson(response, ZhihuBean.class);
                Log.d(TAG, "response: 数据加载成功");
                Utils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mGridAdapter.notifyDataSetChanged();
                        mRootView.setRefreshing(false);
                        flag =false;
                        mZhihuLoading.setVisibility(View.GONE);
                    }
                });
            }
        });

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

            if (convertView == null || convertView instanceof ViewPager) {
                convertView = View.inflate(getContext(), R.layout.item_list_zhihu, null);
                viewHolder = new ViewHolder();
                viewHolder.zhihuListviewImg = (ImageView) convertView.findViewById(R.id.zhihu_listview_img);
                viewHolder.zhihuListviewTitle = (TextView) convertView.findViewById(R.id.zhihu_listview_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ZhihuBean.StoriesEntity storiesEntity = mData.stories.get(position);
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            int width = display.getWidth();
            Picasso.with(Utils.getContext()).load(storiesEntity.images.get(0)).resize(width / 2, width / 2).into(viewHolder.zhihuListviewImg);
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
            return view == (View) object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getContext());
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            int width = display.getWidth();
            Picasso.with(getContext()).load("" + mData.top_stories.get(position).image).resize(width, Utils.dp2px(getContext(), 200)).centerCrop().into(imageView);
            container.addView(imageView);
            return imageView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
