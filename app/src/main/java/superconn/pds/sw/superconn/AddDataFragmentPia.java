package superconn.pds.sw.superconn;

import android.icu.text.SimpleDateFormat;
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
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import java.util.Date;
import java.util.concurrent.TimeUnit;

import superconn.pds.sw.superconn.DataBase.Person;

public class AddDataFragmentPia extends Fragment {

    private EditText inputID, inputName, inputEmail, inputPiaDate, inputPiaResult, inputPiaQna, inputPiaCompany;

    private ImageButton btnPiaSave, pia_add_btn_save_up;
    long time = new Date().getTime();
    long sysTime = System.currentTimeMillis();
    long systime1 = System.currentTimeMillis() / 1000;
    Date date = new Date();
    private RadioGroup pia_add_rg_qna, pia_add_rg_result;
    private  Button pia_add_btn_save, pia_add_btn_savecancle;
    private RadioButton pia_add_rb_question, pia_add_rb_answer,pia_add_rb_noanswer,
            pia_add_rb_unknown, pia_add_rb_friend, pia_add_rb_enemy, pia_add_rb_neutral;
    private String pia_add_string_qna,  pia_add_string_result, piaCompany;
    private EditText pia_add_et_time, pia_add_et_company;

    //시간 넣기
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss");
    String nowTime2 =  dateFormat.format(date);

    int hours = (int) TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
    int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
    int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());


    public AddDataFragmentPia() {
        // Required empty public constructor
    }

    FragmentPia fragmentPia = new FragmentPia();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 수정 후 라디오 버튼 달린 뷰
        View view= inflater.inflate(R.layout.fragment_add_pia, container, false);

        pia_add_rg_qna = view.findViewById(R.id.pia_add_rg_qna);
        pia_add_rg_result = view.findViewById(R.id.pia_add_rg_result);
        pia_add_et_time = view.findViewById(R.id.pia_add_et_time);
        pia_add_et_time.setText(nowTime2);
        pia_add_et_company = view.findViewById(R.id.pia_add_et_company);
        pia_add_rb_question = view.findViewById(R.id.pia_add_rb_question);
        pia_add_rb_answer = view.findViewById(R.id.pia_add_rb_answer);
        pia_add_rb_noanswer= view.findViewById(R.id. pia_add_rb_noanswer);
        pia_add_rb_unknown = view.findViewById(R.id.pia_add_rb_unknown);
        pia_add_rb_friend= view.findViewById(R.id.pia_add_rb_friend);
        pia_add_rb_enemy= view.findViewById(R.id.pia_add_rb_enemy);
        pia_add_btn_save = view.findViewById(R.id.pia_add_btn_save);
        pia_add_btn_save_up = view.findViewById(R.id.pia_add_btn_save_up);
        pia_add_string_qna = pia_add_rb_question.getText().toString().trim();
        pia_add_string_result = pia_add_rb_unknown.getText().toString().trim();
        pia_add_et_company.requestFocus();

        pia_add_rg_qna.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            //라디오 버튼 상태 변경값을 감지한다.
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i){
                if(i == R.id.pia_add_rb_question){
                    pia_add_string_qna = pia_add_rb_question.getText().toString().trim();//라디오 버튼의 텍스트 값을 string에 담아준것
                } else if(i == R.id.pia_add_rb_answer){
                    pia_add_string_qna = pia_add_rb_answer.getText().toString().trim();//라디오 버튼의 텍스트 값을 string에 담아준것
                } else if(i == R.id.pia_add_rb_noanswer){
                    pia_add_string_qna = pia_add_rb_noanswer.getText().toString().trim();//라디오 버튼의 텍스트 값을 string에 담아준것
                }
            }
        });
        pia_add_rg_result.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.pia_add_rb_unknown){
                    pia_add_string_result = pia_add_rb_unknown.getText().toString().trim();//라디오 버튼의 텍스트 값을 string에 담아준것
                }else if(checkedId == R.id.pia_add_rb_friend){
                    pia_add_string_result = pia_add_rb_friend.getText().toString().trim();//라디오 버튼의 텍스트 값을 string에 담아준것
                }else if(checkedId == R.id.pia_add_rb_enemy){
                    pia_add_string_result = pia_add_rb_enemy.getText().toString().trim();//라디오 버튼의 텍스트 값을 string에 담아준것
                }
            }
        });

        pia_add_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int id = Integer.parseInt(inputID.getText().toString());
                String piaQna = pia_add_string_qna;
                String piaResult = pia_add_string_result;
                if (TextUtils.isEmpty(pia_add_et_company.getText())){
                    piaCompany = pia_add_rb_unknown.getText().toString().trim();
                } else {
                    piaCompany = pia_add_et_company.getText().toString().trim();
                }

                pia_add_et_company.setText(piaCompany);

                Person person = new Person();
//                person.setId(id);
                person.setPiaDate(nowTime2);
                person.setPiaQna(piaQna);
                person.setPiaResult(piaResult);
                person.setPiaCompany(piaCompany);

                Log.d("pia_add_string_qna", pia_add_string_qna);
                Log.d("pia_add_string_result", pia_add_string_result);
                Log.d("piaCompany", piaCompany);
                Log.d("pia_add_et_company", pia_add_et_company.getText().toString().trim());

                MapActivity.roomDatabaseClass.personDao().addPerson(person);
                Toast.makeText(getActivity(), "Data Successfully saved", Toast.LENGTH_SHORT).show();
//                inputID.setText("");
                pia_add_et_company.setText("");

                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentPia(), null).commit();

            }
        });

        pia_add_btn_save_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int id = Integer.parseInt(inputID.getText().toString());
                String piaQna = pia_add_string_qna;
                String piaResult = pia_add_string_result;
                if (TextUtils.isEmpty(pia_add_et_company.getText())){
                    piaCompany = pia_add_rb_unknown.getText().toString().trim();
                } else {
                    piaCompany = pia_add_et_company.getText().toString().trim();
                }

                pia_add_et_company.setText(piaCompany);

                Person person = new Person();
//                person.setId(id);
                person.setPiaDate(nowTime2);
                person.setPiaQna(piaQna);
                person.setPiaResult(piaResult);
                person.setPiaCompany(piaCompany);

                Log.d("pia_add_string_qna", pia_add_string_qna);
                Log.d("pia_add_string_result", pia_add_string_result);
                Log.d("piaCompany", piaCompany);
                Log.d("pia_add_et_company", pia_add_et_company.getText().toString().trim());

                MapActivity.roomDatabaseClass.personDao().addPerson(person);
                Toast.makeText(getActivity(), "Data Successfully saved", Toast.LENGTH_SHORT).show();
//                inputID.setText("");
                pia_add_et_company.setText("");

                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentPia(), null).commit();

            }
        });


        return view;

    }
}