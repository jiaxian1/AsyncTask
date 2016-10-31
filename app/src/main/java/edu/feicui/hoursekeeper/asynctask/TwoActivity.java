package edu.feicui.hoursekeeper.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * 网络编程
 * Created by jiaXian on 2016/9/19.
 */
public class TwoActivity extends AppCompatActivity {

    ImageView mIv;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mIv=new ImageView(this);
        setContentView(mIv);
//        setContentView(R.layout.main_activity);
        new Thread() {
            @Override
            public void run() {
                super.run();
//                get();
//                post();
//                getClick();
                postClick();

            }
        }.start();
    }

    /**
     *
     */
    public void get() {
        HttpURLConnection httpURLConnection = null;
        InputStream in = null;

        try {

            //1.拿到  HttpURLConnection
            URL url = new URL("http://192.168.199.239:8080/TestWeb/servlet/LoginServer?name=zs&pwd=123");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //2.配置一些信息
//            setDoOutPut(true);//可以服务端传递消息
//            httpURLConnection.getDoInput(true);//可以读取服务端返回的
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            //网络请求结果码
            // 404 一般是客户端的问题
            // 500 一般是服务端的问题
            //200  表示网络请求成功  httpURLConnection.HTTP_OK
            int code = httpURLConnection.getResponseCode();
            Log.e("aaa", "-------code=" + code);
            if (code == httpURLConnection.HTTP_OK) {
                //读数据
                in = httpURLConnection.getInputStream();
                byte[] bytes = new byte[1024];
                int len;
                StringBuffer buffer = new StringBuffer();
                while ((len = in.read(bytes)) != -1) {
                    buffer.append(new String(bytes, 0, len));//将字节数组转换成buffer
                }
                Log.e("aaa", "----buffer=" + buffer.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //网络连接最后需要断开连接
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * post
     */
    public void post() throws IOException {
        URL url=new URL("http://192.168.199.239:8080/TestWeb/servlet/LoginServer");
        HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
        //2.配置一些信息

        httpURLConnection.setReadTimeout(5000);//
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setDoOutput(true);//可以向服务端写
        httpURLConnection.setRequestMethod("POST");//get是默认的  可以不用写
        //写数据
//        OutputSteram out=httpURLConnection.getOutputSteram();
//        out.write("name=ls&pwd=123".getBytes);
        //读数据
        int code=httpURLConnection.getResponseCode();
        Log.e("aaa","-------code="+code);
        InputStream in=null;
        if (code==httpURLConnection.HTTP_OK){
            //读数据
            in=httpURLConnection.getInputStream();
            byte[] bytes=new byte[1024];
            int len;
            StringBuffer buffer=new StringBuffer();
            while((len=in.read(bytes))!=-1){
                buffer.append(new String(bytes,0,len));//将字节数组转换成buffer
            }
            Log.e("aaa","----buffer="+buffer.toString());
        }
//        if(out!=null){
//            out.close();
//        }
        if(in!=null){
            in.close();
        }
    }

//        HttpURLConnection

    /**
     * Android中网络编程
     * 连接网络
     * HttpConnection    即将面临淘汰
     * Google进行封装的网络请求   响应稍慢  不好扩展
     * <p/>
     * HttpURLConnection  java.net  推荐使用
     * java源生支持的 好扩展
     * <p/>
     * URL  统一资源标识符
     * 用来封装连接
     * <p/>
     * 协议：
     * 比如：http
     * 主机名：
     * 127.0.0.1  localhost
     * 端口号：
     * 8080
     * 用户信息：
     * 比如：用户名、密码
     * <p/>
     * HttpURLConnection的使用
     * 1.获取HttpURLConnection的对象
     * URL url=new URL("");
     * URL.openConnection
     * 2.配置一些信息
     */
    public void getClick() {
        /**
         * HttpClick的get请求
         *     jar包   需要展开  添加到工程
         *
         *  HttpClick   接口
         *  1.拿到HttpClick的对象
         *       实现类------->DefaultHttpClient
         *  2.拿到HttpUriResquest  请求方式  接口
         *       A：HttpGet
         *            new HttpGet(String uri)
         *       B：HttpPost
         *
         */
        HttpURLConnection httpURLConnection = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpUriRequest httpUriRequest = new HttpGet("http://www.baidu.com/img/bd_logo1.png");

        try {
            //执行这个链接
            HttpResponse httpResponse = httpClient.execute(httpUriRequest);
            //拿到响应的结果 HttpResponse
            int code = httpResponse.getStatusLine().getStatusCode();
            Log.e("aaa", "getClick:---code==" + code);
            if (code == httpURLConnection.HTTP_OK) {
                ///实际的结果
                HttpEntity entity=httpResponse.getEntity();
                //1.。。。。
//                String str=EntityUtils.toString(entity);
//                Log.e("aaa", "postClick:str== "+str );
                //2.流。。。。
                InputStream in=entity.getContent();
                Bitmap bitmap=BitmapFactory.decodeStream(in);
                /**
                 * 1.Message
                 */
                Message msg = Message.obtain();
                msg.obj = bitmap;
                handler.sendMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void postClick() {
        HttpURLConnection httpURLConnection = null;
        //拿到HttpClient的对象
        HttpClient httpClient = new DefaultHttpClient();
        //拿到请求方式
        HttpPost request = new HttpPost("http://192.168.199.239:8080/TestWeb/abc");
        //            List<? extends NameValuePair> parameters
        try {
            ArrayList<NameValuePair> list = new ArrayList<>();
            NameValuePair pair1 = new BasicNameValuePair("name", "zs");
            NameValuePair pair2 = new BasicNameValuePair("pwd", "123456");
            list.add(pair1);
            list.add(pair2);
            HttpEntity entity = new UrlEncodedFormEntity(list, "utf_8");
            request.setEntity(entity);
            HttpResponse response = httpClient.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if (code == httpURLConnection.HTTP_OK) {
                Log.e("aaa", "postClick: code==" + code);
                ///实际的结果
                HttpEntity entity1 = response.getEntity();
                String str = EntityUtils.toString(entity);
                Log.e("aaa", "postClick:str== " + str);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mIv.setImageBitmap((Bitmap) msg.obj);
            }
        };

}
