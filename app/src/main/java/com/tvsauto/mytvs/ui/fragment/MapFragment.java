package com.tvsauto.mytvs.ui.fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tvsauto.mytvs.R;
import com.tvsauto.mytvs.holder.Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mapEmployees;
    private MapCallback listener;
    private List<Map> markerDataList;
    private TextView tvTitle, tvEmployees;
    private CardView cardEmpList;
    private List<String> appliedMarkerList = new ArrayList<>();

    public MapFragment() {
    }

    public interface MapCallback {
        void mapClosed();
    }

    public static MapFragment newInstance(List<Map> markerList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("markerDataList", (Serializable) markerList);

        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            markerDataList = (List<Map>) bundle.getSerializable("markerDataList");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        cardEmpList = view.findViewById(R.id.card_emp_names);
        tvEmployees = view.findViewById(R.id.tv_employees);
        tvTitle = view.findViewById(R.id.tv_title);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readBundle(getArguments());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapEmployees = googleMap;
        mapEmployees.getUiSettings().setMapToolbarEnabled(false);
        mapEmployees.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style_2));

        for (int i = 0; i < markerDataList.size(); i++) {
            if (!appliedMarkerList.contains(markerDataList.get(i).getCity())) {
                LatLng city = getCityLatitude(markerDataList.get(i).getCity());
                if (null != city) {
                    appliedMarkerList.add(markerDataList.get(i).getCity());
                    mapEmployees.addMarker(new MarkerOptions().position(city)
                            .title(markerDataList.get(i).getCity()));

                    if (i == 0) {
                        mapEmployees.animateCamera(CameraUpdateFactory.newLatLngZoom(city, 12));
                        setEmployeeList(markerDataList.get(i).getCity());
                    }
                }
            }
        }

        mapEmployees.setOnMarkerClickListener(marker -> {
            setEmployeeList(marker.getTitle());
            CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
            mapEmployees.animateCamera(center, 300, null);
            return true;
        });
    }

    private void setEmployeeList(String cityName) {
        cardEmpList.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.emp_list_title, cityName));
        StringBuilder empList = new StringBuilder();
        for (int i = 0; i < markerDataList.size(); i++) {
            if (markerDataList.get(i).getCity().equals(cityName)) {
                empList.append(markerDataList.get(i).getName()).append("\n");
            }
        }
        tvEmployees.setText(empList.toString());
    }

    public LatLng getCityLatitude(String city) {
        Geocoder geocoder = new Geocoder(getContext(), getContext().getResources().getConfiguration().locale);
        List<Address> addresses;
        LatLng latLng = null;
        try {
            addresses = geocoder.getFromLocationName(city, 1);
            Address address = addresses.get(0);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return latLng;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MapCallback) {
            listener = (MapCallback) getActivity();
        }
    }

    @Override
    public void onDetach() {
        listener.mapClosed();
        super.onDetach();
    }
}
