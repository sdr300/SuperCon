package superconn.pds.sw.superconn.junmun;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import superconn.pds.sw.superconn.R;

public class JunmunIntelFragment extends Fragment {

    ArrayList<String> arrayList;

    public JunmunIntelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_junmun_intel, container, false);

        //spinner 장착

        arrayList = new ArrayList<>();
        Spinner junmun_intel_sp_priority = view.findViewById(R.id.junmun_intel_sp_priority);

        final ArrayAdapter arrayAdapter_priority = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.priority, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter_priority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        junmun_intel_sp_priority.setAdapter(arrayAdapter_priority);

        Spinner junmun_intel_sp_type = view.findViewById(R.id.junmun_intel_sp_type);

        final ArrayAdapter arrayAdapter_type = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.type, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        junmun_intel_sp_type.setAdapter(arrayAdapter_type);

        Spinner junmun_intel_sp_unit = view.findViewById(R.id.junmun_intel_sp_unit);

        final ArrayAdapter arrayAdapter_unit = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.type, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter_unit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        junmun_intel_sp_unit.setAdapter(arrayAdapter_type);

        Spinner junmun_intel_sp_unit2 = view.findViewById(R.id.junmun_intel_sp_unit2);

        final ArrayAdapter arrayAdapter_unit2 = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.pia, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter_unit2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        junmun_intel_sp_unit2.setAdapter(arrayAdapter_type);

        //엔터 클릭시 다음줄이 아닌 키보드 안보이게 설정
        final EditText junmun_intel_et_time = view.findViewById(R.id.junmun_intel_et_time);
        final EditText junmun_intel_et_location_observe = view.findViewById(R.id.junmun_intel_et_location_observe);
        final EditText junmun_intel_et_location_object = view.findViewById(R.id.junmun_intel_et_location_object);

        junmun_intel_et_time.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(junmun_intel_et_time.getWindowToken(), 0);    //hide keyboard
                    return true;
                }

                return false;
            }
        });

        junmun_intel_et_location_observe.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(junmun_intel_et_location_observe.getWindowToken(), 0);    //hide keyboard
                    return true;
                }

                return false;
            }
        });

        junmun_intel_et_location_object.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(junmun_intel_et_location_object.getWindowToken(), 0);    //hide keyboard
                    return true;
                }

                return false;
            }
        });

        return view;




    }
}