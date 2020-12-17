package superconn.pds.sw.superconn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentCheck extends Fragment {


    private RecyclerView.LayoutManager layoutManager;


    public FragmentCheck() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check, container, false);
        layoutManager=new LinearLayoutManager(getActivity());

        // 버튼
        TextView check_tv_bit = view.findViewById(R.id.check_tv_bit);
        check_tv_bit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentBIT(), null).addToBackStack(null).commit();
            }
        });
        TextView check_tv_input = view.findViewById(R.id.check_tv_input);
        check_tv_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentInput(), null).addToBackStack(null).commit();
            }
        });
        TextView check_tv_setting = view.findViewById(R.id.check_tv_setting);
        check_tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentSetting(), null).addToBackStack(null).commit();
            }
        });


        return view;
    }
}