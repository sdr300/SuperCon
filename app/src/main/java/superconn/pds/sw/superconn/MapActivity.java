package superconn.pds.sw.superconn;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarFile;

import icops.framework.common.BasicReturn;
import icops.framework.common.CAppContext;
import icops.framework.common.Geometry;
import icops.framework.util.CGeometricUtil;
import icops.warsym.inf.IWarSymFactory;
import superconn.pds.sw.superconn.DataBase.Buho;
import superconn.pds.sw.superconn.DataBase.RoomDatabaseClass;
import superconn.pds.sw.superconn.ICOPS.DemoMainView;
import superconn.pds.sw.superconn.camera.CameraMainFragment;
import superconn.pds.sw.superconn.chat.ChatMainFragment;
import superconn.pds.sw.superconn.chook.ChookFragment;
import superconn.pds.sw.superconn.comm.LIFFReceiver;
import superconn.pds.sw.superconn.coord.CoordDMS;
import superconn.pds.sw.superconn.coord.CoordUTM;
import superconn.pds.sw.superconn.coord.CoordinateManager;
import superconn.pds.sw.superconn.coord.coordMgrs;
import superconn.pds.sw.superconn.dogu.FragmentDogu;
import superconn.pds.sw.superconn.etc.EtcMainFragment;
import superconn.pds.sw.superconn.globe.GlobeFragment;
import superconn.pds.sw.superconn.junmun.JunmunReceiveFragment;
import superconn.pds.sw.superconn.video.VideoMainFragment;
import superconn.pds.sw.superconn.walkie.WalkieMainFragment;

import static android.os.Environment.getExternalStorageDirectory;

public class MapActivity extends AppCompatActivity {

    List<Buho> buhoArrayList = new ArrayList<>();
    int buhoSize = buhoArrayList.size();
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
    public static DemoMainView mMainView = null;
    boolean mLoading = false;
    public static FragmentManager fragmentManager;
    public  static RoomDatabaseClass roomDatabaseClass;
    private int fragmentBoolean = 0;
    private long backKeyPressedTime = 0; // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private Integer dogu_int;
    private String dogu_string;
    TextView distance1, clickLocation;
    int doguint , junmunint, chatint = 0;
    public String dogu_addText;
    private SwitchCompat dogu_sw;
    private RadioButton globe_rb_dms;
    private View 	decorView;
    private int	uiOption;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //풀화면
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_IMMERSIVE|
//                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
//                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
//                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
//                        View.SYSTEM_UI_FLAG_FULLSCREEN);
//        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
//        int newUiOptions = uiOptions;
//        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);



        //
        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //줌
        map.getController().setZoom(16.0);
        map.setMultiTouchControls(true);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET
        });

        //zoom 버튼 ( ALWAYS <-> NEVER 로 버튼 표시/비표시 전환)
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        //gps로 위치 가져옴
        gpsTracker = new GpsTracker(MapActivity.this);

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        double maplatitude = Double.parseDouble(String.format("%.6f", gpsTracker.getLatitude()));
        double maplongitude = Double.parseDouble(String.format("%.6f", gpsTracker.getLongitude()));

        //메인화면에 위치표시
        TextView addressText;
        addressText =  findViewById(R.id.addressText);
        addressText.setText("현재위치 좌표계: 경위도(DMS)\n"+dms(maplatitude, maplongitude));


        //콤파스(compass) 필요하면 복구
//        CompassOverlay compassOverlay = new CompassOverlay(this, map);
//        compassOverlay.enableCompass();
//        map.getOverlays().add(compassOverlay);

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

        //==========================부호 좌표를 가져온 마커들========================//

        Log.d("길이", buhoSize+"");
        for (int i=0; i< buhoSize; i++){
            Marker buhoMarker = new Marker(map);
            GeoPoint buhoPoint = new GeoPoint(Double.parseDouble(buhoArrayList.get(i).getBuhoLatitude()), (Double.parseDouble(buhoArrayList.get(i).getBuhoLongitude())));
            Log.d("마커 좌표", Double.parseDouble(buhoArrayList.get(i).getBuhoLatitude())+","+ Double.parseDouble(buhoArrayList.get(i).getBuhoLongitude()));
            buhoMarker.setIcon(this.getResources().getDrawable(R.drawable.map_marker2));
            buhoMarker.setPosition(buhoPoint);
            map.getOverlays().add(buhoMarker);
            buhoMarker.setInfoWindow(null);
            buhoMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    return false;
                }
            });
        }

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
                double dlatitude = Double.parseDouble(String.format("%.6f",p.getLatitude()));
                double dlongitude = Double.parseDouble(String.format("%.6f",p.getLongitude()));
                Toast.makeText(getBaseContext(),"선택 좌표 위치:" +"\n"+dlatitude + "\n"+ dlongitude, Toast.LENGTH_LONG).show();


                distance1 = (TextView) findViewById(R.id.distance1);
                dogu_string = distance1.getText().toString();
                dogu_int = (Integer.parseInt(dogu_string))/50;
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
                line250lat.setColor(Color.TRANSPARENT);
                line250lat.setWidth(6);

                map.getOverlayManager().add(line250lat);

                List<GeoPoint> geoPoint250lon = new ArrayList<>();

                geoPoint250lon.add(new GeoPoint(p.getLatitude() , p.getLongitude()- 0.00283*dogu_int*4));
                geoPoint250lon.add(new GeoPoint(p.getLatitude() , p.getLongitude()+ 0.00283*dogu_int*4));

                final Polyline line250lon = new Polyline();
                line250lon.setPoints(geoPoint250lon);
                line250lon.setColor(Color.TRANSPARENT);
                line250lon.setWidth(6);

                map.getOverlayManager().add(line250lon);

                //거리환 원그리기 (4개)
                final Marker[] dmarkers = new Marker[5];
                GeoPoint[] dGpoints = new GeoPoint[5];
                final Polygon[] dpolygons = new Polygon[5];
                for ( i=0; i<5; i++){
                    dpolygons[i] = new Polygon(map);
                    dpolygons[i].setPoints(Polygon.pointsAsCircle(p, 250.0*dogu_int*(i)));
                    dpolygons[i].setFillColor(Color.TRANSPARENT);
                    dpolygons[i].setStrokeColor(Color.BLACK);
                    dpolygons[i].setStrokeWidth(5);
                    if (i<4) {
                        dpolygons[i].setOnClickListener(new Polygon.OnClickListener() {
                            @Override
                            public boolean onClick(Polygon polygon, MapView mapView, GeoPoint eventPos) {
                                return false;
                            }
                        });
                    }else {
                        dpolygons[i].setOnClickListener(new Polygon.OnClickListener() {
                            @Override
                            public boolean onClick(Polygon polygon, MapView mapView, GeoPoint eventPos) {
                                int i =0;
                                for (i=0; i<5 ; i++) {
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
                    for (i = 0; i < 5; i++) {
                        dGpoints[i] = new GeoPoint(p.getLatitude() + 0.002245 * dogu_int * (i), p.getLongitude());
                        dmarkers[i] = new Marker(map);
                        dmarkers[i].setTextIcon(50 * dogu_int * (i) + "");
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

                dpolygons[0].setOnClickListener(new Polygon.OnClickListener() {
                    @Override
                    public boolean onClick(Polygon polygon, MapView mapView, GeoPoint eventPos) {
                        int i=0;
                        for ( i=0; i<5; i++) {
                            dpolygons[i].setOnClickListener(new Polygon.OnClickListener() {
                                @Override
                                public boolean onClick(Polygon polygon, MapView mapView, GeoPoint eventPos) {
                                    int i = 0;
                                    for (i = 0; i < 5; i++) {
                                        dmarkers[i].remove(map);
                                        mapView.getOverlays().remove(dpolygons[i]);
                                    }
                                    mapView.getOverlays().remove(line250lat);
                                    mapView.getOverlays().remove(line250lon);
                                    return false;
                                }
                            });
                        }
                        return false;
                    }
                });
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

        //아래 두줄은 풀스크린용(상단바 제거, 상단바가 필요하면 주석처리)
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

        String sdPath = getExternalStorageDirectory().getAbsolutePath();
        String wsDataPath = sdPath + "/ICOPS/ICOPSaWarsymData.jar";
        Log.d(this.getClass().getName(), "@@@@@ 주소 :" + wsDataPath);
        // ICOPS 컴포넌트 초기화
        new IcopsInitTask().execute(getString(R.string.config_icopsAppContext));

        mMainView = findViewById(R.id.DemoMainView);
        mMainView.mMain = this;

        // 화면 해상도에 따른 계산을 위해 필요한 환경설정 값을 초기화한다.
        CGeometricUtil.initContext(getApplicationContext());
        CGeometricUtil.mDPM *= 1.2f; // 밀리미터에 해당하는 화면 픽셀 개수를 임의로 조정하려면 mDPM 값을 변경한다.

        map = (MapView) findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        //======================gps 위치받아오기=========/
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        //icops overlay maplistener (맵 이동시 icops 아이콘 이동)
        map.setMapListener(new DelayedMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                mMainView.clearSymbol();
                icopsTest(map);
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                mMainView.clearSymbol();
                icopsTest(map);
                return false;
            }
        },100));

        //내 위치
        addressText = findViewById(R.id.addressText);
        addressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //====================내 위치===========================

                if (fragmentBoolean == 1) {
                    fragmentBoolean = 0;
                }

                //gps로 위치 잡아옴

                MapView mapview = (MapView) findViewById(R.id.mapView);

                Marker startMarker = new Marker(map);

                map.getOverlays().remove(0);

                startMarker.setIcon(getResources().getDrawable(R.drawable.map_marker2));

                gpsTracker = new GpsTracker(MapActivity.this);

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                String address = getCurrentAddress(latitude, longitude);

                map.getController().setZoom(16.0);
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

            }
        });


    }

    private final String LIFF_IDENTIFICATION_SEND = "LIFF.COMM.RS232.IDENTIFICATION.SEND";
    private final String LIFF_IDENTIFICATION_RECEIVER = "LIFF.COMM.RS232.IDENTIFICATION.RECEIVER";
    LIFFReceiver LIFFReceiver;

    @Override
    public void onResume() {
        super.onResume();

        //
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

    public void icopsTest(View view) {

        gpsTracker = new GpsTracker(MapActivity.this);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        Marker dmarker = new Marker(map);

        GeoPoint geoPoint = new GeoPoint(latitude, longitude); // 좌표설정
        Projection pj = map.getProjection();
        Point point = pj.toPixels(geoPoint, null);
        Geometry.Point.Array gPoints = new Geometry.Point.Array();
        gPoints.add(point.x, point.y);
        mMainView.drawSingle("SUG*UCF---*****", 0, gPoints);
        Log.d("icops", point.x+"/"+point.y);
        mMainView.invalidate();
    }

    // ICOPS 초기화를 수행하는 Task
    private class IcopsInitTask extends AsyncTask<String, String, CAppContext> {

        @Override
        protected CAppContext doInBackground(String... args) {
            boolean res;
            CAppContext appCxt = new CAppContext();
            Application app = getApplication();
            AssetManager amg = app.getAssets();

            // open resource
            InputStream is;
            try {
                is = amg.open(args[0]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            res = appCxt.initialize("icops.framework.common.CAppContextDataAgentXml", is, app, amg);
            if (!res) {
                publishProgress("ICOPS 초기화 실패");
                return null;
            }

            // 군대부호 JAR 파일 열기
            String sdPath = getExternalStorageDirectory().getAbsolutePath();
            String wsDataPath = sdPath + "/ICOPS/ICOPSaWarsymData.jar";
            Log.d(this.getClass().getName(), "@@@@@ 주소 :" + wsDataPath);

            try {
                mWarsymJar = new JarFile(wsDataPath);
            } catch (IOException e) {
                e.printStackTrace();
                publishProgress("군대부호자료 열기 실패 " + wsDataPath);
                mAppCxt = appCxt;
                return appCxt;
            }

            BasicReturn.TypeObject retObj = new BasicReturn.TypeObject();
            res = appCxt.getStockObject("icops.warsym.inf.IWarSymFactory", retObj);
            if (!res || retObj.value == null) {
                publishProgress("군대부호 관리 객체 생성 실패");
                mAppCxt = appCxt;
                return appCxt;
            }

            try {
                IWarSymFactory symFac = (IWarSymFactory) retObj.value;
                res = symFac.initialize(appCxt, mWarsymJar);
                if (res) {
                    mSymFac = symFac;
                } else {
                    publishProgress("군대부호 관리 객체 초기화 실패");
                }
            } catch (ClassCastException e) {
                publishProgress("군대부호 관리 객체 초기화 오류");
            }

            // 군대부호 도시에 사용할 폰트를 설정한다.
            // 설정하지 않으면 기본으로 <Arial Unicode MS>를 사용한다.
            //WarSym.FontFamily = "궁서체";

            mAppCxt = appCxt;
            return appCxt;
        }

        @Override
        protected void onPostExecute(CAppContext result) {
            mLoading = false;
            onPostInitICOPS(result);
        }

        @Override
        protected void onProgressUpdate(String... prgs) {
        }
    }

    public void onPostInitICOPS(CAppContext appCxt) {
        if (appCxt == null) {
            return;
        }

        //mMainView.setVisibility(View.VISIBLE);
    }

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
        } else if (view.getId() == R.id.btn_back_under){
            fr = new FragmentZ();
            fm.popBackStack();
            fragmentBoolean = 0;
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_frame, fr);
            fragmentTransaction.commit();
        } else if (view.getId() == R.id.btn_back_under_super){
            super.onBackPressed();
 //======================== MapActivity ibt 버튼 ====================================
        } else if (view.getId() == R.id.ibt_zoom_plus) {
            map.getController().zoomIn();
        } else if (view.getId() == R.id.ibt_zoom_minus) {
            map.getController().zoomOut();
        } else if (view.getId() == R.id.ibt_chook ) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_globe) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_dogu ) {
            switchDogu();
            switchAll(view);
        } else if (view.getId() == R.id.ibt_dogu_target ) {
            FragmentDogu fragmentDogu = new FragmentDogu();
            Bundle bundle = new Bundle();
            bundle.putString("addText", dogu_addText);
            fragmentDogu.setArguments(bundle);
            switchAll(view);
        } else if (view.getId() == R.id.ibt_video ) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_camera ) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_chat ) {
            switchChat();
            switchAll(view);
        } else if (view.getId() == R.id.ibt_chat_team ) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_junmun ) {
            switchJunmun();
            switchAll(view);
        } else if (view.getId() == R.id.ibt_junmun_team) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_waikie ) {
            switchAll(view);
        } else if (view.getId() == R.id.ibt_etc) {
            switchAll(view);
        }
    }

    //========================switchFragment(power)===============
    //getSupportFragmentManager().findFragmentById(R.id.fragment_frame)==null
    Fragment fr;
    FragmentManager fm = getSupportFragmentManager();


    //=============측정도구 선택
    public void switchDogu() {
        if ( doguint%2 == 0) {
            findViewById(R.id.doguselect).setVisibility(View.VISIBLE);
            doguint = doguint+1;
            //
            findViewById(R.id.junmunselect).setVisibility(View.INVISIBLE);
            junmunint = 0;
            //
            findViewById(R.id.chatselect).setVisibility(View.INVISIBLE);
            chatint = 0;
        } else  if ( doguint%2 == 1 ) {
            findViewById(R.id.doguselect).setVisibility(View.INVISIBLE);
            doguint = doguint+1;
        }
    }

    //============= 전문 선택
    public void switchJunmun() {
        if ( junmunint%2 == 0) {
            findViewById(R.id.junmunselect).setVisibility(View.VISIBLE);
            junmunint = junmunint+1;
            //
            findViewById(R.id.doguselect).setVisibility(View.INVISIBLE);
            doguint = 0;
            //
            findViewById(R.id.chatselect).setVisibility(View.INVISIBLE);
            chatint = 0;
        } else  if ( junmunint%2 == 1 ) {
            findViewById(R.id.junmunselect).setVisibility(View.INVISIBLE);
            junmunint = junmunint+1;
        }
    }

    //============== 채팅 선택
    public void switchChat() {
        if ( chatint%2 == 0) {
            findViewById(R.id.chatselect).setVisibility(View.VISIBLE);
            chatint = chatint+1;
            //
            findViewById(R.id.doguselect).setVisibility(View.INVISIBLE);
            doguint = 0;
            //
            findViewById(R.id.junmunselect).setVisibility(View.INVISIBLE);
            junmunint = 0;
        } else  if ( chatint%2 == 1 ) {
            findViewById(R.id.chatselect).setVisibility(View.INVISIBLE);
            chatint = chatint+1;
        }
    }

    //=============== switchAll
    public void switchAll(View view) {
         if (view.getId() == R.id.ibt_dogu || view.getId() == R.id.ibt_junmun || view.getId() == R.id.ibt_chat) {
            fr = new FragmentZ();
            fm.popBackStack();
             fragmentBoolean = 0;
        } else if (fragmentBoolean == 0) {
            if (view.getId() ==R.id.ibt_chook) {
                fr = new ChookFragment();
            } else if (view.getId() ==R.id.ibt_globe) {
                fr = new GlobeFragment();
            } else if (view.getId() ==R.id.ibt_dogu_target) {
                fr = new FragmentDogu();
            } else if (view.getId() ==R.id.ibt_video) {
                fr = new VideoMainFragment();
            } else if (view.getId() ==R.id.ibt_camera) {
                fr = new CameraMainFragment();
            } else if (view.getId() ==R.id.ibt_chat_team) {
                fr = new ChatMainFragment();
            } else if (view.getId() ==R.id.ibt_junmun_team ) {
                fr = new JunmunReceiveFragment();
            } else if (view.getId() ==R.id.ibt_waikie) {
                fr = new WalkieMainFragment();
            } else if (view.getId() ==R.id.ibt_etc) {
                fr = new EtcMainFragment();
            }
             if ( doguint%2 == 1 ) {
                 findViewById(R.id.doguselect).setVisibility(View.INVISIBLE);
                 doguint = doguint+1;
             }
             if ( junmunint%2 == 1 ) {
                 findViewById(R.id.junmunselect).setVisibility(View.INVISIBLE);
                 junmunint = junmunint + 1;
             }
             if ( chatint%2 == 1 ) {
                 findViewById(R.id.chatselect).setVisibility(View.INVISIBLE);
                 chatint = chatint + 1;
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