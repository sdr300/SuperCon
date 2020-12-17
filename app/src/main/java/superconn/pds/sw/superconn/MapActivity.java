package superconn.pds.sw.superconn;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarFile;

import icops.framework.common.CAppContext;
import icops.warsym.inf.IWarSymFactory;
import superconn.pds.sw.superconn.ICOPS.DemoMainView;
import superconn.pds.sw.superconn.comm.LIFFReceiver;
import superconn.pds.sw.superconn.coord.CoordDMS;
import superconn.pds.sw.superconn.coord.CoordUTM;
import superconn.pds.sw.superconn.coord.CoordinateManager;
import superconn.pds.sw.superconn.coord.coordMgrs;

public class MapActivity extends AppCompatActivity {

    public static Context maincontext;
    public static MapActivity mainActivity;
    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    public CAppContext mAppCxt;
    private MapView map = null;
    IWarSymFactory mSymFac = null;
    JarFile mWarsymJar = null;
    static DemoMainView mMainView = null;
    boolean mLoading = false;
    static FragmentManager fragmentManager;
    static RoomDatabaseClass roomDatabaseClass;
    private int fragmentBoolean = 0;
    private long backKeyPressedTime = 0; // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private Integer dogu_int;
    private String dogu_string;
    TextView distance1;
    int poweri = 0;
    public String dogu_addText;
    private SwitchCompat dogu_sw;
    private RadioButton globe_rb_dms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        //줌
        map.getController().setZoom(18.0);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET
        });
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);

        //gps로 위치 가져옴
        gpsTracker = new GpsTracker(MapActivity.this);

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        //메인화면에 위치표시
        TextView addressText;
        addressText =  findViewById(R.id.addressText);
        addressText.setText("현재위치 좌표계: 경위도(DMS)\n"+dms(latitude, longitude));

        CompassOverlay compassOverlay = new CompassOverlay(this, map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);

        //========starterMarker
        GeoPoint point = new GeoPoint(latitude, longitude);

        final Marker startMarker = new Marker(map);
        startMarker.setIcon(this.getResources().getDrawable(R.drawable.map_marker2));
        startMarker.setPosition(point);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        map.getOverlays().add(0, startMarker);
//        map.getOverlays().remove(startMarker);
        startMarker.setInfoWindow(null);
        startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                return false;
            }
        });

        map.getController().setCenter(point);

        //거리환 테스트
        final List<GeoPoint> geoPoints = new ArrayList<>();
        //add your points here
        Polyline line = new Polyline();   //see note below!
        line.setPoints(geoPoints);
        map.getOverlayManager().add(line);
        //==============측정도구 버튼용 주소
        final String addText = addressText.getText().toString();
        dogu_addText = addText;
        //==================클릭시 좌표를 가져오는 리시버 + 거리환
        final MapEventsReceiver mReceive = new MapEventsReceiver(){
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                Toast.makeText(getBaseContext(),"선택 좌표 위치:" +"\n"+p.getLatitude() + "\n"+p.getLongitude(), Toast.LENGTH_LONG).show();
                distance1 = (TextView) findViewById(R.id.distance1);
                dogu_string = distance1.getText().toString();
                dogu_int = (Integer.parseInt(dogu_string))/250;
                int i=0;
                drawMarker(p);
                CompassOverlay compassOverlay = new CompassOverlay(MapActivity.this, map);
                compassOverlay.enableCompass();
                map.getOverlays().add(1, compassOverlay);

                // ======================== 거리환 십자 직선
                List<GeoPoint> geoPoint250lat = new ArrayList<>();

                geoPoint250lat.add(new GeoPoint(p.getLatitude() - 0.002245*dogu_int*4, p.getLongitude()));
                geoPoint250lat.add(new GeoPoint(p.getLatitude() + 0.002245*dogu_int*4, p.getLongitude()));

                final Polyline line250lat = new Polyline();

                line250lat.setPoints(geoPoint250lat);
                line250lat.setColor(Color.BLACK);
                line250lat.setWidth(6);

                map.getOverlayManager().add(line250lat);

                List<GeoPoint> geoPoint250lon = new ArrayList<>();

                geoPoint250lon.add(new GeoPoint(p.getLatitude() , p.getLongitude()- 0.00283*dogu_int*4));
                geoPoint250lon.add(new GeoPoint(p.getLatitude() , p.getLongitude()+ 0.00283*dogu_int*4));

                final Polyline line250lon = new Polyline();
                line250lon.setPoints(geoPoint250lon);
                line250lon.setColor(Color.BLACK);
                line250lon.setWidth(6);

                map.getOverlayManager().add(line250lon);

                //거리환 원그리기 (4개)
                final Marker[] dmarkers = new Marker[4];
                GeoPoint[] dGpoints = new GeoPoint[4];
                final Polygon[] dpolygons = new Polygon[4];
                for ( i=0; i<4; i++){
                    dpolygons[i] = new Polygon(map);
                    dpolygons[i].setPoints(Polygon.pointsAsCircle(p, 250.0*dogu_int*(i+1)));
                    dpolygons[i].setFillColor(Color.TRANSPARENT);
                    dpolygons[i].setStrokeColor(Color.BLACK);
                    dpolygons[i].setStrokeWidth(4);
                    if (i<3) {
                        dpolygons[i].setOnClickListener(new Polygon.OnClickListener() {
                            @Override
                            public boolean onClick(Polygon polygon, MapView mapView, GeoPoint eventPos) {
                                return false;
                            }
                        });
                    } else {
                        dpolygons[i].setOnClickListener(new Polygon.OnClickListener() {
                            @Override
                            public boolean onClick(Polygon polygon, MapView mapView, GeoPoint eventPos) {
                                int i =0;
                                for (i=0; i<4 ; i++) {
                                    dmarkers[i].remove(map);
                                    mapView.getOverlays().remove(dpolygons[i]);
                                }
                                mapView.getOverlays().remove(line250lat);
                                mapView.getOverlays().remove(line250lon);
                                return false;
                            }
                        });
                    }
                    map.getOverlays().add(dpolygons[i]);
                }
                //===========================거리환 숫자 표기(단위)
                if(dogu_int>0) {
                    for (i = 0; i < 4; i++) {
                        dGpoints[i] = new GeoPoint(p.getLatitude() + 0.002245 * dogu_int * (i + 1), p.getLongitude());
                        dmarkers[i] = new Marker(map);
                        dmarkers[i].setTextIcon(250 * dogu_int * (i + 1) + "");
                        dmarkers[i].setPosition(dGpoints[i]);
                        dmarkers[i].setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker, MapView mapView) {
                                return false;
                            }
                        });
                        map.getOverlayManager().add(dmarkers[i]);
                    }
                }
                map.invalidate();
                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }

        };
        map.getOverlays().add(new MapEventsOverlay(mReceive));

        map.invalidate();

        //아래 두줄은 풀스크린용(상단바 제거)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //플래그먼트 매니저
        fragmentManager = getSupportFragmentManager();

        //db 생성 (migration 시 데이터 유지가 필요하다면 하단의 fallbackToDestructiveMigration 해제)
        roomDatabaseClass = Room.databaseBuilder(getApplicationContext(),
                RoomDatabaseClass.class, "mydb")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        //시작할때 띄울게 있으면 삽입
        if (findViewById(R.id.fragment_frame) != null) {
            if (savedInstanceState != null) {
                return;
            }
            //여기에 띄움
//            fragmentManager.beginTransaction().add(R.id.fragment_frame, new FragmentZ(), null).commit();
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_frame, new FragmentZ());
        fragmentTransaction.commit();

//        String sdPath = getExternalStorageDirectory().getAbsolutePath();
//        String wsDataPath = sdPath + "/ICOPS/ICOPSaWarsymData.jar";
//        Log.d(this.getClass().getName(), "@@@@@ 주소 :" + wsDataPath);
//        // ICOPS 컴포넌트 초기화
//        new IcopsInitTask().execute(getString(R.string.config_icopsAppContext));
//
//        mMainView = findViewById(R.id.DemoMainView);
//        mMainView.mMain = this;
//
//        // 화면 해상도에 따른 계산을 위해 필요한 환경설정 값을 초기화한다.
//        CGeometricUtil.initContext(getApplicationContext());
//        CGeometricUtil.mDPM *= 1.2f; // 밀리미터에 해당하는 화면 픽셀 개수를 임의로 조정하려면 mDPM 값을 변경한다.
//        GeoPoint startPoint = new GeoPoint(36.63507, 126.41642); // 좌표설정
//
//        map = (MapView) findViewById(R.id.mapView);
//        map.getController().setCenter(startPoint);
//        map.setTileSource(TileSourceFactory.MAPNIK);
//
//        requestPermissionsIfNecessary(new String[]{
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        });

        //======================gps 위치받아오기=========/
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }
    }

    private final String LIFF_IDENTIFICATION_SEND = "LIFF.COMM.RS232.IDENTIFICATION.SEND";
    private final String LIFF_IDENTIFICATION_RECEIVER = "LIFF.COMM.RS232.IDENTIFICATION.RECEIVER";
    superconn.pds.sw.superconn.comm.LIFFReceiver LIFFReceiver;

    @Override
    public void onResume() {
        super.onResume();
        LIFFReceiver = new LIFFReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(LIFF_IDENTIFICATION_SEND);
        intentFilter.addAction(LIFF_IDENTIFICATION_RECEIVER);
        this.registerReceiver(LIFFReceiver,intentFilter);
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }

        if ( requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSIONS.length) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {
                //위치 값을 가져올 수 있음
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(MapActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(MapActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void showPower(int battery){
        Toast toast = Toast.makeText(MapActivity.this, "배터리가 '"+battery+"%' 남아있습니다", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
//
//    public void icopsTest(View view) {
//        GeoPoint geoPoint = new GeoPoint(36.63507, 126.41642); // 좌표설정
//        Projection pj = map.getProjection();
//        Point point = pj.toPixels(geoPoint, null);
//        Geometry.Point.Array gPoints = new Geometry.Point.Array();
//        gPoints.add(point.x, point.y);
//        mMainView.drawSingle("SFG*UCF---*****", 0, gPoints);
//
//        mMainView.invalidate();
//    }
//
//    // ICOPS 초기화를 수행하는 Task
//    private class IcopsInitTask extends AsyncTask<String, String, CAppContext> {
//
//        @Override
//        protected CAppContext doInBackground(String... args) {
//            boolean res;
//            CAppContext appCxt = new CAppContext();
//            Application app = getApplication();
//            AssetManager amg = app.getAssets();
//
//            // open resource
//            InputStream is;
//            try {
//                is = amg.open(args[0]);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                return null;
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//
//            res = appCxt.initialize("icops.framework.common.CAppContextDataAgentXml", is, app, amg);
//            if (!res) {
//                publishProgress("ICOPS 초기화 실패");
//                return null;
//            }
//
//            // 군대부호 JAR 파일 열기
//            String sdPath = getExternalStorageDirectory().getAbsolutePath();
//            String wsDataPath = sdPath + "/ICOPS/ICOPSaWarsymData.jar";
//            Log.d(this.getClass().getName(), "@@@@@ 주소 :" + wsDataPath);
//
//            try {
//                mWarsymJar = new JarFile(wsDataPath);
//            } catch (IOException e) {
//                e.printStackTrace();
//                publishProgress("군대부호자료 열기 실패 " + wsDataPath);
//                mAppCxt = appCxt;
//                return appCxt;
//            }
//
//            BasicReturn.TypeObject retObj = new BasicReturn.TypeObject();
//            res = appCxt.getStockObject("icops.warsym.inf.IWarSymFactory", retObj);
//            if (!res || retObj.value == null) {
//                publishProgress("군대부호 관리 객체 생성 실패");
//                mAppCxt = appCxt;
//                return appCxt;
//            }
//
//            try {
//                IWarSymFactory symFac = (IWarSymFactory) retObj.value;
//                res = symFac.initialize(appCxt, mWarsymJar);
//                if (res) {
//                    mSymFac = symFac;
//                } else {
//                    publishProgress("군대부호 관리 객체 초기화 실패");
//                }
//            } catch (ClassCastException e) {
//                publishProgress("군대부호 관리 객체 초기화 오류");
//            }
//
//            // 군대부호 도시에 사용할 폰트를 설정한다.
//            // 설정하지 않으면 기본으로 <Arial Unicode MS>를 사용한다.
//            //WarSym.FontFamily = "궁서체";
//
//            mAppCxt = appCxt;
//            return appCxt;
//        }
//
//        @Override
//        protected void onPostExecute(CAppContext result) {
//            mLoading = false;
//            onPostInitICOPS(result);
//        }
//
//        @Override
//        protected void onProgressUpdate(String... prgs) {
//        }
//    }
//
//    public void onPostInitICOPS(CAppContext appCxt) {
//        if (appCxt == null) {
//            return;
//        }
//
//        //mMainView.setVisibility(View.VISIBLE);
//    }

    // ===============================버튼 이벤트 관리
    public void onButtonClick(View view) {
        if (view.getId() == R.id.btn_back){
            fr = new FragmentZ();
            fm.popBackStack();
            fragmentBoolean = 0;
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_frame, fr);
            fragmentTransaction.commit();
        } else if (view.getId() == R.id.btn_back_super){
            super.onBackPressed();
        } else if (view.getId() == R.id.pia_update_btn_savecancel ) {
            Toast.makeText(MapActivity.this, "수정 취소", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        } else if (view.getId() == R.id.pia_add_btn_savecancel ) {
            Toast.makeText(MapActivity.this, "입력 취소", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        } else if (view.getId() == R.id.btn_savecancel ) {
            Toast.makeText(MapActivity.this, "취소", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.btnDeleteAll ) {
            DeleteAllPia();
        } else if (view.getId() == R.id.btnDeleteAllBuho ) {
            DeleteAllBuho();

            //========================MapActivity=============
        } else if (view.getId() == R.id.ibt_pia) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_buho) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_location) {
            //====================내 위치===========================
            //gps로 위치 잡아옴

            MapView mapview = (MapView) findViewById(R.id.mapView);

            Marker startMarker = new Marker(map);

            map.getOverlays().remove(0);

            startMarker.setIcon(this.getResources().getDrawable(R.drawable.map_marker2));

            gpsTracker = new GpsTracker(MapActivity.this);

            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            String address = getCurrentAddress(latitude, longitude);

            map.getController().setZoom(18.0);
            CompassOverlay compassOverlay = new CompassOverlay(MapActivity.this, map);
            compassOverlay.enableCompass();
            map.getOverlays().add(compassOverlay);

            GeoPoint point = new GeoPoint(latitude, longitude);

            mapview.invalidate();

            startMarker.setPosition(point);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    return false;
                }
            });

            mapview.getOverlays().add(0, startMarker);

            mapview.getController().setCenter(point);

            FragmentLocation fragmentLocation = new FragmentLocation();
            Bundle bundle = new Bundle();

            bundle.putString("send", address );
            fragmentLocation.setArguments(bundle);

//                Toast.makeText(MapActivity.this, "현재위치 라디오 버튼,\n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_SHORT).show();
            MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragmentLocation).commit();
        } else if (view.getId() == R.id.ibt_situ ) {
            //====================상황도=========================

            map.getController().setZoom(17.0);
            CompassOverlay compassOverlay = new CompassOverlay(MapActivity.this, map);
            compassOverlay.enableCompass();
            map.getOverlays().add(compassOverlay);

            GeoPoint friendPoint1 = new GeoPoint(37.4886, 127.1221);
            GeoPoint friendPoint2 = new GeoPoint(37.4882, 127.1240);
            GeoPoint enemyPoint1 = new GeoPoint(37.4873, 127.1227);
            GeoPoint centerPoint1 = new GeoPoint(37.4862, 127.1266);

            MapView mapview = (MapView) findViewById(R.id.mapView);

            Marker friendMarker1 = new Marker(map);
            Marker friendMarker2 = new Marker(map);
            Marker enemyMarker = new Marker(map);

            mapview.invalidate();

            friendMarker1.setPosition(friendPoint1);
            friendMarker1.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

            friendMarker2.setPosition(friendPoint2);
            friendMarker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

            enemyMarker.setPosition(enemyPoint1);
            enemyMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

            mapview.getOverlays().add(friendMarker1);
            mapview.getOverlays().add(friendMarker2);
            mapview.getOverlays().add(enemyMarker);

            friendMarker1.setIcon(ContextCompat.getDrawable(getApplication(), R.drawable.our4));
            friendMarker2.setIcon(ContextCompat.getDrawable(getApplication(), R.drawable.our4));
            enemyMarker.setIcon(ContextCompat.getDrawable(getApplication(), R.drawable.enemy4));

            friendMarker1.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    return false;
                }
            });
            friendMarker2.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    return false;
                }
            });
           enemyMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    return false;
                }
            });

            mapview.getController().setCenter(centerPoint1);
        } else if (view.getId() == R.id.ibt_junmun ) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_check ) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_setting ) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_gps ) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_power ) {
            //                sendTest();
            switchFragmentPower();
        } else if (view.getId() == R.id.ibt_dogu ) {
            FragmentDogu fragmentDogu = new FragmentDogu();
            Bundle bundle = new Bundle();
            bundle.putString("addText", dogu_addText);
            fragmentDogu.setArguments(bundle);
            switchAll(view);
        } else if (view.getId() == R.id.ibt_chook ) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_globe) {
            switchAll(view);
        }
    }

    //===================== 전체 삭제 =============
    void DeleteAllPia() { AlertDialog.Builder showDialogAll =
            new AlertDialog.Builder(MapActivity.this);
            showDialogAll.setTitle("모든 피아식별 데이터를 삭제하시겠습니까?");
            showDialogAll.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialogInterface, int i) {

                    List<Person> personList = new ArrayList<>();
                    MapActivity.roomDatabaseClass.personDao().reset2();
                    personList.clear();
                    Toast.makeText(getApplicationContext(),"Pressed Reset",
                            Toast.LENGTH_SHORT).show();
                    MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentPia(), null).commit();

                }
            }) .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(),"Pressed Cancle",
                            Toast.LENGTH_SHORT).show();
                    MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentPia(), null).commit();
                }
            });
        AlertDialog msgDlg = showDialogAll.create();
        msgDlg.show(); }

    void DeleteAllBuho() { AlertDialog.Builder showDialogAll =
            new AlertDialog.Builder(MapActivity.this);
        showDialogAll.setTitle("모든 군대부호 모의 데이터를 삭제하시겠습니까?");
        showDialogAll.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {

                List<Buho> buhoList = new ArrayList<>();
                MapActivity.roomDatabaseClass.buhoDao().resetBuho2();
                buhoList.clear();
                Toast.makeText(getApplicationContext(),"Pressed Reset",
                        Toast.LENGTH_SHORT).show();
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentBuho(), null).commit();

            }
        }) .setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Pressed Cancle",
                        Toast.LENGTH_SHORT).show();
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentPia(), null).commit();
            }
        });
        AlertDialog msgDlg = showDialogAll.create();
        msgDlg.show(); }

    //========================switchFragment(power)===============
    //getSupportFragmentManager().findFragmentById(R.id.fragment_frame)==null
    Fragment fr;
    FragmentManager fm = getSupportFragmentManager();

    public void switchFragmentPower() {
        if ( poweri%3 == 0) {
          findViewById(R.id.buttonour).setVisibility(View.VISIBLE);
            poweri = poweri+1;
        } else  if ( poweri%3 == 1 ) {
            findViewById(R.id.buttonenemy).setVisibility(View.VISIBLE);
            poweri = poweri+1;
        } else  if ( poweri%3 == 2 ) {
            findViewById(R.id.buttonour).setVisibility(View.INVISIBLE);
            findViewById(R.id.buttonenemy).setVisibility(View.INVISIBLE);
            poweri = poweri+1;
        }
        Toast toast = Toast.makeText(MapActivity.this, "배터리"+poweri, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void switchAll(View view) {
        if (fragmentBoolean == 0) {
            if (view.getId() ==R.id.ibt_pia) {
                fr = new FragmentPia();
            } else if (view.getId() ==R.id.ibt_buho) {
                fr = new FragmentBuho();
            } else if (view.getId() ==R.id.ibt_junmun) {
                fr = new FragmentJunmun();
            } else if (view.getId() ==R.id.ibt_check) {
                fr = new FragmentCheck();
            } else if (view.getId() ==R.id.ibt_setting) {
                fr = new FragmentSetting();
            } else if (view.getId() ==R.id.ibt_gps) {
                fr = new FragmentGPS();
            } else if (view.getId() ==R.id.ibt_dogu) {
                fr = new FragmentDogu();
            } else if (view.getId() ==R.id.ibt_chook) {
                fr = new FragmentChook();
            } else if (view.getId() ==R.id.ibt_globe) {
                fr = new FragmentGlobe();
            }
            fragmentBoolean = 1;
        } else {
            fr = new FragmentZ() ;
            fm.popBackStack();
            fragmentBoolean = 0;
        }
         FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, fr);
        fragmentTransaction.commit();
    }

    //========================================뒤로 버튼 관리
    @Override
    public void onBackPressed() {
        Toast toast;
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }
    //=====ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.

    void checkRunTimePermission(){
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MapActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MapActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MapActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    public String getCurrentAddress( double latitude, double longitude) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /* 좌표변환 관련 소스 */
    public void utm() {
        CoordDMS dms = new CoordDMS();
        CoordDMS dms2 = new CoordDMS();
        //dms.convertFromDeg(otherlongitude, otherlatitude);
        //dms2.convertFromDeg(mylongitude, mylatitude);

        coordMgrs mgrs = new coordMgrs();
        coordMgrs mgrs2 = new coordMgrs();
        mgrs = CoordinateManager.dmsToMgrs(dms);
        mgrs2 = CoordinateManager.dmsToMgrs(dms2);
        CoordUTM utm = new CoordUTM();
        CoordUTM utm2 = new CoordUTM();
        utm = CoordinateManager.mgrsToUtm(mgrs.getMGRS());
        utm2 = CoordinateManager.mgrsToUtm(mgrs2.getMGRS());
        //otherCoord.setText(utm.degToUtm( otherlatitude,otherlongitude));
        //myCoord.setText(utm2.degToUtm(mylatitude,mylongitude ));
    }

    public void mgrs() {
        CoordDMS dms = new CoordDMS();
        CoordDMS dms2 = new CoordDMS();
        //dms.convertFromDeg(otherlongitude, otherlatitude);
        //dms2.convertFromDeg(mylongitude, mylatitude);

        coordMgrs mgrs = new coordMgrs();
        coordMgrs mgrs2 = new coordMgrs();
        mgrs = CoordinateManager.dmsToMgrs(dms);
        mgrs2 = CoordinateManager.dmsToMgrs(dms2);

        StringBuilder str = new StringBuilder(mgrs.getMGRS());
        StringBuilder str2 = new StringBuilder(mgrs2.getMGRS());
        str.insert(5, " 동거 ");
        str.insert(14, " 북거 ");
        str2.insert(5, " 동거 ");
        str2.insert(14, " 북거 ");
    }

    //gps로 가져온 degree를 dms로 변환

    public String utm(double lat, double lon) {
        CoordDMS dms = new CoordDMS();
        dms.convertFromDeg(lon,lat);

        CoordUTM utm ;
        coordMgrs mgrs ;
        mgrs = CoordinateManager.dmsToMgrs(dms);
        utm = CoordinateManager.mgrsToUtm(mgrs.getMGRS());
        String utmStr = utm.degToUtm(lat,lon);
        return utmStr;
    }

    public String mgrs(double lat, double lon) {
        CoordDMS dms = new CoordDMS();
        dms.convertFromDeg(lon, lat);

        coordMgrs mgrs = new coordMgrs();
        mgrs = CoordinateManager.dmsToMgrs(dms);

        StringBuilder str = new StringBuilder(mgrs.getMGRS());
        str.insert(5, " 동거 ");
        str.insert(14, " 북거 ");

        return  str.toString();
    }

    public String dms(double lat, double lon) {
        int latd, lond;
        double ladm, lads, lonm, lons;

         latd = (int)lat;
         lond = (int)lon;

         ladm = (int)((lat-latd)*60);
         lonm = (int)((lon-lond)*60);

         String lado = String.format("%.0f", ladm);
         String lono = String.format("%.0f", lonm);

         lads = (lat - latd - ladm/60)*3600;
         lons = (lon - lond - lonm/60)*3600;

         String ladf = String.format("%.1f", lads);
         String lonf = String.format("%.1f", lons);

         String dmsstr = "N"+latd+"° "+lado+"' "+ladf+"\" "+"E"+lond+"° "+lono+"' "+lonf+"\"";

         return dmsstr;
    }

    //================================거리환 ==============================/
    List<GeoPoint> geoPoints;
    List<Marker> markers = new ArrayList<Marker>();

    public void drawMarker(GeoPoint p){
        Marker m = new Marker(map);
        m.setPosition(p);
        m.setVisible(false);
        m.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                return false;
            }
        });
        //must set the icon to null last
        m.setIcon(null);
        map.getOverlays().add(m);
        markers.add(m);
    }

    Marker coordMarker;
    public void createMarker(GeoPoint p) {
        if (coordMarker != null) {
            map.getOverlayManager().remove(coordMarker);
        }
        coordMarker = new Marker(map);
        coordMarker.setPosition(p);
        coordMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                map.getOverlayManager().remove(coordMarker);
                return false;
            }
        });
        map.getOverlays().add(coordMarker);
        map.invalidate();
    }

}
