package superconn.pds.sw.superconn.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;

/**
 * created 2020-12-21
 */
public class CameraMainFragment extends Fragment implements LogTarget, SurfaceHolder.Callback{

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
    public CameraMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_main, container, false);

        Button camera_btn_delete = view.findViewById(R.id.camera_btn_delete);
        camera_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new CameraDeleteFragment(), null).addToBackStack(null).commit();
            }
        });

        Button camera_btn_capture = view.findViewById(R.id.camera_btn_capture);
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

        SurfaceView svCameraPreview = (SurfaceView) view.findViewById(R.id.surfaceView);

        this.previewHolder = svCameraPreview.getHolder();
        this.previewHolder.addCallback(this);
        svCameraPreview.setZOrderOnTop(true);
        svCameraPreview.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        try {
            // Create wrapper class for pylon logic
            // and start the logic asynchronously.
            new Thread( new Runnable() {
                @Override
                public void run() {
                    m_PylonGrab = new PylonGrab(CameraMainFragment.this);

                    if(m_PylonGrab != null){
                        m_PylonGrab.setHolder(previewHolder);
                    }

                    m_PylonGrab.execute();
                }
            }).start();
        }
        catch(Exception e)
        {
            Log(LogLevel.Error, e.getMessage() );
        }

        return view;
    }



    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(m_PylonGrab != null){
            m_PylonGrab.setHolder(surfaceHolder);
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

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