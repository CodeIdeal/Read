package tk.cabana.read.custom;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by k on 2016/3/7.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
