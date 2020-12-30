package superconn.pds.sw.superconn.junmun;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import superconn.pds.sw.superconn.R;

public class JunmunInformalFragment extends Fragment {

    ArrayList<String> arrayList;

    public JunmunInformalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_junmun_informal, container, false);


        //spinner 장착
//        junmun_informal_sp = view.findViewById(R.id.junmun_informal_sp);

        arrayList = new ArrayList<>();
//
//        final ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.priority, android.R.layout.simple_spinner_dropdown_item);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        junmun_informal_sp.setAdapter(arrayAdapter);

        final EditText junmun_informal_et_content = view.findViewById(R.id.junmun_informal_et_content);
        final TextView junmun_informal_tv_count = view.findViewById(R.id.junmun_informal_tv_count);
        final EditText junmun_informal_et_title = view.findViewById(R.id.junmun_informal_et_title);

        //글자 수 세기 (내용)
        junmun_informal_et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = junmun_informal_et_content.getText().toString();
                junmun_informal_tv_count.setText(input.length()+" / 200 최대 글자 수");
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //엔터 클릭시 다음줄이 아닌 키보드 안보이게 설정
        junmun_informal_et_title.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(junmun_informal_et_title.getWindowToken(), 0);    //hide keyboard
                    return true;
                }
                return false;
            }
        });

        junmun_informal_et_content.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(junmun_informal_et_content.getWindowToken(), 0);    //hide keyboard
                    return true;
                }
                return false;
            }
        });

        return view;
    }
}