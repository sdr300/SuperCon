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

        Button junmun_btn_write_main = view.findViewById( R.id.junmun_btn_informal);
        junmun_btn_write_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JunmunInformalFragment(), null).addToBackStack(null).commit();
            }
        });

        return view;
    }
}