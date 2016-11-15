package cc.stbl.demo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cc.stbl.demo.R;
import cc.stbl.demo.task.LoginTask;
import cc.stbl.demo.util.EncryptUtils;
import cc.stbl.demo.util.Logger;
import cc.stbl.demo.util.Toaster;
import cc.stbl.demo.weapon.TaskCallback;
import cc.stbl.demo.weapon.TaskError;

public class NoteActivity extends BaseActivity {

    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initView();
    }

    private void initView() {
        mUsernameEt = (EditText) findViewById(R.id.et_username);
        mPasswordEt = (EditText) findViewById(R.id.et_password);
        mLoginBtn = (Button) findViewById(R.id.btn_login);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String username = mUsernameEt.getText().toString().trim();
        String password = mPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toaster.show("账号/密码为空");
            return;
        }
        String passEncrypt = EncryptUtils.encryptPassword(password);
        Logger.e("passEncrypt= " + passEncrypt);
        mLoginBtn.setEnabled(false);
        mTaskManager.start(LoginTask.login(username, passEncrypt)
                .setCallback(new TaskCallback<String>() {

                    @Override
                    public void onFinish() {
                        mLoginBtn.setEnabled(true);
                    }

                    @Override
                    public void onError(TaskError e) {
                        Toaster.show(e.msg);
                    }

                    @Override
                    public void onSuccess(String result) {
                        Toaster.show(result);
                    }
                }));
    }

}
