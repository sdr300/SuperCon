package superconn.pds.sw.superconn;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import java.util.Date;

import superconn.pds.sw.superconn.DataBase.Buho;


public class UpdateDataFragmentBuho extends Fragment {

    private GpsTracker gpsTracker;
    private ImageButton buho_update_btn_save_up;
    long time = new Date().getTime();
    long sysTime = System.currentTimeMillis();
    long systime1 = System.currentTimeMillis() / 1000;
    Date date = new Date();
    private RadioGroup buho_update_rg_icon, buho_update_rg_location;
    private Button buho_update_btn_save, buho_update_btn_savecancle;
    private RadioButton buho_update_rb_unknown, buho_update_rb_our, buho_update_rb_enemy,
            buho_update_rb_map, buho_update_rb_self;
    private String buho_update_string_icon,  buhoCompany, buhoLatitude, buhoLongitude;
    private EditText buho_update_et_id, buho_update_et_time, buho_update_et_latitude, buho_update_et_longitude, buho_update_et_company,
            buho_update_et_sender ;

    public UpdateDataFragmentBuho() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //현재 좌표
        gpsTracker = new GpsTracker(getActivity());

        final double latitude = gpsTracker.getLatitude();
        final double longitude = gpsTracker.getLongitude();
        final double maplatitude = Double.parseDouble(String.format("%.6f", gpsTracker.getLatitude()));
        final double maplongitude = Double.parseDouble(String.format("%.6f", gpsTracker.getLongitude()));

        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_update_buho, container, false);

        buho_update_et_id = view.findViewById(R.id.buho_update_et_id);
        buho_update_rg_icon = view.findViewById(R.id.buho_update_rg_icon);
        buho_update_rg_location = view.findViewById(R.id.buho_update_rg_location);

        buho_update_rb_unknown = view.findViewById(R.id.buho_update_rb_unknown);
        buho_update_rb_our = view.findViewById(R.id.buho_update_rb_our);
        buho_update_rb_enemy= view.findViewById(R.id. buho_update_rb_enemy);
        buho_update_rb_map= view.findViewById(R.id.buho_update_rb_map);
        buho_update_rb_self= view.findViewById(R.id.buho_update_rb_self);

        buho_update_et_time = view.findViewById(R.id.buho_update_et_time);
//        buho_update_et_latitude = view.findViewById(R.id.buho_update_et_latitude);
//        buho_update_et_longitude = view.findViewById(R.id.buho_update_et_longitude);
        buho_update_et_company = view.findViewById(R.id.buho_update_et_company);
        buho_update_et_sender = view.findViewById(R.id.buho_update_et_sender);

        buho_update_btn_save = view.findViewById(R.id.buho_update_btn_save);
        buho_update_btn_save_up = view.findViewById(R.id.buho_update_btn_save_up);

        Buho buho = new Buho();
        buho_update_string_icon = getArguments().getString("buhoIcon").trim();

        //======= 군대부호(icon) 라디오박스그룹 받아와서 표시 및 선택기능===========
        buho_update_rb_unknown.setChecked(true);
        buho_update_rb_our.setChecked(false);
        buho_update_rb_enemy.setChecked(false);
        if(buho_update_string_icon.matches("unknown") ) buho_update_rb_unknown.setChecked(true);
        if(buho_update_string_icon.matches("our")) buho_update_rb_our.setChecked(true);
        if(buho_update_string_icon.matches("enemy"))  buho_update_rb_enemy.setChecked(true);

        buho_update_rg_icon.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            //라디오 버튼 상태 변경값을 감지한다.
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i){
                if(i == R.id.buho_update_rb_unknown){
                    buho_update_string_icon = "unknown";
                } else if(i == R.id.buho_update_rb_our){
                    buho_update_string_icon = "our";
                } else if(i == R.id.buho_update_rb_enemy){
                    buho_update_string_icon = "enemy";
                }
            }
        });

        //=======  좌표 그룹===========
        buho_update_rb_map.setChecked(true);

        Log.d("id", String.valueOf(getArguments().getInt("ID")));

        if (getArguments() != null) {
            buho_update_et_id.setText(String.valueOf(getArguments().getInt("ID")));
            buho_update_et_time.setText(getArguments().getString("buhoDate"));
            buho_update_et_latitude.setText(getArguments().getString("buhoLatitude"));
            buho_update_et_longitude.setText(getArguments().getString("buhoLongitude"));
            buho_update_et_company.setText(getArguments().getString("buhoCompany"));
            buho_update_et_sender.setText(getArguments().getString("buhoSender"));

        }

        buho_update_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buho_update_btn_save:
                        String buhoIcon = buho_update_string_icon;
                        String buhoSender = buho_update_et_sender.getText().toString().trim();

//                        if (buho_update_rb_map.isChecked()){
//                            buhoLocation = "미확인 좌표";
//                        } else if (buho_update_rb_self.isChecked() && TextUtils.isEmpty(buho_update_et_location.getText())){
//                            buhoLocation = "현재 좌표";
//                        } else {
//                            buhoLocation = buho_update_et_location.getText().toString().trim();
//                        }

                        if (TextUtils.isEmpty(buho_update_et_company.getText())){
                            buhoCompany = "미식별";
                        } else {
                            buhoCompany = buho_update_et_company.getText().toString().trim();
                        }

                        int ID = Integer.parseInt(buho_update_et_id.getText().toString());
                        String Date = buho_update_et_time.getText().toString();

                        //상황도 가져오기

                        final String buhoLatitude = buho_update_et_latitude.getText().toString();
                        final String buhoLongitude = buho_update_et_longitude.getText().toString();

                        Buho buho = new Buho();

                        buho.setBuhoID(ID);
                        buho.setBuhoDate(Date);
                        buho.setBuhoIcon(buhoIcon);
                        buho.setBuhoLatitude(buhoLatitude);
                        buho.setBuhoLongitude(buhoLongitude);
                        buho.setBuhoCompany(buhoCompany);
                        buho.setBuhoSender(buhoSender);

                        MapActivity.roomDatabaseClass.buhoDao().updateBuho(buho);

                        MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentBuho(), null).commit();
                }
            }
        });
        return view;
    }

    public void addLatitude(String s) {
        if (buho_update_rb_self.isChecked()){
            buho_update_et_latitude.setText(s);
        }
    }
    public void addLongitude(String s) {
        if (buho_update_rb_self.isChecked()){
            buho_update_et_longitude.setText(s);
        }
    }

}