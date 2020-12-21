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

public class JunmunInformalFragment extends Fragment {
    private Spinner junmun_informal_sp;

    ArrayList<String> arrayList;

    public JunmunInformalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_junmun_informal, container, false);

        junmun_informal_sp = view.findViewById(R.id.junmun_informal_sp);

        arrayList = new ArrayList<>();
        arrayList.add("보통");
        arrayList.add("지급");
        arrayList.add("긴급");
        arrayList.add("위급");

        final ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.informal, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        junmun_informal_sp.setAdapter(arrayAdapter);


        return view;
    }
}