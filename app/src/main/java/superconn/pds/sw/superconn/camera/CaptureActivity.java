package superconn.pds.sw.superconn.camera;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import superconn.pds.sw.superconn.R;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class CaptureActivity extends Activity implements LogTarget, SurfaceHolder.Callback {

    private static final int        PERMISSION_STORAGE_REQUEST = 0xBAce;
    private TextView m_LogView;
    private PylonGrab               m_PylonGrab;
    public static final String      LOG_TAG ="Grab";
    SurfaceHolder previewHolder;

    /** Log function implementation.
     *  Is invoked by Log() and called in context of UI thread.
     *  @param logLevel Severity of the log message.
     *  @param logText Text to log.
     **/
    private void LogImpl(LogTarget.LogLevel logLevel, String logText) {
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
    /** Override of LogTarget Log() function.
     *  @param logLevel Severity of the log message.
     *  @param logText Text to log.
     *  Can be called from non-UI threads and will invoke logging in UI thread.
     **/
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
        CaptureActivity.this.runOnUiThread( new LogTask( logLevel, logText) );

    }
    testView svCameraPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_capture);

        requestRights();

        m_RootPathPictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

        svCameraPreview = (testView) findViewById(R.id.surfaceView);
        Context con = this;
        svCameraPreview.setContext(con);
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
                    m_PylonGrab = new PylonGrab(CaptureActivity.this);

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

    }
    private AtomicBoolean m_isCameraValid = new AtomicBoolean(false);
    private AtomicBoolean m_isStoragePermissionGrant = new AtomicBoolean(false);
    private AtomicBoolean m_isOnDestroyCalled = new AtomicBoolean(false);

    public void onButtonClick(View view) {
    }

    private void requestRights()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
        {
            Log(LogLevel.Info,"Asking for permission to write access EXTERNAL_STORAGE");
            String[] permission = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,permission,PERMISSION_STORAGE_REQUEST) ;
        }
        else {
            Log(LogLevel.Info,"Permission to write EXTERNAL_STORAGE already granted");
            m_isStoragePermissionGrant.set(true);
            tryChangeEnableUIState(true);
        }
    }

    synchronized void tryChangeEnableUIState(boolean newUIState)
    {
        boolean tmpState = newUIState;

        if( !m_isCameraValid.get() || !m_isStoragePermissionGrant.get() || m_isOnDestroyCalled.get())
        {
            tmpState = false;
        }

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
        // Disable the UI ...
        tryChangeEnableUIState(false);

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

    public void zoomIn(View view) {
        m_PylonGrab.setZoom(true);
    }

    public void zoomOut(View view) {
        m_PylonGrab.setZoom(false);
    }
    float scalingFactor = 1.0f;
    public void zoom(View view) {
        if(scalingFactor == 1.0f){
            scalingFactor = 2.0f;
        }else{
            scalingFactor = 1.0f;
        }
        svCameraPreview.setScaleX(scalingFactor);
        svCameraPreview.setScaleY(scalingFactor);

    }

    private class SaveSingleImageRunnable implements Runnable
    {
        @Override
        public void run()
        {
            Bitmap grabImage = null;

            // Execute the sample code.
            try {
                grabImage = m_PylonGrab.getBitmap();
                if (grabImage != null) {
                    // Fetch system time and create filename.
                    String timeStamp = new SimpleDateFormat("_yyyyMMdd_HHmmss", Locale.ENGLISH).format(Calendar.getInstance().getTime());
                    String fullFilePath = m_RootPathPictures + File.separator + "PylonImgSingle" + timeStamp;

                    saveImage(fullFilePath , m_CurrentCompressFormat, grabImage);
                } else {
                    Log(LogLevel.Error, "Error: No bitmap from grab");
                }
            } catch (Exception e) {
                Log(LogLevel.Error, "Exception while handling onClick SaveImage: " + e.getMessage());
            }

            // Display the result.
            if( !m_isOnDestroyCalled.get()) {
                CaptureActivity.this.runOnUiThread(new UpdateUIAfterSampleRunRunnable(grabImage));
            }
        }
    }

    private class UpdateUIAfterSampleRunRunnable implements Runnable
    {
        Bitmap m_ResultImage;
        public UpdateUIAfterSampleRunRunnable(Bitmap resultImage){
            m_ResultImage = resultImage;
        }

        @Override
        public void run()
        {
           /* // Display the result, if set
            if(m_ResultImage != null) {
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(m_ResultImage);
            }

            // Enable the UI again
            tryChangeEnableUIState(true);*/
        }
    }
}