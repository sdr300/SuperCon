package superconn.pds.sw.superconn.etc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;
import superconn.pds.sw.superconn.databinding.FragmentEtcMainBinding;

/**
 * created 2020-12-21
 */

public class EtcMainFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private FragmentEtcMainBinding mBinding;

    public EtcMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentEtcMainBinding.inflate(inflater,container, false);

        layoutManager=new LinearLayoutManager(getActivity());

        // 버튼
//        TextView junmun_tv_location = view.findViewById(R.id.junmun_tv_location);
//        junmun_tv_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentJunmunLocation(), null).addToBackStack(null).commit();
//            }
//        });

        mBinding.etcTvTransparency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new TransparencyFragment(), null).addToBackStack(null).commit();
            }
        });

        mBinding.etcTvTmmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new TmmrFragment(), null).addToBackStack(null).commit();
            }
        });

//        TextView junmun_tv_setting = view.findViewById(R.id.junmun_tv_setting);
//        junmun_tv_setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentSetting(), null).addToBackStack(null).commit();
//            }
//        });


        return mBinding.getRoot();
    }

}