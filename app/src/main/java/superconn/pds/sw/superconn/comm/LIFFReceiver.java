package superconn.pds.sw.superconn.comm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static superconn.pds.sw.superconn.MapActivity.mainActivity;
import static superconn.pds.sw.superconn.MapActivity.maincontext;

public class LIFFReceiver extends BroadcastReceiver {
    private static String LIFF_IDENTIFICATION_SEND = "LIFF.COMM.RS232.IDENTIFICATION.SEND";
    private final String LIFF_IDENTIFICATION_RECEIVER = "LIFF.COMM.RS232.IDENTIFICATION.RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("android.intent.action.BOOT_COMPLETED")){

        }else if(LIFF_IDENTIFICATION_RECEIVER.equals(action)){
            int battery = intent.getIntExtra("battery",0);
            mainActivity.showPower(battery);
        }
    }
    public static void sendTest(){
        Intent intentTest = new Intent();
        intentTest.setAction(LIFF_IDENTIFICATION_SEND);
        intentTest.setPackage("liff.pds.sw.liffcomm");
        Log.e("리시버",LIFF_IDENTIFICATION_SEND+"메시지 보냄");
        maincontext.sendBroadcast(intentTest);
    }
}
