package superconn.pds.sw.superconn;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class FragmentLocation extends Fragment {


    public FragmentLocation() {
        // Required empty public constructor
    }

    private TextView textview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_location, container, false);




        String getstr = getArguments().getString("send");

        if (getArguments() != null) {
//            String address = getArguments().getString("addressBundle");
            Log.d("addressCCCCC", getstr);
            Toast.makeText(getActivity(), getstr.trim(), Toast.LENGTH_LONG).show();
        } else {
            Log.d("addressXXXX", "none");
        }
        return view;

    }
}