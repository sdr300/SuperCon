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

import org.osmdroid.views.MapView;

public class FragmentChook extends Fragment {

    RadioGroup chook_rg_map, chook_rg_chook;
    RadioButton chook_rb_cadrg, chook_rb_geotiff, chook_rb_2k, chook_rb_8k, chook_rb_150k, chook_rb_35k;


    public FragmentChook() {
        // Required empty public constructor
    }


    double ratio = 18.0;

    Button chook_btn_save;

    private View 	decorView;
    private int	uiOption;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_chook, container, false);
        final MapView map = (MapView) getActivity().findViewById(R.id.mapView);


//        view.setContentView(R.layout.activity_map);

        chook_btn_save = view.findViewById(R.id.chook_btn_save);

        chook_rg_map = view.findViewById(R.id.chook_rg_map);
        chook_rg_chook = view.findViewById(R.id.chook_rg_chook);
        chook_rb_cadrg = view.findViewById(R.id.chook_rb_cadrg);
        chook_rb_geotiff = view.findViewById(R.id.chook_rb_geotiff);
        chook_rb_2k = view.findViewById(R.id.chook_rb_2k);
        chook_rb_8k = view.findViewById(R.id.chook_rb_8k);
        chook_rb_35k = view.findViewById(R.id.chook_rb_35k);
        chook_rb_150k = view.findViewById(R.id.chook_rb_150k);

        chook_rb_2k.setChecked(Update("CHOOK2"));
        chook_rb_8k.setChecked(Update("CHOOK8"));
        chook_rb_35k.setChecked(Update("CHOOK35"));
        chook_rb_150k.setChecked(Update("CHOOK150"));

        chook_rb_2k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chook2_isChecked) {
                SaveIntoSharedPrefs("CHOOK2", chook2_isChecked);
            }
        });

        chook_rb_8k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chook8_isChecked) {
                SaveIntoSharedPrefs("CHOOK8", chook8_isChecked);
            }
        });

        chook_rb_35k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chook35_isChecked) {
                SaveIntoSharedPrefs("CHOOK35", chook35_isChecked);
            }
        });

        chook_rb_150k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chook150_isChecked) {
                SaveIntoSharedPrefs("CHOOK150", chook150_isChecked);
            }
        });

        chook_rg_chook.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                if (i == R.id.chook_rb_2k){
                    ratio = 18.0;
                } else  if (i == R.id.chook_rb_8k){
                    ratio = 16.0;
                } else if (i == R.id.chook_rb_35k){
                    ratio = 14.0;
                } else if (i == R.id.chook_rb_150k){
                    ratio = 3.6438561897747253;
                }
            }
        });


//        map.setTileSource(TileSourceFactory.MAPNIK);

        chook_btn_save.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
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