package cc.stbl.demo.task;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import cc.stbl.demo.App;
import cc.stbl.demo.model.Picture;
import cc.stbl.demo.weapon.Task;

/**
 * Created by Administrator on 2016/10/17.
 */

public class AlbumTask {

    public static Task<ArrayList<Picture>> getAllPicture() {
        return new Task<ArrayList<Picture>>() {
            @Override
            protected void call() {
                ArrayList<Picture> list = new ArrayList<>();
                final String[] projectionPhotos = {
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DATE_TAKEN,
                        MediaStore.Images.Media.ORIENTATION,
                        MediaStore.Images.Thumbnails.DATA
                };
                Cursor cursor = null;
                try {
                    cursor = MediaStore.Images.Media.query(App.getContext().getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            , projectionPhotos, "", null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
                    if (cursor != null) {
                        int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                        final int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
                        while (cursor.moveToNext()) {
                            int bucketId = cursor.getInt(bucketIdColumn);
                            String bucketName = cursor.getString(bucketNameColumn);
                            final int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                            final int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                            //int thumbImageColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                            final int imageId = cursor.getInt(imageIdColumn);
                            final String path = cursor.getString(dataColumn);
                            //final String thumb = cursor.getString(thumbImageColumn);
                            File file = new File(path);
                            if (file.exists() && file.length() > 0) {
                                Log.e("demo", bucketName + ",  " + path);
                                Picture picture = new Picture();
                                picture.file = file;
                                picture.path = path;
                                list.add(picture);
                            }
                        }
                        onSuccess(list);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onError(e);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        };
    }

}
