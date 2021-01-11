package superconn.pds.sw.superconn.etc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.lang.reflect.Field;

import superconn.pds.sw.superconn.R;
import superconn.pds.sw.superconn.databinding.FragmentTransparencyWriteBinding;

public class TransparencyWriteFragment extends Fragment {

    private FragmentTransparencyWriteBinding mBinding;
    int[] spinnerColors,spinnerLines;
    String[] spinnerColorNames, spinnerLineNames;
    int selected_color_idx, selected_line_idx = 0;


    public TransparencyWriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_transparency_write, container, false);
        mBinding = FragmentTransparencyWriteBinding.inflate(inflater, container, false);


        // 스피너에 보여줄 문자열과 이미지 목록을 작성합니다.
        spinnerColorNames = new String[]{"yellow", "black", "green", "red"};
        spinnerColors = new int[]{
                R.drawable.transyellow,
                R.drawable.transblack,
                R.drawable.transgreen,
                R.drawable.transred
        };
        spinnerLineNames = new String[]{"실선", "점선"};
        spinnerLines = new int[]{
                R.drawable.transsolidline,
                R.drawable.transdottedline
        };



        final Spinner tpwrite_sp_color = view.findViewById(R.id.tpwrite_sp_color);
        final Spinner tpwrite_sp_line = view.findViewById(R.id.tpwrite_sp_line);

        // 어댑터와 스피너를 연결합니다.
        CustomSpinnerAdapter customSpinnerAdapterColor = new CustomSpinnerAdapter(getActivity().getApplicationContext() , spinnerColorNames ,spinnerColors);
        tpwrite_sp_color.setAdapter(customSpinnerAdapterColor);
        CustomSpinnerAdapter customSpinnerAdapterline = new CustomSpinnerAdapter(getActivity().getApplicationContext() , spinnerLineNames ,spinnerLines);
        tpwrite_sp_line.setAdapter(customSpinnerAdapterline);

        // 스피너에서 아이템 선택시 호출하도록 합니다.
        tpwrite_sp_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selected_color_idx = tpwrite_sp_color.getSelectedItemPosition();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tpwrite_sp_line.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_line_idx = tpwrite_sp_line.getSelectedItemPosition();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            ListPopupWindow window = (ListPopupWindow) popup.get(tpwrite_sp_color);
            window.setModal(false);
        } catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }


    
}