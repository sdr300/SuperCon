package superconn.pds.sw.superconn.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;

/**
 * created 2020-12-21
 */
public class ChatMainFragment extends Fragment {

    public ChatMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_main, container, false);
    }

}