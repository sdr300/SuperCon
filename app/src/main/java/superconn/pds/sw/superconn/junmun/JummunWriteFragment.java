package superconn.pds.sw.superconn.junmun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;


public class JummunWriteFragment extends Fragment {

    public JummunWriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jummun_write, container, false);


        // 버튼 클릭으로 해당 프래그먼트로 이동
        Button junmun_btn_informal = view.findViewById( R.id.junmun_btn_informal);
        junmun_btn_informal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JunmunInformalFragment(), null).addToBackStack(null).commit();
            }
        });

        Button junmun_btn_intel = view.findViewById( R.id.junmun_btn_intel);
        junmun_btn_intel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JunmunIntelFragment(), null).addToBackStack(null).commit();
            }
        });

        Button junmun_btn_obstacle = view.findViewById( R.id.junmun_btn_obstacle);
        junmun_btn_obstacle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JunmunObstacleFragment(), null).addToBackStack(null).commit();
            }
        });

        Button junmun_btn_locationreport = view.findViewById( R.id.junmun_btn_locationreport);
        junmun_btn_locationreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JunmunLocationFragment(), null).addToBackStack(null).commit();
            }
        });

        return view;
    }
}