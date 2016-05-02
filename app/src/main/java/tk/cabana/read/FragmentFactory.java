package tk.cabana.read;

import android.support.v4.app.Fragment;

import tk.cabana.read.fragment.CnbetaFragment;
import tk.cabana.read.fragment.NewsFragment;
import tk.cabana.read.fragment.ZhihuFragment;

/**
 * Created by   KY on 2016/1/28.
 * 项目名:      Read
 * 包名:       tk.cabana.read
 * 类名:	      FragmentFactory
 * 创建者:	   康阳
 * 创建时间:   2016/1/28	17:58
 */
public class FragmentFactory {

    public static Fragment getFragmenty(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new CnbetaFragment();
                break;
            case 1:
                fragment = new ZhihuFragment();
                break;
            case 2:
                fragment = new NewsFragment();
                break;
        }
        return fragment;
    }
}
