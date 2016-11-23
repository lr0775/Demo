package cc.stbl.demo.task;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.ArrayList;

import cc.stbl.demo.constant.API;
import cc.stbl.demo.model.Note;
import cc.stbl.demo.util.OkHttpHelper;
import cc.stbl.demo.weapon.HttpResponse;
import cc.stbl.demo.weapon.Task;

/**
 * Created by Administrator on 2016/11/23.
 */

public class NoteTask {

    public static Task<ArrayList<Note>> getNoteList() {
        return new Task<ArrayList<Note>>() {
            @Override
            protected void call() {
                try {
                    HttpResponse response = OkHttpHelper.getInstance().get(API.NOTE);
                    if (response.error != null) {
                        onError(response.error);
                        return;
                    }
                    ArrayList<Note> data = (ArrayList<Note>) JSON.parseArray(response.result, Note.class);
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
