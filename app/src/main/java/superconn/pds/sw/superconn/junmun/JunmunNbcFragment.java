package superconn.pds.sw.superconn.junmun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import superconn.pds.sw.superconn.R;
import superconn.pds.sw.superconn.databinding.FragmentEtcMainBinding;
import superconn.pds.sw.superconn.databinding.FragmentJunmunNbcBinding;
import superconn.pds.sw.superconn.etc.CustomSpinnerAdapter;


public class JunmunNbcFragment extends Fragment {

    private FragmentJunmunNbcBinding mBinding;
    String[] spinner_nbc_chemical;
    int[] im_nbc_chemical;
    ArrayAdapter<String> arrayAdapter_type1;

    public JunmunNbcFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_junmun_nbc, container, false);
        Spinner junmun_nbc_sp_chemical = view.findViewById(R.id.junmun_nbc_sp_chemical);

        mBinding = FragmentJunmunNbcBinding.inflate(inflater,container, false);

        spinner_nbc_chemical = new String[] {"화학", "생물학", "핵"};
        im_nbc_chemical =new int[] {
                R.drawable.spinnernbcicon0,
                R.drawable.spinnernbcicon1,
                R.drawable.spinnernbcicon2
        };

        arrayAdapter_type1  =  new CustomSpinnerAdapter(getActivity().getApplicationContext(), spinner_nbc_chemical, im_nbc_chemical);

//        junmun_nbc_sp_chemical.setAdapter(arrayAdapter_type1);

        mBinding.junmunNbcSpChemical.setAdapter(arrayAdapter_type1);

        return mBinding.getRoot();

    }
}