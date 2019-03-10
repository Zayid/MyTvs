package com.tvsauto.mytvs.ui.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.anychart.core.annotations.Line;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tvsauto.mytvs.R;
import com.tvsauto.mytvs.holder.Employee;
import com.tvsauto.mytvs.holder.Graph;
import com.tvsauto.mytvs.holder.Map;
import com.tvsauto.mytvs.ui.adapter.EmpListAdapter;
import com.tvsauto.mytvs.ui.fragment.EmployeeDetailsFragment;
import com.tvsauto.mytvs.ui.fragment.FilterChooserBottomSheet;
import com.tvsauto.mytvs.ui.fragment.GraphFragment;
import com.tvsauto.mytvs.ui.fragment.MapFragment;

import java.util.ArrayList;
import java.util.List;


public class EmployeeListActivity extends AppCompatActivity implements EmpListAdapter.AdapterCallback,
        FilterChooserBottomSheet.BottomSheetCallback, MapFragment.MapCallback, GraphFragment.GraphCallback,
        EmployeeDetailsFragment.DetailsCallback {

    private String strEmpList;
    private Employee empList;
    private RecyclerView rvEmployees;
    private EmpListAdapter empListAdapter;
    private Toolbar toolbar;
    private SearchView searchView;
    private FloatingActionButton fabFilter;
    private FilterChooserBottomSheet filterChooserBottomSheet;
    private CardView cardClearSearch;
    private LinearLayout llNoResultLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvEmployees = findViewById(R.id.rv_employees);
        fabFilter = findViewById(R.id.fab_filter);
        cardClearSearch = findViewById(R.id.card_clear_search);
        llNoResultLayout = findViewById(R.id.ll_no_result);

        strEmpList = getIntent().getStringExtra("empList");
        generateDataSet();

        fabFilter.setOnClickListener(view -> showBottomSheet());
        cardClearSearch.setOnClickListener(view -> {
            empListAdapter.updateList(empList);
            cardClearSearch.setVisibility(View.GONE);
            llNoResultLayout.setVisibility(View.GONE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint("Search Name or Designation");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cardClearSearch.setVisibility(View.VISIBLE);
                filter(query);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    private void generateDataSet() {
        Gson gson = new GsonBuilder().create();
        empList = gson.fromJson(strEmpList, Employee.class);

        inflateRv();
    }

    private void inflateRv() {
        empListAdapter = new EmpListAdapter(empList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvEmployees.setLayoutManager(mLayoutManager);
        rvEmployees.setAdapter(empListAdapter);
    }

    @Override
    public void inflateDetailFragment(String name, String location, String designation, String salary, String joiningDate) {
        Fragment empDetailsFragment = EmployeeDetailsFragment.newInstance(name, location, designation, salary, joiningDate);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.con_root, empDetailsFragment);
        ft.addToBackStack("empDetails");
        ft.commit();

        fabFilter.hide();
    }

    private void filter(String text) {
        Employee tempEmp = new Employee();
        List<List<String>> temp = new ArrayList<>();
        for (List<String> d : empList.getData()) {
            String tmpName = d.get(0).toLowerCase();
            String tmpDesignation = d.get(1).toLowerCase();
            if (tmpName.contains(text.toLowerCase()) || tmpDesignation.contains(text.toLowerCase())) {
                temp.add(d);
            }
        }

        tempEmp.setData(temp);
        empListAdapter.updateList(tempEmp);

        if (temp.size() == 0) {
            llNoResultLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showBottomSheet() {
        filterChooserBottomSheet = new FilterChooserBottomSheet();
        filterChooserBottomSheet.show(getSupportFragmentManager(), "bottom_sheet_filter");
    }

    @Override
    public void displayBarChart() {

        List<Graph> graphList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Graph graph = new Graph(
                    empList.getData().get(i).get(0),
                    empList.getData().get(i).get(5)
            );

            graphList.add(graph);
        }

        Fragment graphFragment = GraphFragment.newInstance(graphList);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.con_root, graphFragment);
        ft.addToBackStack("chartSalary");
        ft.commit();

        fabFilter.hide();
    }

    @Override
    public void displayMap() {
        fabFilter.hide();
        List<Map> mapList = new ArrayList<>();
        for (int i = 0; i < empList.getData().size(); i++) {
            Map graph = new Map(
                    empList.getData().get(i).get(0),
                    empList.getData().get(i).get(2)
            );

            mapList.add(graph);
        }
        Fragment mapFragment = MapFragment.newInstance(mapList);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.con_root, mapFragment);
        ft.addToBackStack("mapEmployees");
        ft.commit();

    }

    @Override
    public void mapClosed() {
        fabFilter.show();
    }

    @Override
    public void graphClosed() {
        fabFilter.show();
    }

    @Override
    public void detailsClosed() {
        fabFilter.show();
    }
}
