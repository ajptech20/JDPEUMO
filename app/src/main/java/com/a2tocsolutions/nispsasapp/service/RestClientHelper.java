package com.a2tocsolutions.nispsasapp.service;

import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class RestClientHelper {
    private static final String LOG_TAG = "RestClientHelper";
    public static String defaultBaseUrl = "";
    private static final Object lockObject = new Object();
    private static RestClientHelper restClientHelper;
    private final Executor executor;
    private final Handler handler = new Handler(Looper.getMainLooper());

    /* renamed from: com.nscdc.pcas.RestClientHelper$1 */
    class C02091 implements ThreadFactory {
        C02091() {
        }

        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "RestClientHelperThread");
        }
    }

    public interface RestClientListener {
        void onError(String str);

        void onSuccess(String str) throws JSONException;
    }

    private RestClientHelper() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 5, TimeUnit.SECONDS, new LinkedBlockingQueue(), new C02091());
        executor.allowCoreThreadTimeOut(true);
        this.executor = executor;
    }

    public static RestClientHelper getInstance() {
        if (restClientHelper == null) {
            synchronized (lockObject) {
                if (restClientHelper == null) {
                    restClientHelper = new RestClientHelper();
                }
            }
        }
        return restClientHelper;
    }

    private void addHeaders(Builder builder, @NonNull ArrayMap<String, String> headers) {
        for (String key : headers.keySet()) {
            builder.addHeader(key, (String) headers.get(key));
        }
    }

    public void get(@NonNull String serviceUrl, @NonNull RestClientListener restClientListener) {
        get(serviceUrl, null, null, restClientListener);
    }

    public void get(@NonNull String serviceUrl, ArrayMap<String, Object> params, @NonNull RestClientListener restClientListener) {
        get(serviceUrl, null, params, restClientListener);
    }

    public void get(@NonNull String serviceUrl, ArrayMap<String, String> headers, ArrayMap<String, Object> params, RestClientListener restClientListener) {
        Builder builder = new Builder();
        if (headers != null) {
            addHeaders(builder, headers);
        }
        builder.url(generateUrlParams(serviceUrl, params));
        execute(builder, restClientListener);
    }

    public void post(@NonNull String serviceUrl, @NonNull ArrayMap<String, Object> params, @NonNull RestClientListener restClientListener) {
        post(serviceUrl, null, params, restClientListener);
    }

    public void post(@NonNull String serviceUrl, ArrayMap<String, String> headers, @NonNull ArrayMap<String, Object> params, @NonNull RestClientListener restClientListener) {
        Builder builder = new Builder();
        if (headers != null) {
            addHeaders(builder, headers);
        }
        StringBuffer urls = new StringBuffer();
        if (defaultBaseUrl.length() > 0) {
            urls.append(defaultBaseUrl);
        }
        urls.append(serviceUrl);
        builder.url(urls.toString());
        builder.post(generateRequestBody(params));
        execute(builder, restClientListener);
    }

    public void put(@NonNull String serviceUrl, @NonNull ArrayMap<String, Object> params, @NonNull RestClientListener restClientListener) {
        put(serviceUrl, null, params, restClientListener);
    }

    public void put(@NonNull String serviceUrl, ArrayMap<String, String> headers, @NonNull ArrayMap<String, Object> params, @NonNull RestClientListener restClientListener) {
        Builder builder = new Builder();
        if (headers != null) {
            addHeaders(builder, headers);
        }
        StringBuffer urls = new StringBuffer();
        if (defaultBaseUrl.length() > 0) {
            urls.append(defaultBaseUrl);
        }
        urls.append(serviceUrl);
        builder.url(urls.toString());
        builder.put(generateRequestBody(params));
        execute(builder, restClientListener);
    }

    public void delete(@NonNull String serviceUrl, @NonNull ArrayMap<String, Object> params, @NonNull RestClientListener restClientListener) {
        delete(serviceUrl, null, params, restClientListener);
    }

    public void delete(@NonNull String serviceUrl, ArrayMap<String, String> headers, @NonNull ArrayMap<String, Object> params, @NonNull RestClientListener restClientListener) {
        Builder builder = new Builder();
        if (headers != null) {
            addHeaders(builder, headers);
        }
        StringBuffer urls = new StringBuffer();
        if (defaultBaseUrl.length() > 0) {
            urls.append(defaultBaseUrl);
        }
        urls.append(serviceUrl);
        builder.url(urls.toString());
        builder.delete(generateRequestBody(params));
        execute(builder, restClientListener);
    }

    public void postMultipart(@NonNull String serviceUrl, @NonNull ArrayMap<String, File> files, @NonNull RestClientListener restClientListener) {
        postMultipart(serviceUrl, null, null, files, restClientListener);
    }

    public void postMultipart(@NonNull String serviceUrl, ArrayMap<String, Object> params, @NonNull ArrayMap<String, File> files, @NonNull RestClientListener restClientListener) {
        postMultipart(serviceUrl, null, params, files, restClientListener);
    }

    public void postMultipart(@NonNull String serviceUrl, ArrayMap<String, String> headers, ArrayMap<String, Object> params, @NonNull ArrayMap<String, File> files, @NonNull RestClientListener restClientListener) {
        Builder builder = new Builder();
        if (headers != null) {
            addHeaders(builder, headers);
        }
        StringBuffer urls = new StringBuffer();
        if (defaultBaseUrl.length() > 0) {
            urls.append(defaultBaseUrl);
        }
        urls.append(serviceUrl);
        builder.url(urls.toString());
        builder.post(generateMultipartBody(params, files));
        execute(builder, restClientListener);
    }

    private void execute(final Builder builder, final RestClientListener restClientListener) {
        this.executor.execute(new Runnable() {

            /* renamed from: com.nscdc.pcas.RestClientHelper$2$2 */
            class C02112 implements Runnable {
                C02112() {
                }

                public void run() {
                    restClientListener.onError("APIs not working...");
                }
            }

            public void run() {
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).writeTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).build();
                try {
                    System.setProperty("http.keepAlive", "false");
                    final Response response = client.newCall(builder.build()).execute();
                    final String responseData = response.body().string();
                    RestClientHelper.this.handler.post(new Runnable() {
                        public void run() {
                            if (response.code() == 200) {
                                try {
                                    restClientListener.onSuccess(responseData);
                                    return;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                            restClientListener.onError(responseData);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    RestClientHelper.this.handler.post(new C02112());
                }
            }
        });
    }

    private String generateUrlParams(String serviceUrl, ArrayMap<String, Object> params) {
        StringBuffer urls = new StringBuffer();
        if (defaultBaseUrl.length() > 0) {
            urls.append(defaultBaseUrl);
        }
        urls.append(serviceUrl);
        if (params != null) {
            int i = 0;
            for (String key : params.keySet()) {
                if (i == 0) {
                    urls.append("?" + key + "=" + params.get(key));
                } else {
                    urls.append("&" + key + "=" + params.get(key));
                }
                i++;
            }
        }
        return urls.toString();
    }

    private RequestBody generateRequestBody(ArrayMap<String, Object> params) {
        JSONObject jsonObj = new JSONObject();
        if (params != null) {
            for (String key : params.keySet()) {
                try {
                    jsonObj.put(key, params.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObj));
    }

    private RequestBody generateMultipartBody(ArrayMap<String, Object> params, ArrayMap<String, File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, String.valueOf(params.get(key)));
            }
        }
        if (files != null) {
            for (String key2 : files.keySet()) {
                builder.addFormDataPart(key2, key2, RequestBody.create(MediaType.parse("image/png"), (File) files.get(key2)));
            }
        }
        return builder.build();
    }
}
