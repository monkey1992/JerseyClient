package com.jy.jerseyclient.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2015/12/29.
 */
public class JsonUtil {
    private final static String TAG = "JsonUtil";
    /**
     * 将对象转换为json字符串
     * @param obj   对象，可以是实体对象，List对象，Map对象等
     * @return   json字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("illegal argument");
        }

        return new Gson().toJson(obj);
    }

    /**
     * 将json字符串转换为实体对象
     * @param jsonString   json字符串
     * @param cls   实体类
     * @param <T>  泛型参数
     * @return   实体对象
     */
    public static <T> T getEntity(String jsonString, final Class<T> cls) {
        T t;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, jsonString + " 无法转换为 " + cls.getSimpleName() + " 对象");
            return null;
        }

        return t;
    }

    /**
     * 将json字符串转换为List对象
     * @param jsonString   json字符串
     * @param cls   实体类
     * @param <T>   泛型参数
     * @return   实体List对象
     */
    public static <T> List<T> getEntityList(String jsonString, final Class<T> cls) {
        List<T> list = new ArrayList<T>();

        JsonArray array = new JsonParser().parse(jsonString).getAsJsonArray();

        for (final JsonElement elem : array) {
            list.add(new Gson().fromJson(elem, cls));
        }

        return list;
    }

    /**public static <T> List<T> getEntityList1(String jsonString, final Class<T[]> cls) {
        T[] list;
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return Arrays.asList(list);
    }*/

    /**
     * 泛型被擦除
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    /**public static <T> ArrayList<T> getList(String jsonString, final Class<T> cls) {
        ArrayList<T> list;
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, new TypeToken<ArrayList<T>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }*/

    /**
     * 泛型被擦除
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    /**public static <T> HashMap<String, T> getMap(String jsonString, Class<T> cls) {
        HashMap<String, T> map = new HashMap<String, T>();
        try {
            Gson gson = new Gson();
            map = gson.fromJson(jsonString, new TypeToken<HashMap<String, T>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return map;
    }*/

    /**public static <T> HashMap<String, T> getMap1(String jsonString, final Class<T> cls) {
        HashMap<String, T> map = new HashMap<String, T>();

        JsonArray array = new JsonParser().parse(jsonString).getAsJsonArray();
        for (final JsonElement elem : array) {
            map.put("", new Gson().fromJson(elem, cls));
        }

        return map;
    }*/

    /**public static List<Map<String, Object>> getListMap(String jsonString) {
        List<Map<String, Object>> list;
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, new TypeToken<List<Map<String, Object>>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }*/
}
