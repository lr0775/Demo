package cc.stbl.demo.task;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import cc.stbl.demo.constant.API;
import cc.stbl.demo.util.OkHttpHelper;
import cc.stbl.demo.weapon.HttpResponse;
import cc.stbl.demo.weapon.Task;

/**
 * Created by Administrator on 2016/11/14.
 */

public class LoginTask {

    public static Task<String> login(final String email, final String password) {
        return new Task<String>() {
            @Override
            protected void call() {
                JSONObject json = new JSONObject();
                json.put("email", email);
                json.put("password", password);
                try {
                    HttpResponse response = OkHttpHelper.getInstance().post(API.LOGIN, json);
                    if (response.error != null) {
                        onError(response.error);
                        return;
                    }
                    onSuccess(response.result);
                } catch (IOException e) {
                    e.printStackTrace();
                    onError(e);
                }
            }
        };
    }

}
