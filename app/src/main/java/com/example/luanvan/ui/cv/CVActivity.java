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
    public static ArrayList<Title> arrayListTTLH, arrayListRemove, arrayListAdd;
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
        arrayListTTLH.add(new Title(1, "Thông tin liên hệ"));
        arrayListRemove.add(new Title(1, "Mục tiêu nghề nghiệp"));
        arrayListRemove.add(new Title(2, "Học vấn"));
        arrayListRemove.add(new Title(3, "Kinh nghiệm làm việc"));
        arrayListAdd.add(new Title(1, "Sở thích"));
        arrayListAdd.add(new Title(2, "Hoạt động"));
        titleAdapterTTLH = new TitleAdapter(getApplicationContext(), arrayListTTLH, 1);
        titleAdapterRemove = new TitleAdapter(getApplicationContext(), arrayListRemove, 0);
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
