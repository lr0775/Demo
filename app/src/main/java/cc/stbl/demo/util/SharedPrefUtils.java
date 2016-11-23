package cc.stbl.demo.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import cc.stbl.demo.App;
import cc.stbl.demo.constant.KEY;

/**
 * SharedPreferences的工具类
 */
public class SharedPrefUtils {

    private static final String PUBLIC_FILE = "public";

    public static void putToPublicFile(String key, Object object) {
        put(PUBLIC_FILE, key, object);
    }

    public static Object getFromPublicFile(String key, Object defaultObject) {
        return get(PUBLIC_FILE, key, defaultObject);
    }

    public static void putToUserFile(String key, Object object) {
        put(getUserFileName(), key, object);
    }

    public static Object getFromUserFile(String key, Object defaultObject) {
        return get(getUserFileName(), key, defaultObject);
    }

    private static String getUserFileName() {
        return "user_" + getFromPublicFile(KEY.LOGINED_UID, 0);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    private static void put(String fileName, String key, Object object) {
        if (object == null) {
            return;
        }
        final Context context = App.getContext();
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    private static Object get(String fileName, String key, Object defaultObject) {
        if (defaultObject == null) {
            throw new NullPointerException("需要指定默认值");
        }
        final Context context = App.getContext();
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     */
    private static void remove(String fileName, String key) {
        final Context context = App.getContext();
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    private static void clear(String fileName) {
        final Context context = App.getContext();
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否已经存在
     */
    private static boolean contains(String fileName, String key) {
        final Context context = App.getContext();
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    private static Map<String, ?> getAll(String fileName) {
        final Context context = App.getContext();
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

}
