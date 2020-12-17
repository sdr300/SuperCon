package superconn.pds.sw.superconn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FragmentPia extends Fragment implements View.OnClickListener{

    private ImageButton btnAdd_view ;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerAdapterPia recyclerAdapterPia;



    public FragmentPia() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_pia, container, false);
       recyclerView = view.findViewById(R.id.recyclerView);
       layoutManager=new LinearLayoutManager(getActivity());
       recyclerView.setLayoutManager(layoutManager);

       //작성 버튼
       btnAdd_view=view.findViewById(R.id.btn_add_pia);
       btnAdd_view.setOnClickListener(this);

        List<Person> list = MapActivity.roomDatabaseClass.personDao().getPerson();

        recyclerAdapterPia = new RecyclerAdapterPia(list);
        recyclerView.setAdapter(recyclerAdapterPia);

       return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_pia:
                try {
                MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new AddDataFragmentPia(), null).addToBackStack(null).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}