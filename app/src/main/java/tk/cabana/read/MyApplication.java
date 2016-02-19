package tk.cabana.read;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by   KY on 2016/1/28.
 * 项目名:      Read
 * 包名:       tk.cabana.read
 * 类名:	      MyApplication
 * 创建者:	   康阳
 * 创建时间:   2016/1/28	18:05
 * 描述:	TODO
 * <p/>
 * svn版本:   $$Rev$
 * 更新人:     $$Author$
 * 更新时间:    $$Date$
 * 更新描述:    TODO
 */
public class MyApplication extends Application{
    static Handler mHandler = new Handler();
    static Context mContext;
    @Override
    public void onCreate() {
        mContext = getApplicationContext();
        super.onCreate();
    }

    public static Context getContext(){
        return mContext;
    }

    public static Handler getHandler(){
        return mHandler;
    }
}
