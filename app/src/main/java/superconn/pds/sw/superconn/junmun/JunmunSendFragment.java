package superconn.pds.sw.superconn.junmun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;

public class JunmunSendFragment extends Fragment {

    public JunmunSendFragment() {
        // Required empty public constructor
    }

    public static JunmunSendFragment newInstance(String param1, String param2) {
        JunmunSendFragment fragment = new JunmunSendFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_junmun_send, container, false);

            TextView junmun_tv_receive = view.findViewById( R.id.junmun_tv_receive);
            junmun_tv_receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JunmunReceiveFragment(), null).addToBackStack(null).commit();
                }
            });

        Button junmun_btn_write_main = view.findViewById( R.id.junmun_btn_write_main);
        junmun_btn_write_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JummunWriteFragment(), null).addToBackStack(null).commit();
            }
        });

        Button junmun_btn_delete_send = view.findViewById(R.id.junmun_btn_delete_send);
        junmun_btn_delete_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JunmunSendDeleteFragment(), null).addToBackStack(null).commit();
            }
        });
        
        return view;
    }
}