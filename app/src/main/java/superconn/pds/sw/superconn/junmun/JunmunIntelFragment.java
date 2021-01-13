package superconn.pds.sw.superconn.junmun;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import superconn.pds.sw.superconn.R;
import superconn.pds.sw.superconn.databinding.FragmentJunmunIntelBinding;
import superconn.pds.sw.superconn.etc.CustomSpinnerAdapter;

public class JunmunIntelFragment extends Fragment {

    private FragmentJunmunIntelBinding mBinding;
    ArrayList<String> arrayList_type1,arrayList_type2, arrayList_type3, arrayList_type2_unit, arrayList_type2_equipment, arrayList_type2_emergency, arrayList_type3_count, arrayList_type3_property;
    ArrayAdapter<String> arrayAdapter_type3, arrayAdapter_type1, arrayAdapter_type2;

    String[] spinner_type1,spinner_type2_unit, spinner_type2_equip, spinner_type2_emergency, spinner_type3_property, spinner_type3_count;
    int[] im_type1,im_type2_unit, im_type2_equip, im_type2_emergency, im_type3_property, im_type3_count;
    int unit_idx, equip_idx, emergency_idx, property_idx;

    public JunmunIntelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_junmun_intel, container, false);

        //============ dependant spinner - type1 ===========//

        mBinding = FragmentJunmunIntelBinding.inflate(inflater, container, false);
        Spinner junmun_intel_sp_type1 = view.findViewById(R.id.junmun_intel_sp_type1);
        final Spinner junmun_intel_sp_type2 = view.findViewById(R.id.junmun_intel_sp_type2);
        final Spinner junmun_intel_sp_type3 = view.findViewById(R.id.junmun_intel_sp_type3);
        final TextView junmun_intel_tv_quantity2 = view.findViewById(R.id. junmun_intel_tv_quantity2);

        // 이미지 삽입
        spinner_type1 = new String[] {"부대", "장비", "비상관리"};
        im_type1 = new int[]{
                R.drawable.none,
                R.drawable.none,
                R.drawable.none
        };

        spinner_type2_unit =  new String[] {"미지정", "조", "분대", "반", "소대", "중대", "대대"};
        im_type2_unit = new int[]{
                R.drawable.spinnerunit0,
                R.drawable.spinnerunit1,
                R.drawable.spinnerunit2,
                R.drawable.spinnerunit3,
                R.drawable.spinnerunit4,
                R.drawable.spinnerunit5,
                R.drawable.spinnerunit6
        };

        spinner_type3_property =  new String[] {"미식별(적)","보병", "포병","기갑", "공병","항공", "수색/정찰"};
        im_type3_property  = new int[]{
                R.drawable.spinnerproperty0,
                R.drawable.spinnerproperty1,
                R.drawable.spinnerproperty2,
                R.drawable.spinnerproperty3,
                R.drawable.spinnerproperty4,
                R.drawable.spinnerproperty5,
                R.drawable.spinnerproperty6
        };

        spinner_type2_equip =  new String[] {"장갑차량","다목적차량", "공병차량","민간차량", "고정익항공기","회전익항공기"};
        im_type2_equip = new int[]{
                R.drawable.spinnerequip0,
                R.drawable.spinnerequip1,
                R.drawable.spinnerequip2,
                R.drawable.spinnerequip3,
                R.drawable.spinnerequip4,
                R.drawable.spinnerequip5
        };

        spinner_type2_emergency =  new String[] {"화재"};
        im_type2_emergency = new int[]{
                R.drawable.spinneremergency0
        };

        spinner_type3_count =  new String[] {"1","2","3","4","5"};
        im_type3_count = new int[]{
                R.drawable.none,
                R.drawable.none,
                R.drawable.none,
                R.drawable.none,
                R.drawable.none
        };

        arrayAdapter_type1  =  new CustomSpinnerAdapter(getActivity().getApplicationContext(), spinner_type1, im_type1);

        final TextView junmun_intel_tv_quantity = view.findViewById(R.id.junmun_intel_tv_quantity);

        junmun_intel_sp_type1.setAdapter(arrayAdapter_type1);

        //==== spinner 장착(커스텀 이후) =====//

        junmun_intel_sp_type1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    arrayAdapter_type2  = new CustomSpinnerAdapter(getActivity().getApplicationContext(), spinner_type2_unit, im_type2_unit);
                    arrayAdapter_type3  = new CustomSpinnerAdapter(getActivity().getApplicationContext(), spinner_type3_property, im_type3_property);
                    junmun_intel_tv_quantity.setText("개체 성질 : ");
                    junmun_intel_tv_quantity2.setText("");
                }
                if (position == 1) {
                    arrayAdapter_type2  = new CustomSpinnerAdapter(getActivity().getApplicationContext(), spinner_type2_equip, im_type2_equip);
                    arrayAdapter_type3  = new CustomSpinnerAdapter(getActivity().getApplicationContext(), spinner_type3_count, im_type3_count);
                    junmun_intel_tv_quantity.setText("개체 수량 : ");
                    junmun_intel_tv_quantity2.setText("문");
                }
                if (position == 2) {
                    arrayAdapter_type2  = new CustomSpinnerAdapter(getActivity().getApplicationContext(), spinner_type2_emergency, im_type2_emergency);
                    arrayAdapter_type3  = new CustomSpinnerAdapter(getActivity().getApplicationContext(), spinner_type3_count, im_type3_count);
                    junmun_intel_tv_quantity.setText("개체 수량 : ");
                    junmun_intel_tv_quantity2.setText("문");
                }

                junmun_intel_sp_type2.setAdapter(arrayAdapter_type2);
                junmun_intel_sp_type3.setAdapter(arrayAdapter_type3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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