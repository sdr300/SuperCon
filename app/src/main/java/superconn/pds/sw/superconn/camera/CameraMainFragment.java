package superconn.pds.sw.superconn.camera;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import superconn.pds.sw.superconn.FragmentSetting;
import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;

/**
 * created 2020-12-21
 */
public class CameraMainFragment extends Fragment {


    public CameraMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_main, container, false);

        Button camera_btn_delete = view.findViewById(R.id.camera_btn_delete);
        camera_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new CameraDeleteFragment(), null).addToBackStack(null).commit();
            }
        });



    return view;
    }
}