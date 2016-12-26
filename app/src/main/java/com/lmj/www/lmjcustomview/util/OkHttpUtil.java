package com.lmj.www.lmjcustomview.util;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.lmj.www.lmjcustomview.BaseApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

import static android.R.attr.key;

/**
 * Created by aus on 2016/11/23.
 */

public class OkHttpUtil {
    //读超时长，单位：毫秒
    public static final int READ_TIME_OUT = 7676;
    //连接时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 7676;

    private static OkHttpUtil okHttpUtil;
    private static OkHttpClient okHttpClient;

    String cachedirectory = Environment.getExternalStorageDirectory() + "/lancoo/caches";
    private static final long CACHE_STALE_SEC = 60 * 1;
    private static final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String cacheControl = request.cacheControl().toString();
            if (!NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
                request = request.newBuilder()
                        .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };
    private String sessionID = "";
    private Interceptor receivedCookiesInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")) {
                    Log.i("aaa", "session:" + header);
                    sessionID = header;
                    cookies.add(header);
                }
            }

            return originalResponse;
        }
    };
    private Interceptor addCookiesInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("Cookie", sessionID);

            return chain.proceed(builder.build());
        }
    };
    private Cache cache;
    private File cacheFile;
    private List<Cookie> cookies_s = new ArrayList<Cookie>();

    private OkHttpUtil() {
        cacheFile = new File(BaseApplication.getAppContext().getCacheDir(), "cache");
        cache = new Cache(cacheFile, 1024 * 1024 * 10); //10Mb
        //增加头部信息
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(build);
            }
        };

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(addCookiesInterceptor)
                .addInterceptor(receivedCookiesInterceptor)
                .addInterceptor(mRewriteCacheControlInterceptor)
                .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .cache(cache)
                .build();
    }

    public static OkHttpUtil getInstance() {
        if (null == okHttpUtil) {
            synchronized (OkHttpUtil.class) {
                if (null == okHttpUtil) {

                    okHttpUtil = new OkHttpUtil();
                }
            }
        }
        return okHttpUtil;
    }

    public void doGet(String url) {

    }

    public Call doGetBackCall(String url, final Callback callback) {
        Request request = new Request.Builder().url(url).build();
        Callback Mycallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }

        };
        Call call = okHttpClient.newCall(request);
        call.enqueue(Mycallback);

        return call;
    }
    public Call doGetBackCall(String url,int cacheTime , final Callback callback) {
        Request request = new Request.Builder().url(url).addHeader("Cache-Control",cacheTime<1?"":"max-stale="+cacheTime).build();
        Callback Mycallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }

        };
        Call call = okHttpClient.newCall(request);
        call.enqueue(Mycallback);

        return call;
    }

    public Call doPostBackCall(HashMap<String, String> params, String url,  Callback callback) {
        FormBody.Builder formBodybuilder = new FormBody.Builder();
        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            formBodybuilder.add(key, val);
        }
        RequestBody requestBody = formBodybuilder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(callback);
        return call;
    }

    public Call doPostJsonBackCall(String jsonParam, String url, final Callback callback) {


        MultipartBody.Builder mulibodyBuilder = new MultipartBody.Builder();
        mulibodyBuilder.addPart(null, new RequestBody() {
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {

            }
        });

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonParam);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        Callback Mycallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }

        };
        call.enqueue(Mycallback);
        return call;
    }
}
