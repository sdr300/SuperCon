package superconn.pds.sw.superconn.etc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import superconn.pds.sw.superconn.R;

/**
 * created 2020-12-21
 */

public class EtcMainFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;

    public EtcMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etc_main, container, false);
        layoutManager=new LinearLayoutManager(getActivity());
//
//        // 버튼
//        TextView junmun_tv_location = view.findViewById(R.id.junmun_tv_location);
//        junmun_tv_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentJunmunLocation(), null).addToBackStack(null).commit();
//            }
//        });
//        TextView junmun_tv_input = view.findViewById(R.id.junmun_tv_write);
//        junmun_tv_input.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentJunmunReceive(), null).addToBackStack(null).commit();
//            }
//        });
//        TextView junmun_tv_send = view.findViewById(R.id.junmun_tv_send);
//        junmun_tv_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentJunmunSend(), null).addToBackStack(null).commit();
//            }
//        });
//
//        TextView junmun_tv_setting = view.findViewById(R.id.junmun_tv_setting);
//        junmun_tv_setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentSetting(), null).addToBackStack(null).commit();
//            }
//        });


        return view;
    }

}