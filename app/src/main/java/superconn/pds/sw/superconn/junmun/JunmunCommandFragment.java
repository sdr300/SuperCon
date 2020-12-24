package superconn.pds.sw.superconn.junmun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import superconn.pds.sw.superconn.R;


public class JunmunCommandFragment extends Fragment {


    public JunmunCommandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_junmun_command, container, false);

        final TextView junmun_command_et_situ_title = view.findViewById(R.id.junmun_command_tv_situ_title);
        final TextView junmun_command_tv_situ_count = view.findViewById(R.id.junmun_command_tv_situ_count);
        final EditText junmun_command_et_situ_content = view.findViewById(R.id.junmun_command_et_situ_content);

        //글자 수 세기 (내용)
        junmun_command_et_situ_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = junmun_command_et_situ_content.getText().toString();
                junmun_command_tv_situ_count.setText(input.length()+" / 200 최대 글자 수");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        final TextView junmun_command_tv_mission_title = view.findViewById(R.id.junmun_command_tv_mission_title);
        final TextView junmun_command_tv_mission_count = view.findViewById(R.id.junmun_command_tv_mission_count);
        final EditText junmun_command_et_mission_content = view.findViewById(R.id.junmun_command_et_mission_content);

        //글자 수 세기 (내용)
        junmun_command_et_mission_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = junmun_command_et_mission_content.getText().toString();
                junmun_command_tv_mission_count.setText(input.length()+" / 200 최대 글자 수");
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final TextView junmun_command_tv_conduct_title = view.findViewById(R.id.junmun_command_tv_conduct_title);
        final TextView junmun_command_tv_conduct_count = view.findViewById(R.id.junmun_command_tv_conduct_count);
        final EditText junmun_command_et_conduct_content = view.findViewById(R.id.junmun_command_et_conduct_content);

        //글자 수 세기 (내용)
        junmun_command_et_conduct_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = junmun_command_tv_conduct_count.getText().toString();
                junmun_command_tv_conduct_count.setText(input.length()+" / 200 최대 글자 수");
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }
}