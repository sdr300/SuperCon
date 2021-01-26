package superconn.pds.sw.superconn.camera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceHolder;

import com.basler.pylon.EGrabStrategy;
import com.basler.pylon.GrabResult;
import com.basler.pylon.InstantCamera;
import com.basler.pylon.TlFactory;
import com.basler.pylon.pylon;

import org.genicam.genapi.IEnumeration;
import org.genicam.genapi.IFloat;
import org.genicam.genapi.IInteger;
import org.genicam.genapi.genapi;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import superconn.pds.sw.superconn.camera.LogTarget.LogLevel;

/**
 *  Wrapper class around pylon functions.
 **/
public class PylonGrab {

    SurfaceHolder imgHolder;
    boolean zoom =false;
    public void setZoom(boolean zoom){this.zoom=zoom; }
    public void setHolder(SurfaceHolder holder){
        imgHolder = holder;
    }
    /* Grab Sample function.
    * */
    public void execute() {
        Bitmap result = null;
        Log.e("pylon 테스트","재생 시작");
        try {

            /*      Startup       */
            Log.e("pylon 테스트","세팅 시작");
            Log(LogLevel.Info, "Initialize");
            Log(LogLevel.Info, "=========================");

            Log(LogLevel.Info, "Initialize Pylon");
            pylon.pylonInitialize();
            Log(LogLevel.Info, "create Instant Camera");
            // Get the first camera and create an InstantCamera helper class.
            m_Camera = new InstantCamera(TlFactory.getInstance().createFirstDevice());
            Log(LogLevel.Info, "open Camera");
            // Open camera. We keep the camera open the whole time.
            Log(LogLevel.Info, " Model Name (from enumeration):    " + m_Camera.getDeviceInfo().getModelName());
            Log(LogLevel.Info, " Device Name (from enumeration):   " + m_Camera.getDeviceInfo().getFullName());
            Log(LogLevel.Info, " Friendly Name (from enumeration): " + m_Camera.getDeviceInfo().getFriendlyName());
            m_Camera.openCamera();
            Log(LogLevel.Info, " Vendor Name:   " + m_Camera.getNodeMap().getStringNode("DeviceVendorName").getValue());
            Log(LogLevel.Info, " Model Name:    " + m_Camera.getNodeMap().getStringNode("DeviceModelName").getValue());
            Log(LogLevel.Info, " Serial Number: " + m_Camera.getNodeMap().getStringNode("DeviceSerialNumber").getValue());
            /*      Camera Settings       */

            Log(LogLevel.Info, " ");
            Log(LogLevel.Info, "Camera Settings");
            Log(LogLevel.Info, "=========================");

            // Set the AOI:
            // On some cameras, the offsets are read-only.
            // Therefore we use the isWritable() method which also checks if the node is not null.
            IInteger offsetXNode = m_Camera.getNodeMap().getIntegerNode("OffsetX");
            if (genapi.isWritable(offsetXNode)){
                long offsetXMin = offsetXNode.getMin();
                offsetXNode.setValue(offsetXMin);
                Log(LogLevel.Info, " Offset X: " + offsetXMin);
            }

            IInteger offsetYNode = m_Camera.getNodeMap().getIntegerNode("OffsetY");
            if (genapi.isWritable(offsetYNode)){
                long offsetYMin = offsetYNode.getMin();
                offsetYNode.setValue(offsetYMin);
                Log(LogLevel.Info, " Offset Y: " + offsetYMin);
            }

            // Some properties have restrictions.
            // We use functions that automatically perform value corrections.
            // Alternatively, you can use get<TYPE>Inc() / get<TYPE>Min() / get<TYPE>Max() to make sure you set a valid value.
            m_Camera.getNodeMap().getIntegerNode( "Width" ).setValueNearest(1920);
            m_Camera.getNodeMap().getIntegerNode( "Height" ).setValueNearest(1080);
            Log(LogLevel.Info, " Width: " + m_Camera.getNodeMap().getIntegerNode("Width").getValue() + ", Height: " + m_Camera.getNodeMap().getIntegerNode("Height").getValue());

            // Set default grab format to Mono8 or BayerXX8.
            // We use the getSettableValues() to get a list of all supported pixel formats.
            List<String> availablePixelFormats = m_Camera.getNodeMap().getEnumerationNode("PixelFormat").getSettableValues();
            String grabPixelFormat = getSupportedBayer8Format(availablePixelFormats);
            if (grabPixelFormat == null) {
                // Mono8 is default fallback.
                grabPixelFormat = "Mono8";
            }
            Log(LogLevel.Info, " Set grab format to: " + grabPixelFormat);
            m_Camera.getNodeMap().getEnumerationNode("PixelFormat").setValue(grabPixelFormat);

            // Change the gain setting to 50 % ->  Min + ((Max-Min) / 2).
            //
            // Note: Some newer camera models may have auto functions enabled.
            //       To be able to set the gain value to a specific value,
            //       the Gain Auto auto function must be disabled first.
            // Access the GainAuto node (enumeration).
            // We use a "Try" function that only performs the action if the parameter is writable.
            IEnumeration nodeGainAuto = m_Camera.getNodeMap().getEnumerationNode("GainAuto");
            if( Objects.nonNull(nodeGainAuto) ) {
                nodeGainAuto.trySetValue("Off");
            }

            // Calculate gain value for 50% and write it to the camera.
            IFloat gainNode = m_Camera.getNodeMap().getFloatNode("Gain");
            double minGain = gainNode.getMin();
            double maxGain = gainNode.getMax();
            double newGain = minGain + ((maxGain - minGain) / 2.0);
            gainNode.setValue(newGain);

            Log(LogLevel.Info,
                    " Gain (50): "
                            + String.format(Locale.ENGLISH,"%.2f", gainNode.getValue())
                            + " (Min: "
                            + String.format(Locale.ENGLISH,"%.2f", minGain)
                            + ", Max: "
                            + String.format(Locale.ENGLISH,"%.2f", maxGain)
                            + ")");

            // Set the exposure time to a fixed value.
            IEnumeration nodeExposureAuto = m_Camera.getNodeMap().getEnumerationNode("ExposureAuto");
            if( Objects.nonNull(nodeExposureAuto) ) {
                nodeExposureAuto.trySetValue("Off");
            }
            IFloat exposureTimeNode = m_Camera.getNodeMap().getFloatNode("ExposureTime");
            if (genapi.isWritable(exposureTimeNode)){
                exposureTimeNode.setValue(100000);
                Log(LogLevel.Info,
                        " ExposureTime: "
                                + String.format(Locale.ENGLISH,"%.2f", exposureTimeNode.getValue())
                                + " [" + exposureTimeNode.getUnit() + "]");
            }

            /*      Streaming Settings       */

            Log(LogLevel.Info, " ");
            Log(LogLevel.Info, "Streaming Settings");
            Log(LogLevel.Info, "=========================");
            optimizeTransferPrm();

            // The parameter MaxNumBuffer can be used to control the number of buffers
            // allocated for grabbing. The default value of this parameter is 10.

            m_Camera.getStreamGrabberNodeMap().getIntegerNode("MaxNumBuffer").setValue(5);
            Log(LogLevel.Info, " Used Image buffer(s): " + m_Camera.getStreamGrabberNodeMap().getIntegerNode("MaxNumBuffer").getValue());

            /*      Capture some images       */

            Log(LogLevel.Info, " ");
            Log(LogLevel.Info, "Capture " + c_countOfImagesToGrab + " Images");
            Log(LogLevel.Info, "=========================");


            Log.e("pylon 테스트","세팅 끝");
            // Start the grabbing of c_countOfImagesToGrab images.
            // The camera device is parameterized with a default configuration which
            // sets up free-running continuous acquisition.

            Log.e("pylon 테스트","그랩 시작");
            m_Camera.startGrabbing(EGrabStrategy.GrabStrategy_LatestImageOnly);



            while (m_Camera.isGrabbing() )
            {
                // We create a new GrabResult for each image.
                // It will be passed to the processing thread
                // where it will be debayered and compressed.
                // This will be passed to the save image command.
                // Make sure to call grabResult.release() to requeue the buffers.
                GrabResult grabResult = new GrabResult();

                // Wait for a grabResult with 5000 ms timeout.
                boolean wasRetrieveResultSucceeded = m_Camera.retrieveResult(5000, grabResult);

                // It needs to be checked whether the grab represented by the grab result has been successful
                if (wasRetrieveResultSucceeded && grabResult.grabSucceeded())
                {
                    result = grabResult.convertToBitmap();
                    Log.e("와우",result.getConfig().toString());
                    // For visualisation only we save the last image.
                    if(imgHolder != null){
                        Canvas canvas = imgHolder.lockCanvas();

                        if (canvas != null)
                        {
                            canvas.save();

                            int canvasWidth = canvas.getWidth();
                            int canvasHeight = canvas.getHeight();

                            bitmap = result;
                            Log.e("와우",result.getConfig().toString());
                            //bitmapToByteArray(result);
                            if(result != null)
                            {
                                if(zoom){
                                    canvas.scale(2.0f,2.0f);
                                    canvas.drawBitmap(bitmap, null, new RectF(canvasWidth/-4, canvasHeight/-4, canvasWidth, canvasHeight), null);
                                }else{
                                    canvas.drawBitmap(bitmap, null, new RectF(0, 0, canvasWidth, canvasHeight), null);
                                }


                               // canvas.drawBitmap(result, 0, 0, paint);
                            }

                            imgHolder.unlockCanvasAndPost(canvas);
                        }
                    }


                    // Parcel the grabResult and compression parameter for an SaveImageCommand and enqueue it.
                    // processor.append( new SaveImageCommand(path + String.format(Locale.ENGLISH,"_%04d",count), format, grabResult,m_LogTarget) );
                } else
                {
                    if(wasRetrieveResultSucceeded) {
                        Log(LogLevel.Error, "Grab failed (grabResult are erroneous): " + grabResult.getErrorDescription());
                    }else{
                        Log(LogLevel.Error, "Grab failed (retrieveResult are erroneous)");
                    }

                }
                grabResult.release();
            }
        }
        catch( Exception e) {
            Log(LogLevel.Error, "Exception occurred :" + e.getMessage());
        }
        finally {
            if (m_Camera != null) {
                m_Camera.closeCamera();
            }
            pylon.pylonTerminate(true);
        }
    }


    /***************** Utility ***********************/

    public static final int c_countOfImagesToGrab = 10;

    Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    private static class USBPrm {
        private long m_NumURBs;
        private long m_SizeURBs;
        public USBPrm(long numURBs, long sizeURBs) {
            this.m_NumURBs = numURBs;
            this.m_SizeURBs = sizeURBs;
        }
        public long NumberOfURBs( ) {
            return m_NumURBs;
        }
        public long SizeURBs() {
            return m_SizeURBs;
        }
    }

    private InstantCamera m_Camera;
    private LogTarget m_LogTarget;
    private void Log( LogTarget.LogLevel logLevel, String logText) {
        if( m_LogTarget != null) {
            m_LogTarget.Log(logLevel, logText);
        }
    }
    /** Return optimized USB parameters for the currently configured camera payload size.
    *
    * Note:
    * This function tries to calculate the best values for typical Android devices.
    * However, this heavily depends on the target system, the OS configuration, and the model and number of cameras used.
    * That is why it is typically useful or even recommended to tweak some parameters.
    *
    * USB-FS memory (USB File System):
    * The USB-FS memory is used for allocating USB transfers, i.e., so-called USB Request
    * Blocks or simply URBs.
    * On the most Android devices, it has a default size of 16 MByte.
    * The default USB-FS size can be changed only on 'rooted' or custom OS images, e.g., on
    * embedded boards. In this case, we recommend increasing the USB-FS size.
    * Android smartphones and tablets don't allow changing the USB-FS size.
    *
    * The USB-FS memory is shared by all connected USB devices, e.g., camera(s), mouse,
    * keyboard, etc.
    * In case you want to use more USB devices, we recommend increasing the number of
    * so-called "reserved bytes".
    * If you intend to use only a single camera, we recommend using 4 MByte as reserved bytes.
    *
    * Fragmentation:
    * As already explained, the USB-FS memory is not preallocated for exclusive use by pylon.
    * Over time, it may happen that the application can't allocate large buffers anymore, although
    * this may have been possible at the beginning of program execution.
    * If you run into problems with that, reduce the 'maxURBSize'.
    *
    * Fragmentation and the internal memory handling on Android also limit the number
    * and the size of the largest possible buffers. That is why we can't use URBs of 16 MByte.
    * This behavior can only be changed by changing some kernel settings.
    * A typical size for the largest zone is 4 MByte.
    * If you are building your own kernel, refer to CONFIG_FORCE_MAX_ZONEORDER and
    * /proc/buddyinfo for more information.
    *
    * The main goal of that function is to define and apply the maximum possible buffer size
    * that would result in reducing the CPU load and improving the overall stability.
    */
    private USBPrm calculateOptimalUSBParameters() {
        //> Get/define the limits.
        final long typicalTotalURBSize_MBytes = 16*1024*1024;
        final long reservedUSBSpace_MBytes = 4*1024*1024;
        final long usedTotalUSBSpace_MBytes = typicalTotalURBSize_MBytes - reservedUSBSpace_MBytes;
        final long maxURBCount = 64;

        final long payloadSize= m_Camera.getNodeMap().getIntegerNode("PayloadSize").getValue();
        final long maxURBSize = m_Camera.getStreamGrabberNodeMap().getIntegerNode("MaxTransferSize").getMax();
        
        //> Determine the maximum useful buffer size starting with the smallest possible value.
        long usedURBSize = m_Camera.getStreamGrabberNodeMap().getIntegerNode("MaxTransferSize").getMin();
        while( usedURBSize < payloadSize )
        {
            // Increase the size, URBSize internally are 2^n.
            long tmpURBSize = usedURBSize << 1;
            if(tmpURBSize > maxURBSize)
                break;

            // We need 4 URBs minimum (2 header + 2 data URBs).
            long tmpURBCount = usedTotalUSBSpace_MBytes / tmpURBSize;
            if(tmpURBCount < 4)
                break;

            usedURBSize = tmpURBSize;
        }

        //> Determine the number of URBs
        // for every completely enqueued payload.
        // Add 2 URBs for the leader and the trailer, which are very small.
        long usedURBCount = usedTotalUSBSpace_MBytes / usedURBSize;
        if( payloadSize <= usedURBSize )
        {
            // Every URB contains one image.
            usedURBCount *= 3;
        }
        else
        {
            // Multiple URBs may be needed to transfer one image.
            long enqueuedImages = (usedURBCount*usedURBSize) / payloadSize;
            usedURBCount +=  enqueuedImages * 2;
        }

        // Limit the total number of URBs (too many URBs will only waste memory).
        usedURBCount = Math.min(usedURBCount, maxURBCount);

        return new USBPrm( usedURBCount, usedURBSize );
    }

    /** Optimize USB parameters.
     **/
    private void optimizeTransferPrm() {
        if( m_Camera.isUsb() ) {
            // Depending on the payload size (AOI and PixelFormat),
            // we can calculate streaming parameters to improve
            // throughput and CPU load.
            IInteger maxQueuedUrbsNode   = m_Camera.getStreamGrabberNodeMap().getIntegerNode("NumMaxQueuedUrbs");
            IInteger maxTransferSizeNode = m_Camera.getStreamGrabberNodeMap().getIntegerNode("MaxTransferSize");
            IInteger payloadSizeNode     = m_Camera.getNodeMap().getIntegerNode("PayloadSize");
            // calculate optimal parameters.
            USBPrm usbPrm = calculateOptimalUSBParameters( );
            // Show the original and changed settings.
            Log( LogLevel.Info, " URBs used: " + maxQueuedUrbsNode.getValue() + " -> "+ usbPrm.NumberOfURBs() );
            Log( LogLevel.Info, " URBs size: " + maxTransferSizeNode.getValue()/1024. + " -> " + usbPrm.SizeURBs()/1024.0 + "[kByte]" );
            Log( LogLevel.Info, " Payload:   " + payloadSizeNode.getValue()/1024. + " [kByte]");
            // Set to optimized values.
            maxQueuedUrbsNode.setValue(usbPrm.NumberOfURBs());
            maxTransferSizeNode.setValue(usbPrm.SizeURBs());
            // Almost every USB camera has an option to limit the maximum bandwidth used.
            IEnumeration throughputModeNode = m_Camera.getNodeMap().getEnumerationNode("DeviceLinkThroughputLimitMode");
            IInteger throughputLimitNode    = m_Camera.getNodeMap().getIntegerNode("DeviceLinkThroughputLimit");

            if( genapi.isWritable(throughputModeNode) && genapi.isWritable(throughputLimitNode) )  {
                final String oldLimitMode = throughputModeNode.getValue();
                throughputModeNode.setValue("On");
                final String newLimitMode = throughputModeNode.getValue();

                double oldThroughputLimit = throughputLimitNode.getValue()/1000.0/1000.0;
                throughputLimitNode.setValueNearest(100 * 1000 * 1000);
                double newThroughputLimit = throughputLimitNode.getValue()/1000.0/1000.0;

                Log( LogLevel.Info," ThroughputLimitMode:  " + oldLimitMode + " -> " + newLimitMode);
                Log( LogLevel.Info," ThroughputLimit:      " + oldThroughputLimit + " -> " + newThroughputLimit + " [MByte/s]" );
            }
        }
    }

    /** Default constructor.
     *  @param logTarget Logger implementation can be null.
     **/
    PylonGrab( LogTarget logTarget) {
        m_LogTarget = logTarget;
    }

    /** Find any supported BayerXX8 pixel format.
     **/
    public String getSupportedBayer8Format(List<String> settableFormats) {
        for( String format : settableFormats ) {
            if (format.matches("Bayer(RG|GR|BG|GB)8") ) {
                return format;
            }
        }
        return null;
    }

}
