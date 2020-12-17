package superconn.pds.sw.superconn.osm;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class WarInfo extends AppCompatActivity {
    View thisView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_war_info);
        thisView= this.getCurrentFocus();

    }

    public void mapEvent(View view) {
        /*switch (view.getId()){
            case R.id.distance:
                staticMapController.distance(view);
                finish();
                break;
            case R.id.area:
                staticMapController.area(view);
                finish();
                break;
            case R.id.distanceRing:
                Intent intentDestanceRing = new Intent(this,DistansRing.class);
                startActivity(intentDestanceRing);
                finish();
                break;
            case R.id.symbolDel:
                Intent intentDelsym = new Intent(this, deleteSymbol.class);
                startActivity(intentDelsym);
                finish();
                break;
        }*/
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /*switch (keyCode){
            case KeyEvent.KEYCODE_F1:
                staticMapController.distance(thisView);
                finish();
                break;
            case KeyEvent.KEYCODE_F2:
                staticMapController.area(thisView);
                finish();
                break;
            case KeyEvent.KEYCODE_F3:
                Intent intentDestanceRing = new Intent(this,DistansRing.class);
                startActivity(intentDestanceRing);
                finish();
                break;
            case KeyEvent.KEYCODE_F4:
                Intent intentDelsym = new Intent(this, deleteSymbol.class);
                startActivity(intentDelsym);
                finish();
                break;
            case KeyEvent.KEYCODE_F12:
                finish();
                break;
        }*/
        return false;
    }

}
