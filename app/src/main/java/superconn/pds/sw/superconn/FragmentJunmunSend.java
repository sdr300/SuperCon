package superconn.pds.sw.superconn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentJunmunSend extends Fragment {

    public FragmentJunmunSend() {
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