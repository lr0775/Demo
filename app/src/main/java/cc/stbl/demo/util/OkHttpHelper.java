package cc.stbl.demo.util;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import cc.stbl.demo.constant.API;
import cc.stbl.demo.constant.KEY;
import cc.stbl.demo.model.ServerResult;
import cc.stbl.demo.weapon.HttpResponse;
import cc.stbl.demo.weapon.TaskError;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpHelper {

    public static final MediaType TEXT = MediaType.parse("text/html; charset=utf-8");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType IMAGE = MediaType.parse("image/*");
    public static final MediaType STREAM = MediaType.parse("application/octet-stream");
    private static final String TAG = "OkHttpHelper";
    private static volatile OkHttpHelper sInstance;
    private OkHttpClient mClient;

    private OkHttpHelper() {
        mClient = new OkHttpClient();
    }

    public static OkHttpHelper getInstance() {
        if (sInstance == null) {
            synchronized (OkHttpHelper.class) {
                if (sInstance == null) {
                    sInstance = new OkHttpHelper();
                }
            }
        }
        return sInstance;
    }

    public HttpResponse get(String method) throws IOException {
        if (!NetUtils.isNetworkAvailable()) {
            return new HttpResponse(new TaskError("网络不可用"), "");
        }
        String url = API.HOST + method;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("x-access-token", (String) SharedPrefUtils.getFromPublicFile(KEY.ACCESS_TOKEN, ""))
                .addHeader("User-Agent", "Android;" + (int) SharedPrefUtils.getFromPublicFile(KEY.LOGINED_UID, 0))
                .build();
        Response response = mClient.newCall(request).execute();
        String res = response.body().string();
        Logger.e("url = " + url + ", response = " + res);
        ServerResult serverResult = com.alibaba.fastjson.JSON.parseObject(res, ServerResult.class);
        TaskError error = null;
        if (serverResult.code != 0) {
            error = new TaskError(serverResult.code, serverResult.msg);
        }
        String result = "";
        if (error == null) {
            result = serverResult.data;
        }
        return new HttpResponse(error, result);
    }

    public Response getResponse(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return mClient.newCall(request).execute();
    }

    public HttpResponse post(String method, JSONObject o) throws IOException {
        if (!NetUtils.isNetworkAvailable()) {
            return new HttpResponse(new TaskError("网络不可用"), "");
        }
        String url = API.HOST + method;
        FormBody.Builder builder = new FormBody.Builder();
        Set<String> keySet = o.keySet();
        for (String key : keySet) {
            builder.add(key, o.get(key).toString());
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("x-access-token", (String) SharedPrefUtils.getFromPublicFile(KEY.ACCESS_TOKEN, ""))
                .addHeader("User-Agent", "Android;" + (int) SharedPrefUtils.getFromPublicFile(KEY.LOGINED_UID, 0))
                .post(body)
                .build();
        Response response = mClient.newCall(request).execute();
        String res = response.body().string();
        Logger.e("url = " + url + ", response = " + res);

        ServerResult serverResult = com.alibaba.fastjson.JSON.parseObject(res, ServerResult.class);
        TaskError error = null;
        if (serverResult.code != 0) {
            error = new TaskError(serverResult.code, serverResult.msg);
        }
        String result = "";
        if (error == null) {
            result = serverResult.data;
        }
        return new HttpResponse(error, result);
    }

    public HttpResponse uploadImage(String method, File file) throws IOException {
        if (!NetUtils.isNetworkAvailable()) {
            return new HttpResponse(new TaskError("网络不可用"), "");
        }
        String url = API.HOST + method;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("x-access-token", (String) SharedPrefUtils.getFromPublicFile(KEY.ACCESS_TOKEN, ""))
                .addHeader("User-Agent", "Android;" + (int) SharedPrefUtils.getFromPublicFile(KEY.LOGINED_UID, 0))
                .post(RequestBody.create(IMAGE, file))
                .build();
        Response response = mClient.newCall(request).execute();
        String res = response.body().string();
        Logger.e("url = " + url + ", response = " + res);

        ServerResult serverResult = com.alibaba.fastjson.JSON.parseObject(res, ServerResult.class);
        TaskError error = null;
        if (serverResult.code != 0) {
            error = new TaskError(serverResult.code, serverResult.msg);
        }
        String result = "";
        if (error == null) {
            result = serverResult.data;
        }
        return new HttpResponse(error, result);
    }

}
