package superconn.pds.sw.superconn.globe;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;

import java.util.Collection;

import superconn.pds.sw.superconn.GpsTracker;
import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;

public class GlobeFragment extends Fragment {

    private GpsTracker gpsTracker;
    Button globe_btn_save;
    private RadioButton globe_rb_utm, globe_rb_mgrs, globe_rb_dms;
    private RadioGroup globe_rg;
    private String globe_String, globe_String_map;
    CheckBox globe_checkBox;
    LatLonGridlineOverlay2 grid;;

    public GlobeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        gpsTracker = new GpsTracker(getActivity());

        final double latitude = gpsTracker.getLatitude();
        final double longitude = gpsTracker.getLongitude();

        View view= inflater.inflate(R.layout.fragment_globe, container, false);

        globe_rg = view.findViewById(R.id.globe_rg);
        globe_rb_mgrs = view.findViewById(R.id.globe_rb_mgrs);
        globe_rb_utm = view.findViewById(R.id.globe_rb_utm);
        globe_rb_dms = view.findViewById(R.id.globe_rb_dms);
        globe_btn_save = view.findViewById(R.id.globe_btn_save);

        //좌표계 라디오 버튼 설정 저장(SharedPreferences)
        globe_rb_mgrs.setChecked(Update("GLOBE_MGRS"));
        globe_rb_utm.setChecked(Update("GLOBE_UTM"));
        globe_rb_dms.setChecked(Update("GLOBE_DMS"));

        globe_rb_mgrs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean mgrs_isChecked) {
                SaveIntoSharedPrefs("GLOBE_MGRS", mgrs_isChecked);
            }
        });
        globe_rb_utm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean utm_isChecked) {
                SaveIntoSharedPrefs("GLOBE_UTM", utm_isChecked);
            }
        });
        globe_rb_dms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean dms_isChecked) {
                SaveIntoSharedPrefs("GLOBE_DMS", dms_isChecked);
            }
        });

        //그리고 체크 박스 버튼 설정 저장(SharedPreferences)
        globe_checkBox = view.findViewById(R.id.globe_checkBox);

        globe_checkBox.setChecked(Update("GLOBE_GRID"));

        globe_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean grid_isChecked) {
                SaveIntoSharedPrefs("GLOBE_GRID", grid_isChecked);
            }
        });

        //
        globe_String = ((MapActivity)getActivity()).dms(latitude, longitude);

        if(globe_rb_mgrs.isChecked()){
            globe_String = ((MapActivity)getActivity()).mgrs(latitude, longitude);
            globe_String_map = globe_rb_mgrs.getText().toString().trim();
        } else if(globe_rb_utm.isChecked()){
            globe_String = ((MapActivity)getActivity()).utm(latitude, longitude);
            globe_String_map = globe_rb_utm.getText().toString().trim();
        } else {
            globe_String = ((MapActivity)getActivity()).dms(latitude, longitude);
            globe_String_map = globe_rb_dms.getText().toString().trim();
        }

        TextView globe_tv_mgrs = view.findViewById(R.id.globe_tv_mgrs);
        globe_tv_mgrs.setText(((MapActivity)getActivity()).mgrs(latitude, longitude));

        TextView globe_tv_utm = view.findViewById(R.id.globe_tv_utm);
        globe_tv_utm.setText(((MapActivity)getActivity()).utm(latitude, longitude));

        TextView globe_tv_dms = view.findViewById(R.id.globe_tv_dms);
        globe_tv_dms.setText(((MapActivity)getActivity()).dms(latitude, longitude));

        globe_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            //라디오 버튼 상태 변경값을 감지한다.
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i){
                if(i == R.id.globe_rb_mgrs){
                    globe_String = ((MapActivity)getActivity()).mgrs(latitude, longitude);
                    globe_String_map = globe_rb_mgrs.getText().toString().trim();
                } else if(i == R.id.globe_rb_utm){
                    globe_String = ((MapActivity)getActivity()).utm(latitude, longitude);
                    globe_String_map = globe_rb_utm.getText().toString().trim();
                } else {
                    globe_String = ((MapActivity)getActivity()).dms(latitude, longitude);
                    globe_String_map = globe_rb_dms.getText().toString().trim();
                }
            }
        });

        final MapView map = (MapView) getActivity().findViewById(R.id.mapView);
        grid = new LatLonGridlineOverlay2();

        globe_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("좌표", globe_String);
                TextView addressText =  getActivity().findViewById(R.id.addressText);
                addressText.setText("현재위치 좌표계: "+globe_String_map+"\n"+globe_String);

//                TextView doguAdrress = getActivity().findViewById(R.id.dogu_tv_setcoordinates);
//                doguAdrress.setText("현재위치 좌표계: "+globe_String_map+"\n"+globe_String);

                //그리드 설정

                if (globe_checkBox.isChecked()) {
                    grid.setFontSizeDp((short) 40);
                    grid.setMultiplier(2.0f);
                    grid.setBackgroundColor(Color.TRANSPARENT);
                    grid.setFontColor(Color.BLACK);
                    map.getOverlays().add(1, grid);
                } else  if (!globe_checkBox.isChecked()){
                    try {
                        map.getOverlays().remove(1);
                    } finally {
                    }
                }
            }
        });

        return view;
    }

    private void SaveIntoSharedPrefs(String key, boolean value) {
        SharedPreferences sp = this.getActivity().getSharedPreferences("GLOBE_RG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private boolean Update(String key) {
        SharedPreferences sp = this.getActivity().getSharedPreferences("GLOBE_RG", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

}

