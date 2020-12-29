package superconn.pds.sw.superconn.junmun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;

public class JunmunReceiverDeleteFragment extends Fragment {

    public JunmunReceiverDeleteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_junmun_receiver_delete, container, false);

        TextView junmun_tv_send = view.findViewById( R.id.junmun_tv_send);
        junmun_tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new JunmunSendFragment(), null).addToBackStack(null).commit();
            }
        });

        return view;
    }
}