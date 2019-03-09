package com.tvsauto.mytvs.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.tvsauto.mytvs.R;
import com.tvsauto.mytvs.holder.Graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GraphFragment extends Fragment {

    private AnyChartView chartSalary;
    private List<Graph> graphList;
    private GraphCallback listener;

    public GraphFragment() {
    }

    public interface GraphCallback {
        void graphClosed();
    }

    public static GraphFragment newInstance(List<Graph> graphList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("graphList", (Serializable) graphList);

        GraphFragment fragment = new GraphFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            graphList = (List<Graph>) bundle.getSerializable("graphList");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        chartSalary = view.findViewById(R.id.chart_salary);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        readBundle(getArguments());
        generateGraph();
    }

    private void generateGraph() {
        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data = new ArrayList<>();

        for (int i = 0; i < graphList.size(); i++) {
            data.add(new ValueDataEntry(graphList.get(i).getName(),
                    Integer.parseInt(graphList.get(i).getSalary().replace("$", "")
                            .replaceAll(",", ""))));
        }

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Salary of first 10 Employees");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Employee");
        cartesian.yAxis(0).title("Salary");

        chartSalary.setChart(cartesian);
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
        if (context instanceof GraphCallback) {
            listener = (GraphCallback) getActivity();
        }
    }

    @Override
    public void onDetach() {
        listener.graphClosed();
        super.onDetach();
    }
}
