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

public class JunmunObstacleFragment extends Fragment {

    ArrayList<String> arrayList_obstacle, arrayList_obstacle_wire, arrayList_obstacle_tank, arrayList_obstacle_mine, arrayList_obstacle_etc;
    ArrayAdapter<String> arrayAdapter_obstacle, arrayAdapter_obstacle_detail;

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

        //============ dependant spinner - obstacle ===========//

        Spinner junmun_obstacle_sp_obstacleType = view.findViewById(R.id.junmun_obstacle_sp_obstacleType);
        final Spinner junmun_obstacle_sp_obstacleDetail = view.findViewById(R.id.junmun_obstacle_sp_obstacleDetail);

        arrayList_obstacle = new ArrayList<>();
        arrayList_obstacle.add("철조망");
        arrayList_obstacle.add("대전차 장애물");
        arrayList_obstacle.add("지뢰지대");
        arrayList_obstacle.add("기타");

        arrayAdapter_obstacle = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_obstacle);

        junmun_obstacle_sp_obstacleType.setAdapter(arrayAdapter_obstacle);

        //============ dependant spinner - detail ===========//

        arrayList_obstacle_wire = new ArrayList<>();
        arrayList_obstacle_wire.add("철조망");
        arrayList_obstacle_wire.add("가시형 철조망");
        arrayList_obstacle_wire.add("면도칼형 철조망");
        arrayList_obstacle_wire.add("미식별");

        arrayList_obstacle_tank = new ArrayList<>();
        arrayList_obstacle_tank.add("대전차 방벽");
        arrayList_obstacle_tank.add("대전차장애물(고정식)");
        arrayList_obstacle_tank.add("대전차장애물(이동식)");

        arrayList_obstacle_mine = new ArrayList<>();
        arrayList_obstacle_mine.add("지뢰지역");

        arrayList_obstacle_etc = new ArrayList<>();
        arrayList_obstacle_etc.add("부비트랩");
        arrayList_obstacle_etc.add("목책");
        arrayList_obstacle_etc.add("미식별");

        //==== spinner 장착 =====//

        junmun_obstacle_sp_obstacleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    arrayAdapter_obstacle_detail = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_obstacle_wire);
                }
                if (position == 1) {
                    arrayAdapter_obstacle_detail = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_obstacle_tank);
                }
                if (position == 2) {
                    arrayAdapter_obstacle_detail = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_obstacle_mine);
                }
                if (position == 3) {
                    arrayAdapter_obstacle_detail = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_obstacle_etc);
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