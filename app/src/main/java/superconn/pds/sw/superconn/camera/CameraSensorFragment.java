package superconn.pds.sw.superconn.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;
import superconn.pds.sw.superconn.databinding.FragmentCameraSensorBinding;

public class CameraSensorFragment extends Fragment implements LogTarget{
    private static final int        PERMISSION_STORAGE_REQUEST = 0xBAce;
    private TextView m_LogView;
    private PylonGrab               m_PylonGrab;
    public static final String      LOG_TAG ="Grab";
    private MapActivity mainActivity;

    private void LogImpl(LogLevel logLevel, String logText) {
        if( m_LogView != null && logLevel != LogLevel.Trace) {
            if(m_LogView.getLineCount() > 1000 ) {
                int charsToRemove = m_LogView.getText().length() / 2;
                m_LogView.getEditableText().delete( 0, charsToRemove);
            }
            m_LogView.append(logLevel.toString() + " :" + logText + "\n");
        }
        switch(logLevel) {
            case Info:
                Log.i(LOG_TAG,logText);
                break;
            case Error:
                Log.e(LOG_TAG,logText);
                break;
            case Warning:
                Log.w(LOG_TAG,logText);
                break;
            default:
                Log.d(LOG_TAG,logText);
                break;

        }
    }
    @Override
    public void Log(LogTarget.LogLevel logLevel, String logText) {
        class LogTask implements Runnable {
            final LogTarget.LogLevel    m_LogLevel;
            final String                m_LogText;
            LogTask( LogTarget.LogLevel level, String logText) {
                m_LogLevel = level;
                m_LogText  = logText;
            }
            @Override
            public void run()
            {
                LogImpl(m_LogLevel, m_LogText);
            }

        }
        //mainActivity.this.runOnUiThread( new LogTask( logLevel, logText) );
    }
    SurfaceHolder previewHolder;
    private FragmentCameraSensorBinding mBinding;

    public CameraSensorFragment() {
        // Required empty public constructor
    }
    private Handler mHandler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_camera_sensor, container, false);
        mBinding = FragmentCameraSensorBinding.inflate(inflater,container, false);

        Button camera_btn_capture = view.findViewById(R.id.camera_sensor_btn_capture);
        camera_btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    capture(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        m_RootPathPictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        // Create a member for easy access to UI elements.
        //m_LogView = findViewById(R.id.logView);
        //m_LogView.setMovementMethod(new ScrollingMovementMethod() );

        final Context con = this.getContext();
        mHandler = new Handler(Looper.getMainLooper());

        mHandler.post( new Runnable() {
            @Override
            public void run() {
                Log.e("pylon 테스트","pylonGrab 생성");
                m_PylonGrab = new PylonGrab(CameraSensorFragment.this);
                //testView test = new testView(con,m_PylonGrab);

                //previewHolder = test.getHolder();
               // previewHolder.addCallback(test);

                previewHolder.setFixedSize(1024,760);
                //((RelativeLayout) view.findViewById(R.id.surfaceView)).addView(test);


                m_PylonGrab.execute();
            }});



        return view;
    }

    public void capture(View view) throws IOException {

        // Start an additional thread to keep the UI responsive.
        Thread sampleThread = new Thread( new SaveSingleImageRunnable());
        sampleThread.start();
    }
    public static void saveImage(String imagePath, Bitmap.CompressFormat compressFormat, Bitmap bitmap) throws IOException
    {
        // Add file extension, e.g., ".png"
        imagePath += "." + compressFormat.toString().toLowerCase();

        //> save the bitmap as compressed image.
        File file = new File(imagePath);
        FileOutputStream outputStream = new FileOutputStream(file);
        bitmap.compress(compressFormat,100, outputStream );
        outputStream.flush();
        outputStream.close();
    }

    private Bitmap.CompressFormat   m_CurrentCompressFormat = Bitmap.CompressFormat.JPEG;
    private String m_RootPathPictures = null;
    private class SaveSingleImageRunnable implements Runnable
    {
        @Override
        public void run() {
            Bitmap grabImage = null;

            // Execute the sample code.
            try {
                grabImage = m_PylonGrab.getBitmap();
                if (grabImage != null) {
                    // Fetch system time and create filename.
                    String timeStamp = new SimpleDateFormat("_yyyyMMdd_HHmmss", Locale.ENGLISH).format(Calendar.getInstance().getTime());
                    String fullFilePath = m_RootPathPictures + File.separator + "PylonImgSingle" + timeStamp;

                    saveImage(fullFilePath, m_CurrentCompressFormat, grabImage);
                } else {
                    Log(LogLevel.Error, "Error: No bitmap from grab");
                }
            } catch (Exception e) {
                Log(LogLevel.Error, "Exception while handling onClick SaveImage: " + e.getMessage());
            }

        }
    }
}