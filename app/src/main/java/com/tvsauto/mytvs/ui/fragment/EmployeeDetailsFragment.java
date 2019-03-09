package com.tvsauto.mytvs.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tvsauto.mytvs.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class EmployeeDetailsFragment extends Fragment {

    private Toolbar toolbar;
    private String empName, empLocation, empDesignation, empSalary, empJoiningDate;
    private TextView tvEmpName, tvEmpLocation, tvEmpDesignation, tvEmpSalary, tvEmpJoiningDate,
            tvLogo, tvTimestamp;
    private CardView cardTakePic;
    private ImageView ivProPic;
    static final int REQUEST_IMAGE_CAPTURE = 23;
    private DetailsCallback listener;

    public EmployeeDetailsFragment() {
    }

    public interface DetailsCallback {
        void detailsClosed();
    }

    public static EmployeeDetailsFragment newInstance(String name, String location, String designation,
                                                      String salary, String joiningDate) {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("location", location);
        bundle.putString("designation", designation);
        bundle.putString("salary", salary);
        bundle.putString("joiningDate", joiningDate);

        EmployeeDetailsFragment fragment = new EmployeeDetailsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            empName = bundle.getString("name");
            empLocation = bundle.getString("location");
            empDesignation = bundle.getString("designation");
            empSalary = bundle.getString("salary");
            empJoiningDate = bundle.getString("joiningDate");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_details, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        tvEmpName = view.findViewById(R.id.tv_emp_name);
        tvEmpLocation = view.findViewById(R.id.tv_emp_location);
        tvEmpDesignation = view.findViewById(R.id.tv_emp_designation);
        tvEmpSalary = view.findViewById(R.id.tv_emp_salary);
        tvEmpJoiningDate = view.findViewById(R.id.tv_emp_joining_date);
        tvLogo = view.findViewById(R.id.tv_logo);
        tvTimestamp = view.findViewById(R.id.tv_timestamp);
        cardTakePic = view.findViewById(R.id.card_take_pic);
        ivProPic = view.findViewById(R.id.iv_profile_pic);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        readBundle(getArguments());
        initView();
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

    private void initView() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        tvEmpName.setText(empName);
        tvEmpLocation.setText(empLocation);
        tvEmpDesignation.setText(empDesignation);
        tvEmpSalary.setText(empSalary);
        tvEmpJoiningDate.setText(empJoiningDate);

        tvLogo.setText(empName.substring(0, 1));

        cardTakePic.setOnClickListener(view -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivProPic.setImageBitmap(imageBitmap);
            ivProPic.setVisibility(View.VISIBLE);
            tvLogo.setVisibility(View.GONE);

            setTimestamp();
        }
    }

    private void setTimestamp() {
        String currentDateTime;

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTime = sdf1.format(new Date());
        tvTimestamp.setText(currentDateTime);
        tvTimestamp.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailsCallback) {
            listener = (DetailsCallback) getActivity();
        }
    }

    @Override
    public void onDetach() {
        listener.detailsClosed();
        super.onDetach();
    }
}
