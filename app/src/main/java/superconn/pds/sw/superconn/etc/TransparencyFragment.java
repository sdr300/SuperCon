package superconn.pds.sw.superconn.etc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;
import superconn.pds.sw.superconn.databinding.FragmentTransparencyBinding;
import superconn.pds.sw.superconn.junmun.JunmunReceiverDeleteFragment;


public class TransparencyFragment extends Fragment {

    private FragmentTransparencyBinding mBinding;

    public TransparencyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentTransparencyBinding.inflate(inflater,container, false);

        mBinding.transparencyBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new TransparencyWriteFragment(), null).addToBackStack(null).commit();

            }
        });

        return mBinding.getRoot();
    }
}