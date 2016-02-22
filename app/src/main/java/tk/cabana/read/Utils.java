package tk.cabana.read;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tk.cabana.read.bean.CnbetaBean;

/**
 * Created by   KY on 2016/1/28.
 * 项目名:      Read
 * 包名:       tk.cabana.read
 * 类名:	      Utils
 * 创建者:	   康阳
 * 创建时间:   2016/1/28	18:05
 * 描述:	TODO
 * <p/>
 * svn版本:   $$Rev$
 * 更新人:     $$Author$
 * 更新时间:    $$Date$
 * 更新描述:    TODO
 */
public class Utils {

    public static Context getContext(){
        return MyApplication.getContext();
    }

    public static void netRequest(final String url, final netRequestListener listener){
        //获取数据并绑定数据
        Utils.newThreadtask(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(url).get().build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    listener.response(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "获取网络数据数失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void newThreadtask(Runnable task){
        new Thread(task).start();
    }

    public static void runOnUIThread(Runnable task){
        MyApplication.getHandler().post(task);
    }

    public static int dp2px(Context context,int dp){
        // 换算公式:1 px = 1dp*(dpi/160)
        int px= -1;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int dpi = metrics.densityDpi;
        px = (int) (dp*(dpi/160f)+0.5f);
        return px;
    }

    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    public interface netRequestListener{
        void response(String response);
    }
}
