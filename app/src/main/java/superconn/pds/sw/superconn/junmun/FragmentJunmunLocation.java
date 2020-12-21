package superconn.pds.sw.superconn.junmun;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import superconn.pds.sw.superconn.DataBase.Buho;
import superconn.pds.sw.superconn.MapActivity;
import superconn.pds.sw.superconn.R;
import superconn.pds.sw.superconn.RecyclerAdapterJunmun;

public class FragmentJunmunLocation extends Fragment implements View.OnClickListener{


    private ImageButton btnAdd_view ;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    RecyclerAdapterJunmun recyclerAdapterJunmun;


    public FragmentJunmunLocation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_junmun_location, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewJunmun);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //작성 버튼
        btnAdd_view=view.findViewById(R.id.btn_add_junmun);
        btnAdd_view.setOnClickListener(this);

        List<Buho> list = MapActivity.roomDatabaseClass.buhoDao().getBuho();

        recyclerAdapterJunmun = new RecyclerAdapterJunmun(list);
        recyclerView.setAdapter(recyclerAdapterJunmun);

        return view;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_junmun:
                try {

                    MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentJunmunLocation(), null).addToBackStack(null).commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}