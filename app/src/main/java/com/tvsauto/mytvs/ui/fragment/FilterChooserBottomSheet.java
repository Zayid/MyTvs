package com.tvsauto.mytvs.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tvsauto.mytvs.R;

/**
 * Created by Zayid on 3/9/2019.
 */

public class FilterChooserBottomSheet extends BottomSheetDialogFragment {

    private ConstraintLayout conMap, conGraph;
    private BottomSheetCallback listener;

    public interface BottomSheetCallback {
        void displayBarChart();

        void displayMap();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottom_sheet_filter, container, false);
        conMap = v.findViewById(R.id.con_map);
        conGraph = v.findViewById(R.id.con_graph);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        conMap.setOnClickListener(view1 -> {
            listener.displayMap();
            dismissAllowingStateLoss();
        });
        conGraph.setOnClickListener(view12 -> {
            listener.displayBarChart();
            dismissAllowingStateLoss();
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomSheetCallback) {
            listener = (BottomSheetCallback) getActivity();
        }
    }
}
