package superconn.pds.sw.superconn.camera;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import superconn.pds.sw.superconn.R;
import superconn.pds.sw.superconn.databinding.FragmentCameraSensorBinding;

public class CameraSensorFragment extends Fragment {

    private FragmentCameraSensorBinding mBinding;

    public CameraSensorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_sensor, container, false);
        mBinding = FragmentCameraSensorBinding.inflate(inflater,container, false);




        return mBinding.getRoot();
    }
}