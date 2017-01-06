package cc.stbl.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import cc.stbl.demo.R;
import cc.stbl.demo.constant.KEY;
import cc.stbl.demo.model.LoginInfo;
import cc.stbl.demo.model.Memo;
import cc.stbl.demo.task.LoginTask;
import cc.stbl.demo.util.EncryptUtils;
import cc.stbl.demo.util.Logger;
import cc.stbl.demo.util.SharedPrefUtils;
import cc.stbl.demo.util.Toaster;
import cc.stbl.demo.weapon.TaskCallback;
import cc.stbl.demo.weapon.TaskError;

public class NoteActivity extends BaseActivity {

    private EditText mPhoneEt;
    private EditText mPasswordEt;
    private Button mLoginBtn;
    private Button mRegisterBtn;
    private TextView mResultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initView();
    }

    private void initView() {
        mPhoneEt = (EditText) findViewById(R.id.et_phone);
        mPasswordEt = (EditText) findViewById(R.id.et_password);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mRegisterBtn = (Button) findViewById(R.id.btn_register);
        mResultTv = (TextView) findViewById(R.id.tv_result);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        findViewById(R.id.btn_create_memo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMemo();
            }
        });
    }

    private void login() {
        String username = mPhoneEt.getText().toString().trim();
        String password = mPasswordEt.getText().toString().trim();
//        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
//            Toaster.show("手机号/密码为空");
//            return;
//        }
        final String passEncrypt = EncryptUtils.encryptPassword(password);
        Logger.e("passEncrypt= " + passEncrypt);
        mLoginBtn.setEnabled(false);
        mTaskManager.start(LoginTask.login(username, passEncrypt)
                .setCallback(new TaskCallback<LoginInfo>() {

                    @Override
                    public void onFinish() {
                        mLoginBtn.setEnabled(true);
                    }

                    @Override
                    public void onError(TaskError e) {
                        Toaster.show(e.msg);
                    }

                    @Override
                    public void onSuccess(LoginInfo result) {
                        Toaster.show("登录成功");
                        SharedPrefUtils.putToPublicFile(KEY.ACCESS_TOKEN, result.accessToken);
                        SharedPrefUtils.putToPublicFile(KEY.REFRESH_TOKEN, result.refreshToken);
                        SharedPrefUtils.putToPublicFile(KEY.LOGINED_UID, result.user.id);
                        SharedPrefUtils.putToPublicFile(KEY.LOGINED_PHONE, result.user.phone);
                        SharedPrefUtils.putToPublicFile(KEY.LOGINED_PASSWORD, passEncrypt);
                        startActivity(new Intent(mActivity, NoteListActivity.class));
                    }
                }));
    }

    private void register() {
        String username = mPhoneEt.getText().toString().trim();
        String password = mPasswordEt.getText().toString().trim();
//        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
//            Toaster.show("手机号/密码为空");
//            return;
//        }
        final String passEncrypt = EncryptUtils.encryptPassword(password);
        Logger.e("passEncrypt= " + passEncrypt);
        mRegisterBtn.setEnabled(false);
        mTaskManager.start(LoginTask.register(username, passEncrypt)
                .setCallback(new TaskCallback<LoginInfo>() {

                    @Override
                    public void onFinish() {
                        mRegisterBtn.setEnabled(true);
                    }

                    @Override
                    public void onError(TaskError e) {
                        Toaster.show(e.msg);
                    }

                    @Override
                    public void onSuccess(LoginInfo result) {
                        Toaster.show("注册成功");
                        SharedPrefUtils.putToPublicFile(KEY.ACCESS_TOKEN, result.accessToken);
                        SharedPrefUtils.putToPublicFile(KEY.REFRESH_TOKEN, result.refreshToken);
                        SharedPrefUtils.putToPublicFile(KEY.LOGINED_UID, result.user.id);
                        SharedPrefUtils.putToPublicFile(KEY.LOGINED_PHONE, result.user.phone);
                        SharedPrefUtils.putToPublicFile(KEY.LOGINED_PASSWORD, passEncrypt);
                        startActivity(new Intent(mActivity, NoteListActivity.class));
                    }
                }));
    }

    private void createMemo() {
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0ODM3NTUyMzksInBob25lIjoiMTM2MzIzODUyODIiLCJ1aWQiOjF9.n_8fCxzhXxwF8q8WZNYbwsRtkbiO8O48aZPFB00ULKA";
        SharedPrefUtils.putToPublicFile(KEY.ACCESS_TOKEN, accessToken);
        String title = "希望今年发财";
        String content = "去年都没赚到钱， 希望今年发财！";
        mTaskManager.start(LoginTask.createMemo(title, content)
                .setCallback(new TaskCallback<Memo>() {

                    @Override
                    public void onFinish() {
                        mRegisterBtn.setEnabled(true);
                    }

                    @Override
                    public void onError(TaskError e) {
                        Toaster.show(e.msg);
                    }

                    @Override
                    public void onSuccess(Memo result) {
                        Toaster.show("创建成功");
                        mResultTv.setText(JSON.toJSONString(result));
                    }
                }));
    }

}
