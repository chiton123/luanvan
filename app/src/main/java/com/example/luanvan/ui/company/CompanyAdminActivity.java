package com.example.luanvan.ui.company;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

public class CompanyAdminActivity extends AppCompatActivity implements OnMapReadyCallback{
    Toolbar toolbar;
    ImageView img, img_background;
    TextView txtCompanyName, txtSize;
    public static Company company;
    TextView txtIntroduction, txtAddress;
    private GoogleMap mMap;
    MapView mMapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_admin);
        anhxa();
        actionBar();
        mMapView.onCreate(savedInstanceState);

        getInfo();
        createMap();
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


    private void createMap() {
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // Add a marker in Sydney and move the camera
                LatLng store = new LatLng(company.getVido(), company.getKinhdo());

                mMap.addMarker(new MarkerOptions().position(store).title("Công ty")).setIcon(BitmapDescriptorFactory.defaultMarker());
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(store).zoom(16).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.setMaxZoomPreference(15.0f);
                mMap.setMinZoomPreference(6.0f);

                //     mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                //    -- Để sau
//                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(LatLng latLng) {
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        markerOptions.position(latLng);
//                        markerOptions.title(latLng.latitude + " : "+ latLng.longitude);
//                        // Clear previously click position.
//                        mMap.clear();
//                        // Zoom the Marker
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
//                        // Add Marker on Map
//                        mMap.addMarker(markerOptions);
//                    }
//                });
            }
        });
    }

    private void anhxa() {
        txtAddress = (TextView) findViewById(R.id.diachi);
        txtIntroduction = (TextView) findViewById(R.id.gioithieu);
        mMapView = (MapView) findViewById(R.id.mapview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtCompanyName = (TextView) findViewById(R.id.tencongty);
        img = (ImageView) findViewById(R.id.img);
        img_background = (ImageView) findViewById(R.id.img_background);
        txtSize = (TextView) findViewById(R.id.txtsize);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng store = new LatLng(company.getVido(), company.getKinhdo());
        //   Toast.makeText(getApplicationContext(), getIntent().getIntExtra("vido", 0) +"" + getIntent().getIntExtra("kinhdo",0), Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(store).title("Store")).setIcon(BitmapDescriptorFactory.defaultMarker());
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(store).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
