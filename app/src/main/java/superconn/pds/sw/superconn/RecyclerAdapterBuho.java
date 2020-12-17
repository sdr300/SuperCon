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
public class RecyclerAdapterBuho extends RecyclerView.Adapter <RecyclerAdapterBuho.MyViewHolder>{

    List<Buho> list;
    ImageView buhoIconImage;
    private GpsTracker gpsTracker;

    public RecyclerAdapterBuho (List<Buho> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_buho, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
//        holder.buhoID.setText(list.get(position).getBuhoID());
        holder.buhoDate.setText(list.get(position).getBuhoDate());
        holder.buhoIconText.setText(list.get(position).getBuhoIcon());
        holder.buhoLatitude.setText(list.get(position).getBuhoLatitude());
        holder.buhoLongitute.setText(list.get(position).getBuhoLongitude());
        holder.buhoCompany.setText(list.get(position).getBuhoCompany());
        buhoIconImage = holder.buhoIconImage;

        if (holder.buhoIconText.getText().toString().trim().equalsIgnoreCase("unknown")){
            buhoIconImage.setImageResource(R.drawable.unknownicon);
        } else  if (holder.buhoIconText.getText().toString().trim().equalsIgnoreCase("our")){
            buhoIconImage.setImageResource(R.drawable.ouricon4);
        } else  if (holder.buhoIconText.getText().toString().trim().equalsIgnoreCase("enemy")){
            buhoIconImage.setImageResource(R.drawable.enemyicon3);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView buhoID, junmun, buhoDate, buhoIcon, buhoLatitude, buhoLongitute, buhoCompany,buhoIconText;
        ImageButton btnEditBuho, btnDeleteBuho;
        ImageView buhoIconImage = itemView.findViewById(R.id.buhoIconImage);
        public  MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            buhoID = itemView.findViewById(R.id.junmun);
            buhoDate = itemView.findViewById(R.id.buhoDate);
            buhoIconText = itemView.findViewById(R.id.buhoIconText);
//            buhoLatitude = itemView.findViewById(R.id.buhoLatitude);
//            buhoLongitute = itemView.findViewById(R.id.buhoLongitude);
            buhoCompany = itemView.findViewById(R.id.buhoCompany);
            btnDeleteBuho = itemView.findViewById(R.id.btnDeleteBuho);
            btnDeleteBuho.setOnClickListener(this);
            btnEditBuho = itemView.findViewById(R.id.btnEditBuho);
            btnEditBuho.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Buho buho = new Buho();
            int ID = list.get(getAdapterPosition()).getBuhoID();
            buho.setBuhoID(ID);

            switch (v.getId()) {
                case R.id.btnDeleteBuho:
                    showDialogBuho();
                    break;

                case R.id.btnEditBuho:
                    Bundle args = new Bundle();
                    args.putInt("ID", list.get(getAdapterPosition()).getBuhoID());
                    args.putString("buhoDate", list.get(getAdapterPosition()).getBuhoDate());
                    args.putString("buhoIcon", list.get(getAdapterPosition()).getBuhoIcon());
                    args.putString("buhoLatitude", list.get(getAdapterPosition()).getBuhoLatitude());
                    args.putString("buhoLongitude", list.get(getAdapterPosition()).getBuhoLongitude());
                    args.putString("buhoCompany", list.get(getAdapterPosition()).getBuhoCompany());
                    args.putString("buhoSender", list.get(getAdapterPosition()).getBuhoSender());

                    UpdateDataFragmentBuho updateDataFragmentBuho = new UpdateDataFragmentBuho();
                    updateDataFragmentBuho.setArguments(args);
                    MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, updateDataFragmentBuho, null).addToBackStack(null).commit();
                    break;
            }
        }

        //삭제시 확인창

        void showDialogBuho() { AlertDialog.Builder msgBuilder = new AlertDialog.Builder(itemView.getContext())
                .setTitle("정말로 삭제하시겠습니까?")
                .setMessage("id:"+ list.get(getAdapterPosition()).getBuhoID()+"\n"
                        +"날짜:"+list.get(getAdapterPosition()).getBuhoDate().replace("\n", "")+"\n"
                        +"군대부호:"+list.get(getAdapterPosition()).getBuhoIcon()+"\n"
                        +"좌표:"+list.get(getAdapterPosition()).getBuhoLatitude()+","+list.get(getAdapterPosition()).getBuhoLongitude()+"\n"
                        +"소속:"+list.get(getAdapterPosition()).getBuhoCompany()+"\n"
                        +"보낸이:"+list.get(getAdapterPosition()).getBuhoSender()+"\n")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Buho buho = new Buho();
                        int BuhoID = list.get(getAdapterPosition()).getBuhoID();
                        buho.setBuhoID(BuhoID);
                        MapActivity.roomDatabaseClass.buhoDao().deleteBuho(buho);
                        Toast.makeText(itemView.getContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
                        MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentBuho(), null).commit();
                    }
                }) .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(itemView.getContext(), "삭제 취소", Toast.LENGTH_SHORT).show();
                        MapActivity.fragmentManager.beginTransaction().replace(R.id.fragment_frame, new FragmentBuho(), null).commit();
                    }
                });
            AlertDialog msgDlg = msgBuilder.create();
            msgDlg.show(); }
    }
}
