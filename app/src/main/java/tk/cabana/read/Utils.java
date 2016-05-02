package tk.cabana.read;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


/**
 * Created by   KY on 2016/1/28.
 * 项目名:      Read
 * 包名:       tk.cabana.read
 * 类名:	      Utils
 * 创建者:	   康阳
 * 创建时间:   2016/1/28	18:05
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
                InputStream certificate = null;
                try {
                    certificate = getContext().getAssets().open("cnbeta.cer");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setCertificates(okHttpClient,certificate);
                Request request = new Request.Builder().url(url).get().build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    listener.response(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.erro();
                            Toast.makeText(getContext(), "获取网络数据数失败", Toast.LENGTH_SHORT).show();
                        }
                    });

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

    public static String decodeUnicode(String unicodeString) {
        char aChar;
        int len = unicodeString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = unicodeString.charAt(x++);
            if (aChar == '\\') {
                aChar = unicodeString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = unicodeString.charAt(x++);
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
        void erro();
    }

    //给okhttp设置特定信任证书
    public static void setCertificates(OkHttpClient mOkHttpClient,InputStream... certificates)
    {
        try
        {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates)
            {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try
                {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e)
                {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());


        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
