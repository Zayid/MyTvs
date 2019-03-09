package com.tvsauto.mytvs.ui.fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tvsauto.mytvs.R;
import com.tvsauto.mytvs.holder.Map;

import java.io.Serializable;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mapEmployees;
    private MapCallback listener;
    private List<Map> markerList;

    public MapFragment() {
    }

    public interface MapCallback {
        void mapClosed();
    }

    public static MapFragment newInstance(List<Map> markerList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("markerList", (Serializable) markerList);

        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            markerList = (List<Map>) bundle.getSerializable("markerList");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

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

        for (int i = 0; i < markerList.size(); i++) {
            LatLng city = getCityLatitude(markerList.get(i).getCity());
            mapEmployees.addMarker(new MarkerOptions().position(city).title(markerList.get(i).getName()));
            mapEmployees.moveCamera(CameraUpdateFactory.newLatLng(city));
        }
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
