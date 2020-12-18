package superconn.pds.sw.superconn;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class FragmentChook extends Fragment {

    private GpsTracker gpsTracker;
    RadioGroup chook_rg_chook, chook_rg_chook2;
    RadioButton chook_rb_25k,chook_rb_50k,chook_rb_100k,chook_rb_250k,chook_rb_500k,chook_rb_1m;


    public FragmentChook() {
        // Required empty public constructor
    }

    double ratio = 18.0;

    Button chook_btn_save;

    private View decorView;
    private int	uiOption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        gpsTracker = new GpsTracker(getActivity());

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        final GeoPoint point = new GeoPoint(latitude, longitude);

        final View view = inflater.inflate(R.layout.fragment_chook, container, false);
        final MapView map = (MapView) getActivity().findViewById(R.id.mapView);

//        view.setContentView(R.layout.activity_map);

        chook_btn_save = view.findViewById(R.id.chook_btn_save);

        chook_rg_chook = view.findViewById(R.id.chook_rg_chook);
        chook_rb_25k = view.findViewById(R.id.chook_rb_25k);
        chook_rb_50k = view.findViewById(R.id.chook_rb_50k);
        chook_rb_100k = view.findViewById(R.id.chook_rb_100k);
        chook_rb_250k = view.findViewById(R.id.chook_rb_250k);
        chook_rb_500k = view.findViewById(R.id.chook_rb_500k);
        chook_rb_1m = view.findViewById(R.id.chook_rb_1m);

        chook_rb_25k.setChecked(Update("CHOOK25"));
        chook_rb_50k.setChecked(Update("CHOOK50"));
        chook_rb_100k.setChecked(Update("CHOOK100"));
        chook_rb_250k.setChecked(Update("CHOOK250"));
        chook_rb_500k.setChecked(Update("CHOOK500"));
        chook_rb_1m.setChecked(Update("CHOOK1M"));

        chook_rb_25k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chook2_isChecked) {
                SaveIntoSharedPrefs("CHOOK25", chook2_isChecked);
            }
        });

        chook_rb_50k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chook2_isChecked) {
                SaveIntoSharedPrefs("CHOOK50", chook2_isChecked);
            }
        });

        chook_rb_100k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chook2_isChecked) {
                SaveIntoSharedPrefs("CHOOK100", chook2_isChecked);
            }
        });

        chook_rb_250k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chook8_isChecked) {
                SaveIntoSharedPrefs("CHOOK250", chook8_isChecked);
            }
        });

        chook_rb_500k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chook35_isChecked) {
                SaveIntoSharedPrefs("CHOOK500", chook35_isChecked);
            }
        });

        chook_rb_1m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chook150_isChecked) {
                SaveIntoSharedPrefs("CHOOK1M", chook150_isChecked);
            }
        });

//        chook_rg_chook.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int i) {
//                if (i == R.id.chook_rb_25k){
//
//                    chook_rg_chook2.setOnCheckedChangeListener(null);
//                    chook_rg_chook2.clearCheck();
//                } else if ( i == R.id.chook_rb_50k) {
//                    chook_rg_chook2.setOnCheckedChangeListener(null);
//                    chook_rg_chook2.clearCheck();
//                } else if ( i == R.id.chook_rb_100k) {
//                    chook_rg_chook2.setOnCheckedChangeListener(null);
//                    chook_rg_chook2.clearCheck();
//                }
//            }
//        });

//        s20기준 3200px 1km ratio: 18.6, 약 16(2^4)센치 > 14.6(18.6-4)이면 1km = 1cm, 100k축척, 적도: ratio = 3.6438561897747253;
//        log2(2.5) = 1.322
//        map.setTileSource(TileSourceFactory.MAPNIK);

        chook_btn_save.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( chook_rb_25k.isChecked()){
                    ratio = 16.6;
                } else  if (chook_rb_50k.isChecked()){
                    ratio = 15.6;
                } else  if (chook_rb_100k.isChecked()){
                    ratio = 14.6;
                } else  if (chook_rb_250k.isChecked()){
                    ratio = 13.278;
                } else if (chook_rb_500k.isChecked()){
                    ratio = 12.278;
                } else if (chook_rb_1m.isChecked()){
                    ratio = 11.278;
                }
                map.getController().setCenter(point);
                String ratioString = Double.toString(ratio);
                Log.d("ratio", ratioString);
                map.getController().setZoom(ratio);
            }
        });
        return view;
    }

    private void SaveIntoSharedPrefs(String key, boolean value) {
        SharedPreferences sp = this.getActivity().getSharedPreferences("CHOOK_RG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private boolean Update(String key) {

        SharedPreferences sp = this.getActivity().getSharedPreferences("CHOOK_RG", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

}