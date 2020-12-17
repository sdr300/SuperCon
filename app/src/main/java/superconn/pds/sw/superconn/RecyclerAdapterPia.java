package superconn.pds.sw.superconn;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import superconn.pds.sw.superconn.DataBase.Person;

/**
 * created 2020-11-02
 */
public class RecyclerAdapterPia extends RecyclerView.Adapter <RecyclerAdapterPia.MyViewHolder>{

    List<Person> list;

    public RecyclerAdapterPia(List<Person> list) {
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_pia, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
//        holder.tvID.setText(String.valueOf(list.get(position).getPiaId()));
        holder.tvDate.setText(list.get(position).getPiaDate());
        holder.tvQna.setText(list.get(position).getPiaQna());
        holder.tvResult.setText(list.get(position).getPiaResult());
        holder.tvCompany.setText(list.get(position).getPiaCompany());


        //글씨색 변화
        holder.tvQna.setTextColor(Color.parseColor("#000000"));

        if (String.valueOf(list.get(position).getPiaResult().trim()).equals("아군")){
            holder.tvResult.setTextColor(Color.parseColor("#00B050"));
        } else  if (String.valueOf(list.get(position).getPiaResult()).equals("적군")){
            holder.tvResult.setTextColor(Color.parseColor("#FF0000"));
        } else{
            holder.tvResult.setTextColor(Color.parseColor("#000000"));
        }

        if (String.valueOf(list.get(position).getPiaCompany().trim()).equals("미식별")){
            holder.tvCompany.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.tvCompany.setTextColor(Color.parseColor("#000000"));
        }

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvID, tvDate, tvQna, tvResult, tvCompany;
        ImageButton btnEdit, btnDelete;


        public  MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate =  itemView.findViewById(R.id.piaDate);
            tvQna = itemView.findViewById(R.id.piaQna);
            tvResult =  itemView.findViewById(R.id.piaResult);
            tvCompany = itemView.findViewById(R.id.piaCompany);

            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDelete.setOnClickListener(this);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnEdit.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            Person person = new Person();
            int ID = list.get(getAdapterPosition()).getPiaID();
            person.setPiaID(ID);

            switch (v.getId()) {
                case R.id.btnDelete:
                    showDialog();
                    break;

                case R.id.btnEdit:
                    Bundle args = new Bundle();
                    args.putInt("ID", list.get(getAdapterPosition()).getPiaID());
                    args.putString("piaDate", list.get(getAdapterPosition()).getPiaDate());
                    args.putString("piaQna", list.get(getAdapterPosition()).getPiaQna());
                    args.putString("piaResult", list.get(getAdapterPosition()).getPiaResult());
                    args.putString("piaCompany", list.get(getAdapterPosition()).getPiaCompany());


                    UpdateDataFragmentPia updateDataFragmentPia = new UpdateDataFragmentPia();
                    updateDataFragmentPia.setArguments(args);
                    MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, updateDataFragmentPia, null).addToBackStack(null).commit();
                    break;


            }
        }

        //삭제시 확인창

        void showDialog() { AlertDialog.Builder msgBuilder = new AlertDialog.Builder(itemView.getContext())
                .setTitle("정말로 삭제하시겠습니까?")
                .setMessage("id:"+ list.get(getAdapterPosition()).getPiaID()+"\n"
                        +"날짜:"+list.get(getAdapterPosition()).getPiaDate().replace("\n", "")+"\n"
                        +"질/응:"+list.get(getAdapterPosition()).getPiaQna()+"\n"
                        +"결과:"+list.get(getAdapterPosition()).getPiaResult()+"\n"
                        +"소속:"+list.get(getAdapterPosition()).getPiaCompany()+"\n")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Person person = new Person();
                        int PiaID = list.get(getAdapterPosition()).getPiaID();
                        person.setPiaID(PiaID);
                        MapActivity.roomDatabaseClass.personDao().deletePerson(person);
                        Toast.makeText(itemView.getContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
                        MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentPia(), null).commit();

                    }
                }) .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(itemView.getContext(), "삭제 취소", Toast.LENGTH_SHORT).show();
                        MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentPia(), null).commit();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show(); }
    }

}

