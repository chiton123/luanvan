package com.example.luanvan.ui.cv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.add_remove.AddAdapter;
import com.example.luanvan.ui.Adapter.add_remove.TitleAdapter;
import com.example.luanvan.ui.Model.Title;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

public class CVActivity extends AppCompatActivity {
    Toolbar toolbar;
    PDFView pdfView;
    Button btndoimau, btntuychinh, btnnoidung;
    Dialog dialog;
    ListView listViewThongtinLienHe, listViewRemove, listViewAdd;
    public static TitleAdapter titleAdapterTTLH, titleAdapterRemove;
    public static ArrayList<Title> arrayListTTLH, arrayListRemove, arrayListAdd, arrayListTongHop;
    public static AddAdapter addAdapter;
    ImageView imgCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v);
        anhxa();
        actionBar();
        eventPDF();
        eventButton();



    }

    private void eventButton() {
        btnnoidung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDialog();
            }
        });

    }

    private void eventDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cv);
        dialog.setCancelable(false);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        listViewThongtinLienHe = (ListView) dialog.findViewById(R.id.listviewthongtinlienhe);
        listViewRemove = (ListView) dialog.findViewById(R.id.listviewRemove);
        listViewAdd = (ListView) dialog.findViewById(R.id.listviewAdd);
        imgCancel = (ImageView) dialog.findViewById(R.id.cancel);
        arrayListRemove = new ArrayList<>();
        arrayListTTLH = new ArrayList<>();
        arrayListAdd = new ArrayList<>();
        arrayListTongHop = new ArrayList<>();
        arrayListTongHop.add(new Title(0, "Thông tin liên hệ"));
        arrayListTongHop.add(new Title(1, "Mục tiêu nghề nghiệp"));
        arrayListTongHop.add(new Title(2, "Học vấn"));
        arrayListTongHop.add(new Title(3, "Kinh nghiệm làm việc"));
        arrayListTongHop.add(new Title(4, "Kỹ năng"));
        arrayListTongHop.add(new Title(5, "Hoạt động"));


        arrayListRemove.add(arrayListTongHop.get(0));
        arrayListRemove.add(arrayListTongHop.get(1));
        arrayListRemove.add(arrayListTongHop.get(2));
        arrayListRemove.add(arrayListTongHop.get(3));
        arrayListAdd.add(arrayListTongHop.get(4));
        arrayListAdd.add(arrayListTongHop.get(5));
        titleAdapterTTLH = new TitleAdapter(getApplicationContext(), arrayListTTLH, this, 1);
        titleAdapterRemove = new TitleAdapter(getApplicationContext(), arrayListRemove, this, 0);
        addAdapter = new AddAdapter(getApplicationContext(), arrayListAdd);
        listViewAdd.setAdapter(addAdapter);
        listViewThongtinLienHe.setAdapter(titleAdapterTTLH);
        listViewRemove.setAdapter(titleAdapterRemove);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void eventPDF() {
        pdfView.fromAsset("cv.pdf").load();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cv, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pdfView = (PDFView) findViewById(R.id.pdfview);
        btndoimau = (Button) findViewById(R.id.buttondoimau);
        btntuychinh = (Button) findViewById(R.id.buttontuychinh);
        btnnoidung = (Button) findViewById(R.id.buttonnoidung);



    }
}
