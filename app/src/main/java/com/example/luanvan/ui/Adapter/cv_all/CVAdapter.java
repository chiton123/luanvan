package com.example.luanvan.ui.Adapter.cv_all;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.cv.CVActivity;
import com.example.luanvan.ui.cv.CVIntroductionActivity;
import com.example.luanvan.ui.cv.CVShowActivity;
import com.example.luanvan.ui.modelCV.PdfCV;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CVAdapter extends RecyclerView.Adapter<CVAdapter.ItemHolder> {
    Context context;
    ArrayList<PdfCV> arrayList;
    Activity activity;
    int REQUEST_CODE = 123;
    ProgressDialog progressDialog;

    public CVAdapter(Context context, ArrayList<PdfCV> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_cv, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }
    // check xem trong CVinfo có những thông tin gì, experience, study, ...


    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        PdfCV pdfCV = arrayList.get(position);
        holder.name.setText(pdfCV.getName());

        holder.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CVShowActivity.class);
                intent.putExtra("kind", 1);  // 1: show cv , 2: job apply
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
                        // Check coi có trong application k, rồi mới dc xóa
                        RequestQueue requestQueue = Volley.newRequestQueue(activity);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCheckCVInApplication,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("fail")){
                                            deleteMysql(position);

                                        }else {
                                            FancyToast.makeText(context,"CV đã ứng tuyển nên không được xóa", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        FancyToast.makeText(context, error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map = new HashMap<>();
                                map.put("iduser", String.valueOf(MainActivity.iduser));
                                map.put("idcv",arrayList.get(position).getKey());
                                return map;
                            }
                        };
                        requestQueue.add(stringRequest);
                       // Toast.makeText(context, arrayList.get(position).getKey(), Toast.LENGTH_SHORT).show();


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
                intent.putExtra("position", position);
                intent.putExtra("style", arrayList.get(position).getStyle());
                CVIntroductionActivity.position = position;
                activity.startActivityForResult(intent, REQUEST_CODE);
            }
        });

        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
                StorageReference reference = MainActivity.storage.getReferenceFromUrl(arrayList.get(position).getUrl());
                File rootPath = new File(Environment.getExternalStorageDirectory(), "/Documents/CVdownload");
                if(!rootPath.exists()) {
                    rootPath.mkdirs();
                }
                final File localFile = new File(rootPath,arrayList.get(position).getName() + ".pdf");
                reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        FancyToast.makeText(context, "Tải thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        FancyToast.makeText(context,"Tải thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });

            }
        });
        holder.btnPutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlPutMain,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("success")){
                                    FancyToast.makeText(context, "Cập nhật thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
//                                    holder.btnPutMain.setText("CV chính");
                                    notifyDataSetChanged();

                                }else {
                                    FancyToast.makeText(context,"Cập nhật thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                FancyToast.makeText(context,error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();
                        map.put("iduser", String.valueOf(MainActivity.iduser));
                        map.put("idcv", String.valueOf(arrayList.get(position).getKey()));
                        return map;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCheckMainCV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                        if(response.equals("success")){
                            holder.btnPutMain.setText("CV chính");
                        }else {
                            //Toast.makeText(context, "Kiểm tra CV chính thất bại", Toast.LENGTH_SHORT).show();
                            holder.btnPutMain.setText("Đặt CV chính");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(context, error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("iduser", String.valueOf(MainActivity.iduser));
                map.put("idcv", String.valueOf(arrayList.get(position).getKey()));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    void loading(){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Vui lòng chờ");
        progressDialog.setMessage("CV đang được tải về");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    private void deleteMysql(final int position) {
       // Toast.makeText(context, "iduser " + MainActivity.iduser + " idcv " + arrayList.get(position).getKey(), Toast.LENGTH_SHORT).show();
        final RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlDeleteCV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            StorageReference pdfDelete = FirebaseStorage.getInstance().getReferenceFromUrl(arrayList.get(position).getUrl());
                            pdfDelete.delete();
                          //  Toast.makeText(context, "Xóa lên Mysql thành công", Toast.LENGTH_SHORT).show();
                            MainActivity.mData.child("cv").child(MainActivity.uid).child(arrayList.get(position).getKey()).removeValue();
                            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(arrayList.get(position).getKey()).removeValue();
                            arrayList.remove(position);
                            notifyDataSetChanged();
                            ((CVIntroductionActivity)activity).checkNothing();
                            FancyToast.makeText(context, "Xóa thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                        }else {
                            FancyToast.makeText(context,"Xóa lên Mysql thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(context, error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("iduser", String.valueOf(MainActivity.iduser));
                map.put("idcv", String.valueOf(arrayList.get(position).getKey()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView img;
        public Button btnShow, btnAdjust, btnDelete, btnDownload, btnPutMain;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            btnAdjust = (Button) itemView.findViewById(R.id.adjust);
            btnDelete = (Button) itemView.findViewById(R.id.delete);
            btnDownload = (Button) itemView.findViewById(R.id.download);
            btnShow = (Button) itemView.findViewById(R.id.show);
            img = (ImageView) itemView.findViewById(R.id.img);
            btnPutMain = (Button) itemView.findViewById(R.id.buttonmain);


        }
    }
}
