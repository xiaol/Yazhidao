package com.news.yazhidao.net;


import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengjigang on 15/1/5.
 */
public class HttpClientUtil {
    private static HttpClient customerHttpClient;

    public static HttpResponse execute(NetworkRequest request) throws MyAppException {
        switch (request.method) {
            case GET:
                return get(request);
            case POST:
                return post(request);
            case PUT:

                break;
            case DELETE:

                break;
            default:
                throw new IllegalArgumentException("The request method " + request.method.name() + " can't be supported ");
        }
        return null;
    }

    private static HttpResponse post(NetworkRequest request) throws MyAppException {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(request.url);
            addRequestHeaders(post, request.headers);
            if (request.params == null) {
                throw new IllegalArgumentException("You should set post parameters when use POST to request ");
            }
            return client.execute(post);
        } catch (ConnectTimeoutException e) {
            throw new MyAppException(MyAppException.ExceptionStatus.TimeOutException, e.getMessage());
        } catch (IOException e) {
            throw new MyAppException(MyAppException.ExceptionStatus.ServerException, e.getMessage());
        }
    }


    private static HttpResponse get(NetworkRequest request) throws MyAppException {
        try {
            HttpClient client = getHttpClient();
            HttpGet get = new HttpGet(request.url);
            addRequestHeaders(get, request.headers);
            return client.execute(get);
        } catch (ConnectTimeoutException e) {
            throw new MyAppException(MyAppException.ExceptionStatus.TimeOutException, e.getMessage());
        } catch (IOException e) {
            throw new MyAppException(MyAppException.ExceptionStatus.ServerException, e.getMessage());
        }
    }

    private static void addRequestHeaders(HttpUriRequest request, HashMap<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> set : headers.entrySet()) {
                request.setHeader(set.getKey(), set.getValue());
            }
        }
    }

    public static synchronized HttpClient getHttpClient() {
        if (null == customerHttpClient) {
            HttpParams params = new BasicHttpParams();
            // 设置一些基本参数
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params,
                    "utf-8");
            HttpProtocolParams.setUseExpectContinue(params, true);
            HttpProtocolParams
                    .setUserAgent(
                            params,
                            "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                                    + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
            // 超时设置
/* 从连接池中取连接的超时时间 */
            ConnManagerParams.setTimeout(params, 10000);
            /* 连接超时 */
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            /* 请求超时 */
            HttpConnectionParams.setSoTimeout(params, 10000);

            // 设置我们的HttpClient支持HTTP和HTTPS两种模式
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory
                    .getSocketFactory(), 443));

            // 使用线程安全的连接管理来创建HttpClient
            ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                    params, schReg);
            customerHttpClient = new DefaultHttpClient(conMgr, params);
        }
        return customerHttpClient;
    }
}
