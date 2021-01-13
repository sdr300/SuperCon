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

import java.util.ArrayList;

import superconn.pds.sw.superconn.R;
import superconn.pds.sw.superconn.etc.CustomSpinnerAdapter;

public class JunmunObstacleFragment extends Fragment {

    ArrayList<String> arrayList_obstacle, arrayList_obstacle_wire, arrayList_obstacle_tank, arrayList_obstacle_mine, arrayList_obstacle_etc;
    ArrayAdapter<String> arrayAdapter_obstacle, arrayAdapter_obstacle_detail;

    String[] spinner_obstacle, spinner_obstacle_wire, spinner_obstacle_tank, spinner_obstacle_mine, spinner_obstacle_etc;
    int[] im_obstacle, im_obstacle_wire, im_obstacle_tank, im_obstacle_mine, im_obstacle_etc;

    public JunmunObstacleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_junmun_obstacle, container, false);

        //spinner 장착
        Spinner junmun_obstacle_sp_priority = view.findViewById(R.id.junmun_obstacle_sp_priority);

        final ArrayAdapter arrayAdapter_priority = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.priority, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter_priority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        junmun_obstacle_sp_priority.setAdapter(arrayAdapter_priority);

        Spinner junmun_obstacle_sp_obstaclePia = view.findViewById(R.id.junmun_obstacle_sp_obstaclePia);

        final ArrayAdapter arrayAdapter_obstaclePia = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.obstaclePia, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter_obstaclePia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        junmun_obstacle_sp_obstaclePia.setAdapter(arrayAdapter_obstaclePia);

        Spinner junmun_obstacle_sp_obstacleType = view.findViewById(R.id.junmun_obstacle_sp_obstacleType);
        final Spinner junmun_obstacle_sp_obstacleDetail = view.findViewById(R.id.junmun_obstacle_sp_obstacleDetail);

        //spinner adapter

        spinner_obstacle = new String[]{"철조망", "대전차 장애물", "지뢰지대", "기타"};
        im_obstacle = new int[]{
                R.drawable.none,
                R.drawable.none,
                R.drawable.none,
                R.drawable.none
        };

        spinner_obstacle_wire = new String[]{"철조망"};
        im_obstacle_wire = new int[]{
                R.drawable.spinnerobstaclewire0
        };

        spinner_obstacle_tank = new String[]{"대전차 방벽", "대전차장애물(고정식)", "대전차장애물(이동식)"};
        im_obstacle_tank = new int[]{
                R.drawable.spinnerobstacletank0,
                R.drawable.spinnerobstacletank1,
                R.drawable.spinnerobstacletank2
        };

        spinner_obstacle_mine = new String[]{"지뢰지역"};
        im_obstacle_mine = new int[]{
                R.drawable.spinnerobstaclemine0
        };

        spinner_obstacle_etc = new String[]{"부비트랩", "목책"};
        im_obstacle_etc = new int[]{
                R.drawable.spinnerobstacleetc0,
                R.drawable.spinnerobstacleetc1
        };

        //============ dependant spinner - obstacle ===========//

        arrayAdapter_obstacle = new CustomSpinnerAdapter(getActivity().getApplicationContext(), spinner_obstacle, im_obstacle);

        junmun_obstacle_sp_obstacleType.setAdapter(arrayAdapter_obstacle);

        //==== spinner 장착 =====//

        junmun_obstacle_sp_obstacleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    arrayAdapter_obstacle_detail  = new CustomSpinnerAdapter(getActivity().getApplicationContext(), spinner_obstacle_wire, im_obstacle_wire);
                }
                if (position == 1) {
                    arrayAdapter_obstacle_detail  = new CustomSpinnerAdapter(getActivity().getApplicationContext(), spinner_obstacle_tank, im_obstacle_tank);
                }
                if (position == 2) {
                    arrayAdapter_obstacle_detail  = new CustomSpinnerAdapter(getActivity().getApplicationContext(), spinner_obstacle_mine, im_obstacle_mine);
                }
                if (position == 3) {
                    arrayAdapter_obstacle_detail  = new CustomSpinnerAdapter(getActivity().getApplicationContext(), spinner_obstacle_etc, im_obstacle_etc);
                }

                junmun_obstacle_sp_obstacleDetail.setAdapter(arrayAdapter_obstacle_detail);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //엔터 클릭시 다음줄이 아닌 키보드 안보이게 설정
        final EditText junmun_obstacle_et_time = view.findViewById(R.id.junmun_obstacle_et_time);
        final EditText junmun_obstacle_et_obslocation = view.findViewById(R.id.junmun_obstacle_et_obslocation);

        junmun_obstacle_et_time.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(junmun_obstacle_et_time.getWindowToken(), 0);    //hide keyboard
                    return true;
                }

                return false;
            }
        });

        junmun_obstacle_et_obslocation.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(junmun_obstacle_et_obslocation.getWindowToken(), 0);    //hide keyboard
                    return true;
                }

                return false;
            }
        });

        return view;
    }
}