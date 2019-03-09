package com.tvsauto.mytvs.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tvsauto.mytvs.R;
import com.tvsauto.mytvs.holder.Employee;
import com.tvsauto.mytvs.ui.adapter.EmpListAdapter;
import com.tvsauto.mytvs.ui.fragment.EmployeeDetailsFragment;

import java.util.ArrayList;
import java.util.List;


public class EmployeeListActivity extends AppCompatActivity implements EmpListAdapter.AdapterCallback {

    private String strEmpList;
    private Employee empList;
    private RecyclerView rvEmployees;
    private EmpListAdapter empListAdapter;
    private Toolbar toolbar;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvEmployees = findViewById(R.id.rv_employees);

        strEmpList = getIntent().getStringExtra("empList");
        generateDataSet();
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
                Toast.makeText(EmployeeListActivity.this, query, Toast.LENGTH_LONG).show();
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
    }
}
