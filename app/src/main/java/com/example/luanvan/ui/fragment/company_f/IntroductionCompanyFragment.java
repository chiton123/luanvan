package com.example.luanvan.ui.fragment.company_f;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Company;
import com.example.luanvan.ui.company.CompanyActivity;
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

public class IntroductionCompanyFragment extends Fragment implements OnMapReadyCallback {
    TextView txtIntroduction, txtAddress;
    private GoogleMap mMap;
    MapView mMapView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_introduction_company, container, false);
        txtAddress = (TextView) view.findViewById(R.id.diachi);
        txtIntroduction = (TextView) view.findViewById(R.id.gioithieu);
        mMapView = (MapView) view.findViewById(R.id.mapview);

        mMapView.onCreate(savedInstanceState);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // Add a marker in Sydney and move the camera
                LatLng store = new LatLng(CompanyActivity.company.getVido(), CompanyActivity.company.getKinhdo());
              //  Toast.makeText(getActivity(), CompanyActivity.company.getVido() + "", Toast.LENGTH_SHORT).show();
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
        getData();

        return view;
    }


    private void getData() {
        txtIntroduction.setText(CompanyActivity.company.getIntroduction());
        txtAddress.setText(CompanyActivity.company.getAddress());


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng store = new LatLng(CompanyActivity.company.getVido(), CompanyActivity.company.getKinhdo());
        //   Toast.makeText(getApplicationContext(), getIntent().getIntExtra("vido", 0) +"" + getIntent().getIntExtra("kinhdo",0), Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(store).title("Store")).setIcon(BitmapDescriptorFactory.defaultMarker());
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(store).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

}
