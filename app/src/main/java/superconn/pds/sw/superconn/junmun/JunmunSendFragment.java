package superconn.pds.sw.superconn.junmun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import superconn.pds.sw.superconn.AddDataFragmentJunmun;
import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;

public class JunmunSendFragment extends Fragment {

    public JunmunSendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_junmun_send, container, false);

        ImageView junmun_add_btn= view.findViewById(R.id.junmun_add_btn);
        junmun_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new AddDataFragmentJunmun(), null).addToBackStack(null).commit();
            }
        });

        return view;
    }
}