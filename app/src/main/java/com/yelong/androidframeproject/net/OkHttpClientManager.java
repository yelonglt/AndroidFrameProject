package com.yelong.androidframeproject.net;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.internal.$Gson$Types;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttpClient请求统一管理类
 * Created by eyetech on 16/2/24.
 * mail:354734713@qq.com
 */
public class OkHttpClientManager {

    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private OkHttpClientManager() {
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    private static class SingletonHolder {
        private static OkHttpClientManager mInstance = new OkHttpClientManager();
    }

    public static OkHttpClientManager getInstance() {
        return SingletonHolder.mInstance;
    }

    /**
     * 初始化OkHttpClient对象
     *
     * @param builder OkHttpClient.Builder
     */
    public void init(OkHttpClient.Builder builder) {
        if (builder == null) builder = new OkHttpClient.Builder();
        mOkHttpClient = builder.connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private Response _get(String url) throws IOException {
        final Request request = new Request.Builder().url(url).build();
        return mOkHttpClient.newCall(request).execute();
    }

    private String _getAsString(String url) throws IOException {
        Response execute = _get(url);
        return execute.body().string();
    }

    private void _getAsync(String url, final ResultCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        deliveryResult(callback, request);
    }

    private Response _post(String url, Param... params) throws IOException {
        Request request = buildPostRequest(url, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private String _postAsString(String url, Param... params) throws IOException {
        Response response = _post(url, params);
        return response.body().string();
    }

    private void _postAsync(String url, final ResultCallback callback, Param... params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    private void _postAsync(String url, final ResultCallback callback, Map<String, String> params) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(callback, request);
    }

    /**
     * 同步基于post的文件上传
     */
    private Response _postFile(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _postFile(String url, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _postFile(String url, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 异步基于post的文件上传
     */
    private void _postFileAsync(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件不带参数上传
     */
    private void _postFileAsync(String url, ResultCallback callback, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     */
    private void _postFileAsync(String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步下载文件
     */
    private void _downloadAsync(final String url, final String destFileDir, final ResultCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackError(request, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    callbackSuccess(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    callbackError(response.request(), e, callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    //*************对外公布的方法************
    public static Response get(String url) throws IOException {
        return getInstance()._get(url);
    }

    public static String getAsString(String url) throws IOException {
        return getInstance()._getAsString(url);
    }

    public static void getAsync(String url, ResultCallback callback) {
        getInstance()._getAsync(url, callback);
    }

    public static Response post(String url, Param... params) throws IOException {
        return getInstance()._post(url, params);
    }

    public static String postAsString(String url, Param... params) throws IOException {
        return getInstance()._postAsString(url, params);
    }

    public static void postAsync(String url, final ResultCallback callback, Param... params) {
        getInstance()._postAsync(url, callback, params);
    }

    public static void postAsync(String url, final ResultCallback callback, Map<String, String> params) {
        getInstance()._postAsync(url, callback, params);
    }

    public static Response postFile(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        return getInstance()._postFile(url, files, fileKeys, params);
    }

    public static Response postFile(String url, File file, String fileKey) throws IOException {
        return getInstance()._postFile(url, file, fileKey);
    }

    public static Response postFile(String url, File file, String fileKey, Param... params) throws IOException {
        return getInstance()._postFile(url, file, fileKey, params);
    }

    public static void postFileAsync(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        getInstance()._postFileAsync(url, callback, files, fileKeys, params);
    }

    public static void postFileAsync(String url, ResultCallback callback, File file, String fileKey) throws IOException {
        getInstance()._postFileAsync(url, callback, file, fileKey);
    }

    public static void postFileAsync(String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException {
        getInstance()._postFileAsync(url, callback, file, fileKey, params);
    }

    public static void downloadAsync(String url, String destDir, ResultCallback callback) {
        getInstance()._downloadAsync(url, destDir, callback);
    }
    //****************************

    /**
     * Post方式提交分块请求
     */
    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, Param[] params) {
        params = validateParam(params);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMineType(fileName)), file);
                //根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private String guessMineType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    private Param[] validateParam(Param[] params) {
        if (params == null) {
            return new Param[0];
        }
        return params;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private void deliveryResult(final ResultCallback callback, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callbackError(call.request(), e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String string = response.body().string();
                    if (callback.mType == String.class) {
                        callbackSuccess(string, callback);
                    } else {
                        Object object = mGson.fromJson(string, callback.mType);
                        callbackSuccess(object, callback);
                    }
                } catch (IOException e) {
                    callbackError(response.request(), e, callback);
                } catch (JsonParseException e) {//Json解析的错误
                    callbackError(response.request(), e, callback);
                }

            }
        });
    }

    private void callbackError(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onError(request, e);
                }
            }
        });
    }

    private void callbackSuccess(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    private Request buildPostRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    public static abstract class ResultCallback<T> {
        Type mType;

        protected ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);
    }

    public static class Param {
        String key;
        String value;

        private Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }
}
