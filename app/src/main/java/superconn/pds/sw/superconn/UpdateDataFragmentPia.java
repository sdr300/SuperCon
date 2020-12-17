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

import superconn.pds.sw.superconn.DataBase.Person;


public class UpdateDataFragmentPia extends Fragment {

    private EditText inputPiaIDUpdate, inputPiaQnaUpdate, inputPiaResultUpdate, inputPiaCompanyUpdate, inputPiaDateUpdate, pia_update_et_id;
    private ImageButton btnPiaUpdate;
    private RadioGroup pia_update_rg_qna, pia_update_rg_result;
    private  Button pia_update_btn_save, pia_update_btn_savecancle;
    private RadioButton pia_update_rb_question, pia_update_rb_answer,pia_update_rb_noanswer,
            pia_update_rb_unknown, pia_update_rb_friend, pia_update_rb_enemy, pia_update_rb_neutral;
    private String pia_update_string_qna,  pia_update_string_result, piaCompany;
    private EditText pia_update_et_time, pia_update_et_company;

    public UpdateDataFragmentPia() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_pia, container, false);

        pia_update_et_id = view.findViewById(R.id.pia_update_et_id);
        pia_update_rg_qna = view.findViewById(R.id.pia_update_rg_qna);
        pia_update_rg_result = view.findViewById(R.id.pia_update_rg_result);
        pia_update_et_time = view.findViewById(R.id.pia_update_et_time);
        pia_update_et_company = view.findViewById(R.id.pia_update_et_company);
        pia_update_rb_question = view.findViewById(R.id.pia_update_rb_question);
        pia_update_rb_answer = view.findViewById(R.id.pia_update_rb_answer);
        pia_update_rb_noanswer= view.findViewById(R.id. pia_update_rb_noanswer);
        pia_update_rb_unknown = view.findViewById(R.id.pia_update_rb_unknown);
        pia_update_rb_friend= view.findViewById(R.id.pia_update_rb_friend);
        pia_update_rb_enemy= view.findViewById(R.id. pia_update_rb_enemy);
        pia_update_btn_save = view.findViewById(R.id.pia_update_btn_save);
//        pia_update_string_qna = pia_update_rb_question.getText().toString().trim();
//        pia_update_string_result = pia_update_rb_unknown.getText().toString().trim();
        pia_update_string_qna = getArguments().getString("piaQna").trim();
        pia_update_string_result = getArguments().getString("piaResult").trim();

        Log.d("qna", pia_update_string_qna);
        Log.d("result", pia_update_string_result);


        //======= 질/응(qna) 라디오박스그룹 받아와서 표시===========
        pia_update_rb_question.setChecked(true);
        pia_update_rb_answer.setChecked(false);
        pia_update_rb_noanswer.setChecked(false);
        if( pia_update_string_qna.matches("질문") ) pia_update_rb_question.setChecked(true);
        if(pia_update_string_qna.matches("응답")) pia_update_rb_answer.setChecked(true);
        if(pia_update_string_qna.matches("불응"))  pia_update_rb_noanswer.setChecked(true);

        //=======  결과 (result) 라디오박스그룹 받아와서 표시===========
        pia_update_rb_unknown.setChecked(true);
        pia_update_rb_friend.setChecked(false);
        pia_update_rb_enemy.setChecked(false);
        pia_update_rb_neutral.setChecked(false);
        if(pia_update_string_result.matches("미식별") ) pia_update_rb_unknown.setChecked(true);
        if(pia_update_string_result.matches("아군") ) pia_update_rb_friend.setChecked(true);
        if(pia_update_string_result.matches("적군") ) pia_update_rb_enemy.setChecked(true);

        pia_update_et_company.requestFocus();

        pia_update_rg_qna.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            //라디오 버튼 상태 변경값을 감지한다.
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i){
                if(i == R.id.pia_update_rb_question){
                    pia_update_string_qna = pia_update_rb_question.getText().toString().trim();//라디오 버튼의 텍스트 값을 string에 담아준것
                } else if(i == R.id.pia_update_rb_answer){
                    pia_update_string_qna = pia_update_rb_answer.getText().toString().trim();//라디오 버튼의 텍스트 값을 string에 담아준것
                } else if(i == R.id.pia_update_rb_noanswer){
                    pia_update_string_qna = pia_update_rb_answer.getText().toString().trim();//라디오 버튼의 텍스트 값을 string에 담아준것
                }
            }
        });
        pia_update_rg_result.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.pia_update_rb_unknown){
                    pia_update_string_result = pia_update_rb_unknown.getText().toString().trim();//라디오 버튼의 텍스트 값을 string에 담아준것
                }else if(checkedId == R.id.pia_update_rb_friend){
                    pia_update_string_result = pia_update_rb_friend.getText().toString().trim();//라디오 버튼의 텍스트 값을 string에 담아준것
                }else if(checkedId == R.id.pia_update_rb_enemy){
                    pia_update_string_result = pia_update_rb_enemy.getText().toString().trim();//라디오 버튼의 텍스트 값을 string에 담아준것
                }
            }
        });

        Log.d("id", String.valueOf(getArguments().getInt("ID")));

        if (getArguments() != null) {
            pia_update_et_id.setText(String.valueOf(getArguments().getInt("ID")));
            pia_update_et_time.setText(getArguments().getString("piaDate"));
            pia_update_et_company.setText(getArguments().getString("piaCompany"));

            pia_update_et_company.requestFocus();
        }

        pia_update_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.pia_update_btn_save:
                        String piaQna = pia_update_string_qna;
                        String piaResult = pia_update_string_result;
                        if (TextUtils.isEmpty(pia_update_et_company.getText())){
                            piaCompany = pia_update_rb_unknown.getText().toString().trim();
                        } else {
                            piaCompany = pia_update_et_company.getText().toString().trim();
                        }
                        int ID = Integer.parseInt(pia_update_et_id.getText().toString());
                        String Date = pia_update_et_time.getText().toString();

                        Person person = new Person();

                        person.setPiaID(ID);
                        person.setPiaQna(piaQna);
                        person.setPiaResult(piaResult);
                        person.setPiaCompany(piaCompany);
                        person.setPiaDate(Date);

                        MapActivity.roomDatabaseClass.personDao().updatePerson(person);

                        MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentPia(), null).commit();
                }
            }
        });

        return view;

    }
}