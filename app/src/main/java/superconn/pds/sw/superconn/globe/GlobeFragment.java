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

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;

import java.util.Collection;
import java.util.Map;

import icops.framework.common.Coord;
import superconn.pds.sw.superconn.GpsTracker;
import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;
import superconn.pds.sw.superconn.coord.CoordDEG;
import superconn.pds.sw.superconn.coord.CoordGARS;

public class GlobeFragment extends Fragment {

    private GpsTracker gpsTracker;
    Button globe_btn_save;
    private RadioButton globe_rb_utm, globe_rb_mgrs, globe_rb_gars, globe_rb_dms;
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
        globe_rb_gars = view.findViewById(R.id.globe_rb_gars);
        globe_btn_save = view.findViewById(R.id.globe_btn_save);

        //좌표계 라디오 버튼 설정 저장(SharedPreferences)
        globe_rb_mgrs.setChecked(Update("GLOBE_MGRS"));
        globe_rb_utm.setChecked(Update("GLOBE_UTM"));
        globe_rb_gars.setChecked(Update("GLOBE_GARS"));
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
        globe_rb_gars.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean gars_isChecked) {
                SaveIntoSharedPrefs("GLOBE_GARS", gars_isChecked);
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
        } else if(globe_rb_gars.isChecked()){
            globe_String = (getGARS(latitude, longitude));
            globe_String_map = globe_rb_gars.getText().toString().trim();
        } else {
            globe_String = ((MapActivity)getActivity()).dms(latitude, longitude);
            globe_String_map = globe_rb_dms.getText().toString().trim();
        }

        TextView globe_tv_mgrs = view.findViewById(R.id.globe_tv_mgrs);
        globe_tv_mgrs.setText(((MapActivity)getActivity()).mgrs(latitude, longitude));
        TextView globe_tv_utm = view.findViewById(R.id.globe_tv_utm);
        globe_tv_utm.setText(((MapActivity)getActivity()).utm(latitude, longitude));
        TextView globe_tv_gars = view.findViewById(R.id.globe_tv_gars);
        globe_tv_gars.setText(getGARS(latitude, longitude));
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
                } else if (i == R.id.globe_rb_gars) {
                    globe_String = getGARS(latitude, longitude);
                    globe_String_map = globe_rb_gars.getText().toString().trim();
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
                    try {
                        map.getOverlays().remove(grid);
                    } catch(IndexOutOfBoundsException e) {
                        System.out.println(e);
                    }
                    grid.setFontSizeDp((short) 40);
                    grid.setMultiplier(2.0f);
                    grid.setBackgroundColor(Color.TRANSPARENT);
                    grid.setFontColor(Color.BLACK);
                    map.getOverlays().add(3, grid);
                } else  if (!globe_checkBox.isChecked()){
                    //ArrayIndexOutOfBoundsException 을 try catch로 잡아냄
                    try {
                        map.getOverlays().remove(grid);
                    } catch(IndexOutOfBoundsException e) {
                        System.out.println(e);
                    }
                }
                map.setTileSource(TileSourceFactory.MAPNIK);
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

    //===================gars

    public  String getLongitudeBand(double longitude) {
        Double longBand = longitude + 180;
        // Normalize to 0.0 <= longBand < 360
        while (longBand < 0) {
            longBand = longBand + 360;
        }
        while (longBand > 360) {
            longBand = longBand - 360;
        }
        longBand = Math.floor(longBand * 2.0);
        Integer intLongBand = longBand.intValue() + 1; // Start at 001, not 000
        String strLongBand = intLongBand.toString();
        // Left pad the string with 0's so X becomes 00X
        while (strLongBand.length() < 3) {
            strLongBand = "0" + strLongBand;
        }
        return strLongBand;
    }//getLongitudeBand

    /**
     * Get the GARS 2 letter latitude band descriptor for a given latitude
     * (-90..90)
     * <p>
     * The fourth and fifth characters of a GARS code represent 30-minute
     * latitudinal bands and are numbered from AA through QZ, starting at -90
     * through 90.
     * <p>
     * @param latitude as double in degrees decimal
     * @return String representation of the 2 character latitudinal band
     */
    public  String getLatitudeBand(double latitude) {
        char[] letterArray = "ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
        Double offset = latitude + 90;
        // Normalize offset to 0< offset <90
        while (offset < 0) {
            offset = offset + 180;
        }
        while (offset > 180) {
            offset = offset - 180;
        }
        offset = Math.floor(offset * 2.0);
        Double firstOffest = offset / letterArray.length;
        Double secondOffest = offset % letterArray.length;
        StringBuilder sb = new StringBuilder();
        sb.append(letterArray[firstOffest.intValue()]).append(letterArray[secondOffest.intValue()]);
        return sb.toString();
    }//getLatitudeBand

    /**
     * Returns the GARS 15 minute quadrant cell identifier for a latitude,
     * longitude pair.This identifier is the 6th character of the GARS
     * code.<br>Each 30-minute cell is divided into four 15-minute by 15-minute
     * quadrants. The quadrants are numbered sequentially, from west to east,
     * starting with the northernmost band. Specifically, the northwest quadrant
     * is “1”; the northeast quadrant is “2”; the southwest quadrant is “3”; the
     * southeast quadrant is “4”.
     * <p>
     *
     * @param latitude as double in degrees decimal
     * @param longitude as double in degrees decimal
     * @return String of the quadrant identifier (1..4), returns 0 if lat/lon is
     * invalid
     */
    public  String getQudarant(double latitude, double longitude) {
        Double latBand = (Math.floor((latitude + 90.0) * 4.0) % 2.0);
        Double longBand = (Math.floor((longitude + 180.0) * 4.0) % 2.0);
        // Return "0" if error occurs
        if (latBand < 0 || latBand > 1) {
            return "0";
        }
        if (longBand < 0 || longBand > 1) {
            return "0";
        }
        // Otherwise return the quadrant
        if (latBand == 0.0 && longBand == 0.0) {
            return "3";
        } else if (latBand == 1.0 && longBand == 0.0) {
            return "1";
        } else if (latBand == 1.0 && longBand == 1.0) {
            return "2";
        } else if (latBand == 0.0 && longBand == 1.0) {
            return "4";
        }
        return "0";
    }//getQudarant

    /**
     * Returns the GARS 5 minute keypad cell identifier for a latitude,
     * longitude pair. This is the seventh character of the GARS code.<br>Each
     * 15-minute quadrant is divided into nine 5-minute by 5-minute areas. The
     * areas are numbered sequentially, from west to east, starting with the
     * northernmost band. The graphical representation of a 15-minute quadrant
     * with numbered 5-minute by 5-minute areas resembles a telephone keypad.
     * <p>
     * This code was ported from the Geotrans 3.5 CCP implementation:
     * http://earth-info.nga.mil/GandG/geotrans/
     * <p>
     * @param latitude as double in degrees decimal
     * @param longitude as double in degrees decimal
     * @return String of the 5x5 identifier (1..9), returns 0 if lat/lon is
     * invalid
     */
    public  String getKeyPad(double latitude, double longitude) {
        // Check for valid lat/lon range
        if (latitude < -90 || latitude > 90) {
            return "0";
        }
        if (longitude < -180 || longitude > 180) {
            return "0";
        }

        /* Convert longitude and latitude from degrees to minutes */
        /* longitude assumed in -180 <= long < +180 range */
        double long_minutes = (longitude + 180) * 60.0;
        double lat_minutes = (latitude + 90) * 60.0;
        /* now we have a positive number of minutes */

        /* Find 30-min cell indices 0-719 and 0-359 */
        long horiz_index_30 = (long) (long_minutes / 30.0);
        long vert_index_30 = (long) (lat_minutes / 30.0);

        /* Compute remainders 0 <= x < 30.0 */
        double long_remainder = long_minutes - (horiz_index_30) * 30.0;
        double lat_remainder = lat_minutes - (vert_index_30) * 30.0;

        /* Find 15-min cell indices 0 or 1 */
        long horiz_index_15 = (long) (long_remainder / 15.0);
        long vert_index_15 = (long) (lat_remainder / 15.0);

        /* Compute remainders 0 <= x < 15.0 */
        long_remainder = long_remainder - (horiz_index_15) * 15.0;
        lat_remainder = lat_remainder - (vert_index_15) * 15.0;

        /* Find 5-min cell indices 0, 1, or 2 */
        long horiz_index_5 = (long) (long_remainder / 5.0);
        long vert_index_5 = (long) (lat_remainder / 5.0);

        String[][] _5_minute_array = {{"7", "4", "1"}, {"8", "5", "2"}, {"9", "6", "3"}};

        String keypad = _5_minute_array[(int) horiz_index_5][(int) vert_index_5];

        return keypad;

    }//getKeyPad

    /**
     * Returns a GARS coordinate string for a point (latitude, longitude)
     * <p>
     * The precision parameter indicates if a 30x30min, 15x15min, or 5x5min GARS
     * code is to be returned.
     * <p>
     * Valid precision values are ["30","15","5"]
     * <p>
     *
     * @param latitude as double in degrees decimal
     * @param longitude as double in degrees decimal
     * @param precision as String representing the precision of the GARS string
     *
     * @return String representation of the GARS identifier, returns 0 if
     * lat/lon is invalid
     */
    public  String getGARS(double latitude, double longitude, String precision) {
        /* North pole is an exception, read over and down */
        if (latitude == 90.0) {
            latitude = 89.99999999999;
        }
        // Check for valid lat/lon range
        if (latitude < -90 || latitude > 90) {
            return "0";
        }
        if (longitude < -180 || longitude > 180) {
            return "0";
        }
        // Get the longitude band ==============================================
        Double longBand = longitude + 180;
        // Normalize to 0.0 <= longBand < 360
        while (longBand < 0) {
            longBand = longBand + 360;
        }
        while (longBand > 360) {
            longBand = longBand - 360;
        }
        longBand = Math.floor(longBand * 2.0);
        Integer intLongBand = longBand.intValue() + 1; // Start at 001, not 000
        String strLongBand = intLongBand.toString();
        // Left pad the string with 0's so X becomes 00X
        while (strLongBand.length() < 3) {
            strLongBand = "0" + strLongBand;
        }

        // Get the latitude band ===============================================
        char[] letterArray = "ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
        Double offset = latitude + 90;
        // Normalize offset to 0< offset <90
        while (offset < 0) {
            offset = offset + 180;
        }
        while (offset > 180) {
            offset = offset - 180;
        }
        offset = Math.floor(offset * 2.0);
        Double firstOffest = offset / letterArray.length;
        Double secondOffest = offset % letterArray.length;
        StringBuilder sb = new StringBuilder();
        String strLatBand = sb.append(letterArray[firstOffest.intValue()]).append(letterArray[secondOffest.intValue()]).toString();

        // Id the precision is 30x30min then return the longitudinal and latitudinal bands
        if (precision.contains("30")) {
            return strLongBand + strLatBand;
        }

        // Get the quadrant ====================================================
        Double latBand = (Math.floor((latitude + 90.0) * 4.0) % 2.0);
        longBand = (Math.floor((longitude + 180.0) * 4.0) % 2.0);
        String quadrant = "0";
        // return "0" if error occurs
        if (latBand < 0 || latBand > 1) {
            return "0";
        }
        if (longBand < 0 || longBand > 1) {
            return "0";
        }
        // Otherwise get the quadrant
        if (latBand == 0.0 && longBand == 0.0) {
            quadrant = "3";
        } else if (latBand == 1.0 && longBand == 0.0) {
            quadrant = "1";
        } else if (latBand == 1.0 && longBand == 1.0) {
            quadrant = "2";
        } else if (latBand == 0.0 && longBand == 1.0) {
            quadrant = "4";
        }

        // Id the precision is 15x15min then return the longitudinal and latitudinal bands
        // plus the quadrant
        if (precision.contains("15")) {
            return strLongBand + strLatBand + quadrant;
        }

        // Get the keypad ======================================================
        /* Convert longitude and latitude from degrees to minutes */
        /* longitude assumed in -180 <= long < +180 range */
        double long_minutes = (longitude + 180) * 60.0;
        double lat_minutes = (latitude + 90) * 60.0;
        /* now we have a positive number of minutes */

        /* Find 30-min cell indices 0-719 and 0-359 */
        long horiz_index_30 = (long) (long_minutes / 30.0);
        long vert_index_30 = (long) (lat_minutes / 30.0);

        /* Compute remainders 0 <= x < 30.0 */
        double long_remainder = long_minutes - (horiz_index_30) * 30.0;
        double lat_remainder = lat_minutes - (vert_index_30) * 30.0;

        /* Find 15-min cell indices 0 or 1 */
        long horiz_index_15 = (long) (long_remainder / 15.0);
        long vert_index_15 = (long) (lat_remainder / 15.0);

        /* Compute remainders 0 <= x < 15.0 */
        long_remainder = long_remainder - (horiz_index_15) * 15.0;
        lat_remainder = lat_remainder - (vert_index_15) * 15.0;

        /* Find 5-min cell indices 0, 1, or 2 */
        long horiz_index_5 = (long) (long_remainder / 5.0);
        long vert_index_5 = (long) (lat_remainder / 5.0);

        String[][] _5_minute_array = {{"7", "4", "1"}, {"8", "5", "2"}, {"9", "6", "3"}};

        String keypad = _5_minute_array[(int) horiz_index_5][(int) vert_index_5];

        return strLongBand + strLatBand + quadrant + keypad;
    }//getGARS

    /**
     * Returns a GARS 5x5 minute coordinate string for a point (latitude,
     * longitude)
     * <p>
     *
     * @param latitude as double in degrees decimal
     * @param longitude as double in degrees decimal
     *
     * @return String representation of the GARS identifier, returns 0 if
     * lat/lon is invalid
     */
    public  String getGARS(double latitude, double longitude) {
        return getGARS(latitude, longitude, "5");
    }//getGARS





}

