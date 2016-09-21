package com.jy.jerseyclient.service;

import android.text.TextUtils;
import android.util.Log;

import com.jy.jerseyclient.entity.Response;
import com.jy.jerseyclient.utils.JsonUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xy on 2016/4/10.
 */
public class WebService {
    private final static String TAG = "WebService";
    private static final String WEB_ROOT = "http://192.168.254.26:8080/JerseyServer/rest/restService/";
    private static final String LOGIN = "login";
    private static final String GET_ALL_USERS = "getAllUsers";

    public static Response login(String username, String password) {
        String path = WEB_ROOT + LOGIN;
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        InputStream is = connection(path, map);
        if (is != null) {
            String content = getStringFromIS(is);
            if (content != null) {
                return parseResponse(content);
            } else {
                Log.e(TAG, "contentS == null");
            }
        } else {
            Log.e(TAG, "is == null");
        }
        return null;
    }

    public static Response getAllUsers() {
        String path = WEB_ROOT + GET_ALL_USERS;
        InputStream is = connection(path, null);
        if (is != null) {
            String content = getStringFromIS(is);
            if (content != null) {
                return parseResponse(content);
            } else {
                Log.e(TAG, "contentS == null");
            }
        } else {
            Log.e(TAG, "is == null");
        }
        return null;
    }

    private static Response parseResponse(String content) {
        Log.e(TAG, "state======" + content);
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        return JsonUtil.getEntity(content, Response.class);
    }

    /**
     * 得到服务器返回的输入流数据
     * @param path 请求路径
     * @param map 包含密文的map集合
     * @return 服务器返回的数据
     */
    private static InputStream connection(String path, Map<String, String> map) {
        try {
            String pathUrl = path;
            URL url = new URL(pathUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            StringBuffer sb = new StringBuffer();
            if (map != null) {
                if (!map.isEmpty()) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8")).append('&');
                    }
                    sb.deleteCharAt(sb.length() - 1);
                }
            }
            byte[] entityData = sb.toString().getBytes();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setRequestMethod("POST");
            //设置请求服务器连接的超时时间
            httpConn.setConnectTimeout(5 * 1000);
            //设置服务器返回数据的超时时间
            //httpConn.setReadTimeout(30 * 1000);
            httpConn.setRequestProperty("Content-length", "" + entityData.length);
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream outStream = httpConn.getOutputStream();
            outStream.write(entityData);
            outStream.flush();
            outStream.close();
            int responseCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                InputStream is = httpConn.getInputStream();
                return is;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 将服务器返回的输入流转换为字符串
     * @param is 服务器返回的输入流
     * @return 输入流转换之后的字符串
     */
    public static String getStringFromIS(InputStream is) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String reString = new String(os.toByteArray());

        Log.e(TAG, "geStringFromIS reString======" + reString);

        return reString;
    }
}
