package com.example.luanvan.ui.company;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.ViewPageAdapter;
import com.example.luanvan.ui.Model.Company;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

public class CompanyAdminActivity extends AppCompatActivity{
    Toolbar toolbar;
    ImageView img, img_background;
    TextView txtCompanyName, txtSize;
    public static Company company;
    TextView txtIntroduction, txtAddress;
    private GoogleMap mMap;
    ScrollView mainScrollView;
    ImageView transparentImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_admin);
        anhxa();
        actionBar();
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                mMap = googleMap;
                MarkerOptions markerOptions = new MarkerOptions();
                // set position of marker

                LatLng store = new LatLng(company.getVido(), company.getKinhdo());
                markerOptions.position(store);
                markerOptions.title("Công ty");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
                // zoom
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(store, 25));
                mMap.addMarker(markerOptions);

            }
        });
        scrollAll();

        getInfo();

    }

    private void scrollAll() {
        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    private void getInfo() {

        company = (Company) getIntent().getSerializableExtra("company");
        if(company != null){
            txtCompanyName.setText(company.getName());
            Glide.with(getApplicationContext()).load(company.getImage()).into(img);
            Glide.with(getApplicationContext()).load(company.getImage_backgroud()).into(img_background);
            txtSize.setText(company.getSize());
            txtIntroduction.setText(company.getIntroduction());
            txtAddress.setText(company.getAddress());
        }

    }




    private void anhxa() {
        txtAddress = (TextView) findViewById(R.id.diachi);
        txtIntroduction = (TextView) findViewById(R.id.gioithieu);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtCompanyName = (TextView) findViewById(R.id.tencongty);
        img = (ImageView) findViewById(R.id.img);
        img_background = (ImageView) findViewById(R.id.img_background);
        txtSize = (TextView) findViewById(R.id.txtsize);
        mainScrollView = (ScrollView) findViewById(R.id.main_scrollview);
        transparentImageView = (ImageView) findViewById(R.id.transparent_image);
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
}
