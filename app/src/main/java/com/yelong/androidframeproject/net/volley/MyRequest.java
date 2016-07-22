package com.yelong.androidframeproject.net.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义请求类
 * Created by eyetech on 16/5/17.
 * mail:354734713@qq.com
 */
public abstract class MyRequest<T> extends Request {
    private final Response.Listener<T> mListener;
    private byte[] data = null;

    public MyRequest(int method, String url, Response.Listener<T> mListener,
                     Response.ErrorListener listener, HashMap<String, String> requestParams) {
        super(method, url, listener);
        setShouldCache(true);
        this.mListener = mListener;
        if (requestParams != null) {
            this.data = mapTobyteArray(requestParams);
        }
    }

    private byte[] mapTobyteArray(HashMap<String, String> requestParams) {
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            params.append(entry.getKey());
            params.append("=");
            params.append(entry.getValue());
            params.append("&");
        }

        if (params.length() > 0)
            params.deleteCharAt(params.length() - 1);
        try {
            return params.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String string = new String(response.data, "UTF-8");
            // 这里用了一个泛型的技巧，T由后面的实现确定
            T result = parseNetworkResponseDelegate(string);
            // 成功时的回调方法
            mListener.onResponse(result);
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Object response) {

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Charset", "utf-8");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Connection", "Keep-alive");
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        //System.out.println("params:" + super.getParams());
        return super.getParams();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (data == null) {
            return super.getBody();
        }
        return data;
    }

    // parseNetworkResponseDelegate解析json的接口，由子类实现
    protected abstract T parseNetworkResponseDelegate(String jsonString);
}
