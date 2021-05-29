package com.example.luanvan.ui.fragment.company_f;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
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

public class IntroductionCompanyFragment extends Fragment {
    TextView txtIntroduction, txtAddress;
    private GoogleMap mMap;
   // MapView mMapView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_introduction_company, container, false);
        txtAddress = (TextView) view.findViewById(R.id.diachi);
        txtIntroduction = (TextView) view.findViewById(R.id.gioithieu);
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().
                findFragmentById(R.id.map);
        final ScrollView mainScrollView = (ScrollView) view.findViewById(R.id.main_scrollview);
        ImageView transparentImageView = (ImageView) view.findViewById(R.id.transparent_image);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                mMap = googleMap;
                MarkerOptions markerOptions = new MarkerOptions();
                // set position of marker

                LatLng store = new LatLng(CompanyActivity.company.getVido(), CompanyActivity.company.getKinhdo());
                markerOptions.position(store);
                markerOptions.title("Công ty");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
                // zoom
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(store, 25));
                mMap.addMarker(markerOptions);
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
              //  mMap.getUiSettings().setScrollGesturesEnabled(false);
//                mMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);
//                mMap.getUiSettings().setZoomControlsEnabled(true);

            }
        });
        getData();
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
        return view;
    }


    private void getData() {
        txtIntroduction.setText(CompanyActivity.company.getIntroduction());
        txtAddress.setText(CompanyActivity.company.getAddress());


    }




}
