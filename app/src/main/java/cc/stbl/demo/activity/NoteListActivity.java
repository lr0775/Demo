package cc.stbl.demo.activity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import cc.stbl.demo.R;
import cc.stbl.demo.constant.KEY;
import cc.stbl.demo.model.Note;
import cc.stbl.demo.task.NoteTask;
import cc.stbl.demo.util.SharedPrefUtils;
import cc.stbl.demo.util.Toaster;
import cc.stbl.demo.weapon.TaskCallback;
import cc.stbl.demo.weapon.TaskError;

public class NoteListActivity extends BaseActivity {

    TextView mUserInfoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        mUserInfoTv = (TextView) findViewById(R.id.tv_user_info);
        int uid = (int) SharedPrefUtils.getFromPublicFile(KEY.LOGINED_UID, 0);
        String phone = (String) SharedPrefUtils.getFromPublicFile(KEY.LOGINED_PHONE, "");
        mUserInfoTv.setText("uid: " + uid + "\nphone: " + phone);

        getNoteList();
    }

    private void getNoteList() {
        mTaskManager.start(NoteTask.getNoteList()
                .setCallback(new TaskCallback<ArrayList<Note>>() {
                    @Override
                    public void onError(TaskError e) {
                        Toaster.show(e.msg);
                    }

                    @Override
                    public void onSuccess(ArrayList<Note> result) {

                    }
                }));
    }
}
