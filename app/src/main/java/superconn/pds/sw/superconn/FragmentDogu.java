package superconn.pds.sw.superconn;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FragmentDogu extends Fragment {

    private GpsTracker gpsTracker;

    private SwitchCompat dogu_sw;
    private RadioGroup dogu_rg;
    private RadioButton dogu_rb_auto, dogu_rb_manual;
    private Spinner dogu_sp;
    private Button dogu_btn_save;
    private Integer dogu_int;
    private String dogu_string;
    private TextView dogu_tv_setcoordinates;

    ArrayList<String> arrayList;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";
    private boolean switchOnOff;


    public FragmentDogu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dogu_int = 0;
        gpsTracker = new GpsTracker(getActivity());

        final double latitude = gpsTracker.getLatitude();
        final double longitude = gpsTracker.getLongitude();

        View view = inflater.inflate(R.layout.fragment_dogu, container, false);

        final String[] items = {"1", "2", "3", "4"};

        dogu_sw = view.findViewById(R.id.dogu_sw);
        dogu_rg = view.findViewById(R.id.dogu_rg);
        dogu_rb_auto = view.findViewById(R.id.dogu_rb_auto);
        dogu_rb_manual = view.findViewById(R.id.dogu_rb_manual);
        dogu_sp = view.findViewById(R.id.dogu_sp);
        dogu_tv_setcoordinates = view.findViewById(R.id.dogu_tv_setcoordinates);
        dogu_tv_setcoordinates.setText("위도: "+latitude +"\n"+"경도: "+ longitude);

        dogu_btn_save = view.findViewById(R.id.dogu_btn_save);

        dogu_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   dogu_string = dogu_sp.getSelectedItem().toString();
                   dogu_int = Integer.parseInt(dogu_string);
               } else {
                   dogu_int = 0;
               }
            }
        });

        arrayList = new ArrayList<>();
        arrayList.add("250");
        arrayList.add("500");
        arrayList.add("750");
        arrayList.add("1000");

        final ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.unit, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dogu_sp.setAdapter(arrayAdapter);
        dogu_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (dogu_int != 0){
                    dogu_string = dogu_sp.getSelectedItem().toString();
                    dogu_int = Integer.parseInt(dogu_string);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (dogu_int !=0){
                    dogu_string = dogu_sp.getSelectedItem().toString();
                    dogu_int = Integer.parseInt(dogu_string);
                }
            }
        });

        final TextView distance1 = getActivity().findViewById(R.id.distance1);

        dogu_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

                distance1.setText(dogu_int+"");
            }
        });

       int dogu_distance = Integer.parseInt(distance1.getText().toString());

        if (dogu_distance != 0) {
            loadData();
            updateViews();
        }
        return view;
    }

    private void saveData() {

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCH1, dogu_sw.isChecked());

        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switchOnOff = sharedPreferences.getBoolean(SWITCH1,false);
    }

    public void updateViews() {
        dogu_sw.setChecked(switchOnOff);
    }

}