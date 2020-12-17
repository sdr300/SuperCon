package superconn.pds.sw.superconn.osm;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class DistansRing extends AppCompatActivity {
    EditText distanceRing1,distanceRing2,distanceRing3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_distans_ling);
//        distanceRing1 = (EditText) findViewById(R.id.distance1);
//        distanceRing2 = (EditText) findViewById(R.id.distance2);
//        distanceRing3 = (EditText) findViewById(R.id.distance3);
    }

    public void distanceRing(View view) {
//        staticMapController.distanceRing1 = Integer.parseInt(String.valueOf(distanceRing1.getText()));
//        staticMapController.distanceRing2 = Integer.parseInt(String.valueOf(distanceRing2.getText()));
//        staticMapController.distanceRing3 = Integer.parseInt(String.valueOf(distanceRing3.getText()));
//        staticMapController.distanceRing(view);
        finish();
    }
}
