package superconn.pds.sw.superconn;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import android.widget.Toast;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AddDataFragmentBuho extends Fragment {

    private EditText inputID, inputName, inputEmail, inputbuhoDate, inputbuhoResult, inputbuhoQna, inputbuhoCompany;


    private ImageButton buho_add_btn_save_up;
    long time = new Date().getTime();
    long sysTime = System.currentTimeMillis();
    long systime1 = System.currentTimeMillis() / 1000;
    Date date = new Date();
    private RadioGroup buho_add_rg_icon, buho_add_rg_location;
    private Button buho_add_btn_save, buho_add_btn_savecancle;
    private RadioButton buho_add_rb_unknown, buho_add_rb_our, buho_add_rb_enemy,
            buho_add_rb_map, buho_add_rb_self;
    private String buho_add_string_icon,  buho_add_string_location, buho_add_string_company;
    private EditText inputBuhoID, buho_add_et_time, buho_add_et_location, buho_add_et_company;

    //시간 넣기
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss");
    String nowTime2 =  dateFormat.format(date);

    int hours = (int) TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
    int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
    int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    String second = nowTime2.substring(nowTime2.length()-2, nowTime2.length());

    public AddDataFragmentBuho() {
        // Required empty public constructor
    }

    FragmentBuho fragmentBuho = new FragmentBuho();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 수정 후 라디오 버튼 달린 뷰
        View view= inflater.inflate(R.layout.fragment_add_buho, container, false);

        buho_add_rg_icon = view.findViewById(R.id.buho_add_rg_icon);
        buho_add_rg_location = view.findViewById(R.id.buho_add_rg_location);

        buho_add_rb_unknown = (RadioButton)view.findViewById(R.id.buho_add_rb_unknown);
        buho_add_rb_our = (RadioButton)view.findViewById(R.id.buho_add_rb_our);
        buho_add_rb_enemy= (RadioButton)view.findViewById(R.id. buho_add_rb_enemy);
        buho_add_rb_map= view.findViewById(R.id.buho_add_rb_map);
        buho_add_rb_self= view.findViewById(R.id.buho_add_rb_self);

        buho_add_et_time = view.findViewById(R.id.buho_add_et_time);
        buho_add_et_time.setText(nowTime2);
        buho_add_et_location = view.findViewById(R.id.buho_add_et_location);
        buho_add_et_company = view.findViewById(R.id.buho_add_et_company);

        buho_add_btn_save = view.findViewById(R.id.buho_add_btn_save);
        buho_add_btn_save_up = view.findViewById(R.id.buho_add_btn_save_up);

        Buho buho = new Buho();
        buho_add_string_company = buho_add_rb_unknown.getText().toString().trim();
        buho_add_et_company.requestFocus();

        buho_add_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buho_add_rb_unknown.isChecked()){
                    buho_add_string_icon = "unknown";
                } else  if (buho_add_rb_our.isChecked()){
                    buho_add_string_icon = "our";
                } else  if (buho_add_rb_enemy.isChecked()){
                    buho_add_string_icon = "enemy";
                }

                if (buho_add_rb_map.isChecked()){
                    buho_add_string_location = "미확인 좌표";
                } else if (buho_add_rb_self.isChecked() && TextUtils.isEmpty(buho_add_et_location.getText())){
                    buho_add_string_location = "현재 좌표";
                } else {
                    buho_add_string_location = buho_add_et_location.getText().toString().trim();
                }

                if (TextUtils.isEmpty(buho_add_et_company.getText())){
                    buho_add_string_company = "미식별";
                } else {
                    buho_add_string_company = buho_add_et_company.getText().toString().trim();
                }

                buho_add_et_company.setText(buho_add_string_company);

                Buho buho = new Buho();
                buho.setBuhoDate(nowTime2);
                buho.setBuhoIcon(buho_add_string_icon);
                buho.setBuhoLocation(buho_add_string_location);
                buho.setBuhoCompany(buho_add_string_company);
                buho.setBuhoSender("전투원" + second);

                Log.d("buho_add_string_icon", "부호"+buho_add_string_icon);
                Log.d("buho_add_string_location", "좌표"+buho_add_string_location);
                Log.d("buho_add_et_company", "소속"+buho_add_string_company);
                Log.d("buhoSender","전투원" + second);

                MapActivity.roomDatabaseClass.buhoDao().addBuho(buho);
                Toast.makeText(getActivity(), "Data Successfully saved", Toast.LENGTH_SHORT).show();
                buho_add_et_company.setText("");

                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentBuho(), null).commit();

            }
        });

        buho_add_btn_save_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(buho_add_et_company.getText())){
                    buho_add_string_company = "미식별";
                } else {
                    buho_add_string_company = buho_add_et_company.getText().toString().trim();
                }

                buho_add_et_company.setText(buho_add_string_company);

                Buho buho = new Buho();
                buho.setBuhoDate(nowTime2);
                buho.setBuhoIcon(buho_add_string_icon);
                buho.setBuhoLocation(buho_add_string_location);
                buho.setBuhoCompany(buho_add_string_company);
                buho.setBuhoSender("전투원" + second);

                MapActivity.roomDatabaseClass.buhoDao().addBuho(buho);
                Toast.makeText(getActivity(), "Data Successfully saved", Toast.LENGTH_SHORT).show();
                buho_add_et_company.setText("");

                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentBuho(), null).commit();

            }
        });

        return view;
    }
}