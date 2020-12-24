package superconn.pds.sw.superconn.junmun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;

public class JunmunReceiveFragment extends Fragment {


    public JunmunReceiveFragment() {
        // Required empty public constructor
    }

    public static JunmunReceiveFragment newInstance(String param1, String param2) {
        JunmunReceiveFragment fragment = new JunmunReceiveFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_junmun_receive, container, false);

        TextView junmun_tv_send = view.findViewById( R.id.junmun_tv_send);
        junmun_tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JunmunSendFragment(), null).addToBackStack(null).commit();
            }
        });

        Button junmun_btn_write_main = view.findViewById( R.id.junmun_btn_write_main);
        junmun_btn_write_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JummunWriteFragment(), null).addToBackStack(null).commit();
            }
        });

        Button junmun_btn_delete_receiver = view.findViewById(R.id.junmun_btn_delete_receiver);
        junmun_btn_delete_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JunmunReceiverDeleteFragment(), null).addToBackStack(null).commit();
            }
        });


        return view;
    }
}