package superconn.pds.sw.superconn;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import superconn.pds.sw.superconn.DataBase.Buho;

/**
 * created 2020-11-17
 */
public class RecyclerAdapterJunmun extends RecyclerView.Adapter <RecyclerAdapterJunmun.MyViewHolder>{

    List<Buho> list;
    ImageView buhoIconImage;

    public RecyclerAdapterJunmun(List<Buho> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_junmun, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
//        holder.buhoID.setText(list.get(position).getBuhoID());
        holder.junmunSender.setText(list.get(position).getBuhoSender());
        holder.junmunDate.setText(list.get(position).getBuhoDate());
        holder.junmunLatitude.setText(list.get(position).getBuhoLatitude());
        holder.junmunLongitude.setText(list.get(position).getBuhoLongitude());
        holder.junmunCompany.setText(list.get(position).getBuhoCompany());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView junmunID,junmunSender, junmunDate, junmunCompany, junmunLatitude, junmunLongitude;
        ImageButton btnEditJunmun, btnDeleteJunmun;
        public  MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            buhoID = itemView.findViewById(R.id.junmunID);
            junmunSender = itemView.findViewById(R.id.junmunSender);
            junmunDate = itemView.findViewById(R.id.junmunDate);
//            junmunLatitude = itemView.findViewById(R.id.junmunLatitude);
//            junmunLongitude = itemView.findViewById(R.id.junmunLongitude);
            junmunCompany = itemView.findViewById(R.id.junmunCompany);
            btnDeleteJunmun = itemView.findViewById(R.id.btnDeleteJunmun);
            btnDeleteJunmun.setOnClickListener(this);
            btnEditJunmun = itemView.findViewById(R.id.btnEditJunmun);
            btnEditJunmun.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Buho buho = new Buho();
            int ID = list.get(getAdapterPosition()).getBuhoID();
            buho.setBuhoID(ID);

            switch (v.getId()) {
                case R.id.btnDeleteJunmun:
                    showDialogJunmun();
                    break;

                case R.id.btnEditJunmun:
                    Bundle args = new Bundle();
                    args.putInt("ID", list.get(getAdapterPosition()).getBuhoID());
                    args.putString("buhoDate", list.get(getAdapterPosition()).getBuhoDate());
                    args.putString("buhoIcon", list.get(getAdapterPosition()).getBuhoIcon());
                    args.putString("buhoLatitude", list.get(getAdapterPosition()).getBuhoLatitude());
                    args.putString("buhoLongitude", list.get(getAdapterPosition()).getBuhoLongitude());
                    args.putString("buhoCompany", list.get(getAdapterPosition()).getBuhoCompany());
                    args.putString("buhoSender", list.get(getAdapterPosition()).getBuhoSender());

//                    UpdateDataFragmentBuho updateDataFragmentBuho = new UpdateDataFragmentBuho();
//                    updateDataFragmentBuho.setArguments(args);
//                    MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, updateDataFragmentBuho, null).addToBackStack(null).commit();
                    break;
            }
        }

        //삭제시 확인창

        void showDialogJunmun() { AlertDialog.Builder msgBuilder = new AlertDialog.Builder(itemView.getContext())
                .setTitle("정말로 삭제하시겠습니까?")
                .setMessage("id:"+ list.get(getAdapterPosition()).getBuhoID()+"\n"
                        +"보낸이:"+list.get(getAdapterPosition()).getBuhoSender()+"\n"
                        +"날짜:"+list.get(getAdapterPosition()).getBuhoDate().replace("\n", "")+"\n"
                        +"좌표:"+list.get(getAdapterPosition()).getBuhoLatitude()+","+list.get(getAdapterPosition()).getBuhoLongitude()+"\n"
                        +"소속:"+list.get(getAdapterPosition()).getBuhoCompany()+"\n"
                )
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Buho buho = new Buho();
                        int BuhoID = list.get(getAdapterPosition()).getBuhoID();
                        buho.setBuhoID(BuhoID);
                        MapActivity.roomDatabaseClass.buhoDao().deleteBuho(buho);
                        Toast.makeText(itemView.getContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
                        MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentJunmunLocation(), null).commit();
                    }
                }) .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(itemView.getContext(), "삭제 취소", Toast.LENGTH_SHORT).show();
                        MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentJunmunLocation(), null).commit();
                    }
                });
            AlertDialog msgDlg = msgBuilder.create();
            msgDlg.show(); }
    }
}


