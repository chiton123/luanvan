package com.example.luanvan.ui.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.cv.CVActivity;
import com.example.luanvan.ui.cv.CVIntroductionActivity;
import com.example.luanvan.ui.cv.CVShowActivity;
import com.example.luanvan.ui.modelCV.PdfCV;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class CVAdapter extends RecyclerView.Adapter<CVAdapter.ItemHolder> {
    Context context;
    ArrayList<PdfCV> arrayList;
    Activity activity;
    int REQUEST_CODE = 123;

    public CVAdapter(Context context, ArrayList<PdfCV> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_cv, null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }
    // check xem trong CVinfo có những thông tin gì, experience, study, ...


    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        PdfCV pdfCV = arrayList.get(position);
        holder.name.setText(pdfCV.getName());
        holder.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CVShowActivity.class);
                intent.putExtra("url", arrayList.get(position).getUrl());
                activity.startActivity(intent);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Thông báo");
                alert.setMessage("Bạn muốn xóa CV này không ? ");
                alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // Toast.makeText(context, arrayList.get(position).getKey(), Toast.LENGTH_SHORT).show();
                        MainActivity.mData.child("cv").child(MainActivity.uid).child(arrayList.get(position).getKey()).removeValue();
                        MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(arrayList.get(position).getKey()).removeValue();
                        CVIntroductionActivity.arrayListCV.remove(position);
                        notifyDataSetChanged();

                    }
                });
                alert.show();

            }
        });
        holder.btnAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kind: 1 add, kind: 2 update
                Intent intent = new Intent(context, CVActivity.class);
                intent.putExtra("kind",2);
                intent.putExtra("url", arrayList.get(position).getUrl());
                intent.putExtra("cvname", arrayList.get(position).getName());
                intent.putExtra("key", arrayList.get(position).getKey());
                CVIntroductionActivity.position = position;
                activity.startActivityForResult(intent, REQUEST_CODE);
            }
        });

        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference reference = MainActivity.storage.getReferenceFromUrl(arrayList.get(position).getUrl());
          //      StorageReference  islandRef = storageRef.child("file.txt");
                File rootPath = new File(Environment.getExternalStorageDirectory(), "CVdownload");
                if(!rootPath.exists()) {
                    rootPath.mkdirs();
                }
                final File localFile = new File(rootPath,arrayList.get(position).getName() + ".pdf");
                reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(context, "Lưu thành công", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(context, "Lưu thất bại", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView img;
        public Button btnShow, btnAdjust, btnDelete, btnDownload;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            btnAdjust = (Button) itemView.findViewById(R.id.adjust);
            btnDelete = (Button) itemView.findViewById(R.id.delete);
            btnDownload = (Button) itemView.findViewById(R.id.download);
            btnShow = (Button) itemView.findViewById(R.id.show);
            img = (ImageView) itemView.findViewById(R.id.img);


        }
    }
}
