package superconn.pds.sw.superconn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentJunmunReceive extends Fragment {


    public FragmentJunmunReceive() {
        // Required empty public constructor
    }

    public static FragmentJunmunReceive newInstance(String param1, String param2) {
        FragmentJunmunReceive fragment = new FragmentJunmunReceive();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_junmun_receive, container, false);

        TextView  junmun_receiver_receiver1 = view.findViewById( R.id.junmun_receiver_receiver1);
        junmun_receiver_receiver1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentJunmunOpen(), null).addToBackStack(null).commit();
            }
        });


        return view;
    }
}