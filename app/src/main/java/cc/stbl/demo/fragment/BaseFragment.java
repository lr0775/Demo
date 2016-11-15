package cc.stbl.demo.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import cc.stbl.demo.weapon.TaskManager;

/**
 * Created by Administrator on 2016/10/17.
 */

public class BaseFragment extends Fragment {

    protected Activity mActivity;
    protected TaskManager mTaskManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mTaskManager = new TaskManager();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTaskManager.onDestroy();
    }
}
