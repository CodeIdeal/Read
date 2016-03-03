package tk.cabana.read.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tk.cabana.read.Constants;
import tk.cabana.read.R;
import tk.cabana.read.Utils;
import tk.cabana.read.activity.CnbetaDetailActivity;
import tk.cabana.read.bean.CnbetaBean;

/**
 * Created by   KY on 2016/1/28.
 * 项目名:      Read
 * 包名:       tk.cabana.read.fragment
 * 类名:	      CnbetaFragment
 * 创建者:	   康阳
 * 创建时间:   2016/1/28	18:03
 * 描述:	TODO
 * <p/>
 * svn版本:   $$Rev$
 * 更新人:     $$Author$
 * 更新时间:    $$Date$
 * 更新描述:    TODO
 */
public class CnbetaFragment extends Fragment {

    private static final String TAG = "CnbetaFragment";
    private ListView mCnbetaListview;
    private RelativeLayout mLoaing;
    private SwipeRefreshLayout mRootView;


    private List<CnbetaBean> mDatas;
    private CnbetaAdapter mAdapter;
    private boolean flag = false;
    private Context mContext;

    private boolean SwipeRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = (SwipeRefreshLayout) View.inflate(Utils.getContext(), R.layout.fragment_cnbeta, null);

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

        mLoaing = (RelativeLayout) mRootView.findViewById(R.id.loaing);
        mCnbetaListview = (ListView) mRootView.findViewById(R.id.Cnbeta_listview);

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Utils.netRequest(Constants.GET_CNBETA_NEWS_URL, new Utils.netRequestListener() {
            @Override
            public void response(String response) {
                Gson gson = new Gson();
                mDatas = gson.fromJson(response, new TypeToken<List<CnbetaBean>>() {
                }.getType());
                Utils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        //绑定数据
                        Log.d(TAG, "run: 加载数据到listview");
                        mAdapter = new CnbetaAdapter();
                        mCnbetaListview.setAdapter(mAdapter);

                        Log.d(TAG, "run: 显示内容切换");
                        mLoaing.setVisibility(View.GONE);
                        mCnbetaListview.setVisibility(View.VISIBLE);

                        //设置listvew的item点击事件
                        mCnbetaListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), CnbetaDetailActivity.class);
                                intent.putExtra("ArticleID", mDatas.get(position).article_id);
                                startActivity(intent);
                            }
                        });

                        //TODO:性能优化
                        //当数据加载完成后,给listview设置监听
                        //只有当listview在最顶端是才能通过下拉来刷新数据
                        mCnbetaListview.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {

                            }

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                boolean enable = false;
                                if (mCnbetaListview != null && mCnbetaListview.getChildCount() > 0) {
                                    // check if the first item of the list is visible
                                    boolean firstItemVisible = mCnbetaListview.getFirstVisiblePosition() == 0;
                                    // check if the top of the first item is visible
                                    boolean topOfFirstItemVisible = mCnbetaListview.getChildAt(0).getTop() == 0;
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

            @Override
            public void erro() {
                mRootView.setRefreshing(false);
            }
        });


        super.onViewCreated(view, savedInstanceState);
    }

    public void refreshdata() {

        Utils.netRequest(Constants.GET_CNBETA_NEWS_URL, new Utils.netRequestListener() {
            @Override
            public void response(String response) {
                Gson gson = new Gson();
                mDatas = gson.fromJson(response, new TypeToken<List<CnbetaBean>>() {
                }.getType());
                Utils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("kaka", "run: 刷新内容");
                        Log.d(TAG, "run: " + mDatas.toString());
                        if (mDatas != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                        mRootView.setRefreshing(false);
                        flag = false;
                    }
                });
            }

            @Override
            public void erro() {
                mLoaing.setVisibility(View.GONE);
                mRootView.setRefreshing(false);
            }


        });

    }

    private class CnbetaAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            if (mDatas != null) {
                return mDatas.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {

            if (mDatas != null) {
                return mDatas.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(Utils.getContext(), R.layout.item_cnbeta, null);
                convertView.setTag(viewHolder);
                viewHolder.mCnbetaIv = (ImageView) convertView.findViewById(R.id.cnbeta_iv);
                viewHolder.mCnbetaTitle = (TextView) convertView.findViewById(R.id.cnbeta_title);
                viewHolder.mCnbetaIntro = (TextView) convertView.findViewById(R.id.cnbeta_intro);
                viewHolder.mCnbetaScan = (TextView) convertView.findViewById(R.id.cnbeta_scan);
                viewHolder.mComment = (TextView) convertView.findViewById(R.id.comment);
                viewHolder.mCnbetaTime = (TextView) convertView.findViewById(R.id.cnbeta_time);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            CnbetaBean cnbetaBean = mDatas.get(position);

            Picasso.with(Utils.getContext()).load(cnbetaBean.topic).into(viewHolder.mCnbetaIv);
            viewHolder.mCnbetaTitle.setText(cnbetaBean.title);
            viewHolder.mCnbetaIntro.setText(cnbetaBean.intro);
            viewHolder.mCnbetaScan.setText(cnbetaBean.view_num + "");
            viewHolder.mComment.setText(cnbetaBean.comment_num + "");
            viewHolder.mCnbetaTime.setText(cnbetaBean.date.subSequence(11, 18));

            return convertView;
        }
    }

    class ViewHolder {

        ImageView mCnbetaIv;
        TextView mCnbetaTitle;
        TextView mCnbetaIntro;
        TextView mCnbetaScan;
        TextView mComment;
        TextView mCnbetaTime;
    }
}
