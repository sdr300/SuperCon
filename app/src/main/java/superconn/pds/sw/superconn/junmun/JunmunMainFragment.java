package superconn.pds.sw.superconn.junmun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import superconn.pds.sw.superconn.FragmentSetting;
import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;

public class JunmunMainFragment extends Fragment {


    private RecyclerView.LayoutManager layoutManager;


    public JunmunMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_junmun_main, container, false);
        layoutManager=new LinearLayoutManager(getActivity());

        // 버튼
        TextView junmun_tv_input = view.findViewById(R.id.junmun_tv_write);
        junmun_tv_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JunmunReceiveFragment(), null).addToBackStack(null).commit();
            }
        });
        TextView junmun_tv_send = view.findViewById(R.id.junmun_tv_send);
        junmun_tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JunmunSendFragment(), null).addToBackStack(null).commit();
            }
        });

        TextView junmun_tv_setting = view.findViewById(R.id.junmun_tv_setting);
        junmun_tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentSetting(), null).addToBackStack(null).commit();
            }
        });




        return view;
    }
}