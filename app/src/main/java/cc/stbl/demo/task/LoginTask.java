package cc.stbl.demo.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import cc.stbl.demo.constant.API;
import cc.stbl.demo.model.LoginInfo;
import cc.stbl.demo.model.Memo;
import cc.stbl.demo.util.OkHttpHelper;
import cc.stbl.demo.weapon.HttpResponse;
import cc.stbl.demo.weapon.Task;

/**
 * Created by Administrator on 2016/11/14.
 */

public class LoginTask {

    public static Task<LoginInfo> login(final String phone, final String password) {
        return new Task<LoginInfo>() {
            @Override
            protected void call() {
                JSONObject json = new JSONObject();
                json.put("phone", phone);
                json.put("password", password);
                try {
                    HttpResponse response = OkHttpHelper.getInstance().post(API.LOGIN, json);
                    if (response.error != null) {
                        onError(response.error);
                        return;
                    }
                    LoginInfo data = JSON.parseObject(response.result, LoginInfo.class);
                    if (data == null) {
                        onError("JSONError");
                        return;
                    }
                    onSuccess(data);
                } catch (IOException e) {
                    e.printStackTrace();
                    onError(e);
                }
            }
        };
    }

    public static Task<LoginInfo> register(final String phone, final String password) {
        return new Task<LoginInfo>() {
            @Override
            protected void call() {
                JSONObject json = new JSONObject();
                json.put("phone", phone);
                json.put("password", password);
                try {
                    HttpResponse response = OkHttpHelper.getInstance().post(API.REGISTER, json);
                    if (response.error != null) {
                        onError(response.error);
                        return;
                    }
                    LoginInfo data = JSON.parseObject(response.result, LoginInfo.class);
                    if (data == null) {
                        onError("JSONError");
                        return;
                    }
                    onSuccess(data);
                } catch (IOException e) {
                    e.printStackTrace();
                    onError(e);
                }
            }
        };
    }

    public static Task<Memo> createMemo(final String title, final String content) {
        return new Task<Memo>() {
            @Override
            protected void call() {
                JSONObject json = new JSONObject();
                json.put("title", title);
                json.put("content", content);
                try {
                    HttpResponse response = OkHttpHelper.getInstance().post(API.MEMO, json);
                    if (response.error != null) {
                        onError(response.error);
                        return;
                    }
                    Memo data = JSON.parseObject(response.result, Memo.class);
                    if (data == null) {
                        onError("JSONError");
                        return;
                    }
                    onSuccess(data);
                } catch (IOException e) {
                    e.printStackTrace();
                    onError(e);
                }
            }
        };
    }

}
