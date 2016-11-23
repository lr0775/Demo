package cc.stbl.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cc.stbl.demo.R;
import cc.stbl.demo.constant.KEY;
import cc.stbl.demo.model.LoginInfo;
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
                        SharedPrefUtils.putToPublicFile(KEY.LOGINED_UID, result.user.id);
                        SharedPrefUtils.putToPublicFile(KEY.LOGINED_PHONE, result.user.phone);
                        SharedPrefUtils.putToPublicFile(KEY.LOGINED_PASSWORD, passEncrypt);
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
                        SharedPrefUtils.putToPublicFile(KEY.LOGINED_UID, result.user.id);
                        SharedPrefUtils.putToPublicFile(KEY.LOGINED_PHONE, result.user.phone);
                        SharedPrefUtils.putToPublicFile(KEY.LOGINED_PASSWORD, passEncrypt);
                    }
                }));
    }

}
