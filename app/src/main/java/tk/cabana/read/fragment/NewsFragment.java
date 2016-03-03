package tk.cabana.read.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tk.cabana.read.R;

/**
 * Created by   KY on 2016/1/28.
 * 项目名:      Read
 * 包名:       tk.cabana.read.fragment
 * 类名:	      NewsFragment
 * 创建者:	   康阳
 * 创建时间:   2016/1/28	22:06
 * 描述:	TODO
 * <p/>
 * svn版本:   $$Rev$
 * 更新人:     $$Author$
 * 更新时间:    $$Date$
 * 更新描述:    TODO
 */
public class NewsFragment extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_news,null);

        return view;
    }
}
