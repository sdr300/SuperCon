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

public class JunmunObstacleFragment extends Fragment {

    ArrayList<String> arrayList;

    public JunmunObstacleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_junmun_obstacle, container, false);


        //spinner 장착

        arrayList = new ArrayList<>();
        Spinner junmun_obstacle_sp_priority = view.findViewById(R.id.junmun_obstacle_sp_priority);

        final ArrayAdapter arrayAdapter_priority = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.priority, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter_priority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        junmun_obstacle_sp_priority.setAdapter(arrayAdapter_priority);

        Spinner junmun_obstacle_sp_obstaclePia = view.findViewById(R.id.junmun_obstacle_sp_obstaclePia);

        final ArrayAdapter arrayAdapter_obstaclePia = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.obstaclePia, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter_obstaclePia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        junmun_obstacle_sp_obstaclePia.setAdapter(arrayAdapter_obstaclePia);

        Spinner junmun_obstacle_sp_obstacleType = view.findViewById(R.id.junmun_obstacle_sp_obstacleType);

        final ArrayAdapter arrayAdapter_obstacleType = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.obstacleType, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter_obstacleType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        junmun_obstacle_sp_obstacleType.setAdapter(arrayAdapter_obstacleType);

        Spinner junmun_obstacle_sp_obstacleDetail = view.findViewById(R.id.junmun_obstacle_sp_obstacleDetail);

        final ArrayAdapter arrayAdapter_obstacleDetail = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.obstacleDetail, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter_obstacleDetail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        junmun_obstacle_sp_obstacleDetail.setAdapter(arrayAdapter_obstacleDetail);




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