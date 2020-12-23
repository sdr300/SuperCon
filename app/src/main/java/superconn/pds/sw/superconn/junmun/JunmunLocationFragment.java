package superconn.pds.sw.superconn.junmun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import superconn.pds.sw.superconn.R;

public class JunmunLocationFragment extends Fragment {

    ArrayList<String> arrayList;

    public JunmunLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_junmun_location, container, false);

        arrayList = new ArrayList<>();
        Spinner junmun_location_sp_priority = view.findViewById(R.id.junmun_location_sp_priority);

        final ArrayAdapter arrayAdapter_priority = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.priority, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter_priority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        junmun_location_sp_priority.setAdapter(arrayAdapter_priority);

        return view;
    }
}