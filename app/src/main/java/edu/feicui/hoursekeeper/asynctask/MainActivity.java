package edu.feicui.hoursekeeper.asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 异步任务类
 */
public class MainActivity extends AppCompatActivity {

//    RecyclerView
//    RecyclerView.Adapter
//    RecyclerView.ViewHolder
//    FloatingActionButton
//    TextInputLayout
//    TextInputEditText
//    AppBarLayout

    HttpURLConnection httpURLConnection;
    InputStream in = null;
    Button mBtnExe;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mBtnExe = (Button) findViewById(R.id.btn_main);
        mBtnExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行异步任务
                AsyncTask<String, Integer, List> task = new MyAsync();
                task.execute("加载用户软件");
            }
        });
        /**
         * AsyncTask    抽象类
         *   自带线程池
         *  泛型
         *     Params ------>执行任务所需要的附加参数
         *     Progress ------->进度的类型   用于进度的更新
         *     Result ------->任务处理的 数据 结果的类型
         *   方法 ：
         *      1.onPreExecute  ---->主线程   在任务执行之前  可以做一些初始化工作  比如：弹出一个加载框
         *      2.doInBackground ----->子线程  在onPreExecute之后执行  主要用来加载数据的一些耗时操作
         *          publishProgress---->通知更新进度  在此方法调用之后自动执行 onProgressUpdate  此方法可以执行多次
         *      3.onProgressUpdate---->主线程  在publishProgress方法调用之后自动执行 用来更新进度
         *      4.onPostExecute----->z主线程
         *      5.execute  开启任务
         * 1.子类去继承   实现抽象方法
         *      没有写泛型 ---默认的  擦除(Object)
         *      写泛型---->所需附加参数类型   进度类型   结果类型
         * 2.onPreExecute-->doInBackground(子线程加载数据)(publishProgress-->onProgressUpdate）--->onPostExecute
         * 3.开启任务  execute  需要异步任务对象
         *
         * Handler(单个)   和    AsyncTask(多个)
         * Handler  加载一个数据   sendXXX   线程     HandlerMessage
         * AsyncTask加载一个数据   doInBackground
         *
         * 注意事项：
         *   Handler不能直接在子线程中直接初始化
         *   1.异步任务也必须在主线程中开启任务  execute 必须在主线程中调用
         *   2.一个对象只能完成一个任务
         *   3.异步任务类的实例必须在UI 线程中创建
         *   4.不要手动调用那几个方法
         */
    }
    public class MyAsync extends AsyncTask<String, Integer, List> {

        /**
         * 主线程
         * 在任务执行之前  可以做一些初始化工作  比如：
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "", "加载中");
            dialog.show();
            Log.e("aaa", "-----onPreExecute" + Thread.currentThread().getName());
        }
        /**
         * 子线程
         *
         * @param params
         * @return
         */
        @Override
        protected List doInBackground(String... params) {
            Log.e("aaa", "-----doInBackground" + Thread.currentThread().getName() + "----params" + params);

            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                if(i==100){
//                    cancel(false);//参数：是在运行的时候是否取消任务  true：取消 false：不取消
//                }
                publishProgress(33);
            }
            /**
             * 请求网络数据(百度一下)  get
             * 注意：
             */
            try {
                //1.拿到  HttpURLConnection
                URL url = new URL("https://www.baidu.com/");//拿到URL对象
                httpURLConnection = (HttpURLConnection) url.openConnection();
                //2.配置一些信息
//            setDoOutPut(true);//可以服务端传递消息
//            httpURLConnection.getDoInput(true);//可以读取服务端返回的  可以省略
                httpURLConnection.setReadTimeout(5000);//
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
                        in.close();//关闭输入流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;//加载的结果  是onPostExecute方法的List o
        }

        /**
         * 主线程   更新进度
         * 在publishProgress  之后执行
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {//参数是publishProgress中的参数
            super.onProgressUpdate(values);
            Log.e("aaa", "-----onProgressUpdate" + Thread.currentThread().getName());
            Log.e("aaa", "onProgressUpdate" + values[0]);
        }
        /**
         * 拿到任务处理的结果
         *
         * @param o
         */
        @Override
        protected void onPostExecute(List o) { //o是doInBackground方法的return结果
            super.onPostExecute(o);
            Log.e("aaa", "onPostExecute" + Thread.currentThread().getName() + "-----o" + o);
            dialog.dismiss();
            //回调
        }
    }
}
