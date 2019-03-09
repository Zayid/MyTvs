package com.tvsauto.mytvs.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tvsauto.mytvs.R;
import com.tvsauto.mytvs.holder.Employee;


public class EmpListAdapter extends RecyclerView.Adapter<EmpListAdapter.ViewHolder> {

    private Employee empList;
    private AdapterCallback adapterCallback;

    public EmpListAdapter(Employee empList, AdapterCallback adapterCallback) {
        this.empList = empList;
        this.adapterCallback = adapterCallback;
    }

    public interface AdapterCallback {
        void inflateDetailFragment(String name, String location, String designation,
                                   String salary, String joiningDate);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item_employees, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvEmpName.setText(empList.getData().get(i).get(0));
        viewHolder.tvEmpDesignation.setText(empList.getData().get(i).get(1));
        viewHolder.tvEmpLocation.setText(empList.getData().get(i).get(2));
    }

    @Override
    public int getItemCount() {
        return empList.getData().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmpName, tvEmpLocation, tvEmpDesignation;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEmpName = itemView.findViewById(R.id.tv_emp_name);
            tvEmpLocation = itemView.findViewById(R.id.tv_emp_location);
            tvEmpDesignation = itemView.findViewById(R.id.tv_emp_designation);

            itemView.setOnClickListener(view -> adapterCallback.inflateDetailFragment(
                    empList.getData().get(getAdapterPosition()).get(0),
                    empList.getData().get(getAdapterPosition()).get(2),
                    empList.getData().get(getAdapterPosition()).get(1),
                    empList.getData().get(getAdapterPosition()).get(5),
                    empList.getData().get(getAdapterPosition()).get(4)
            ));
        }
    }

    public void updateList(Employee filteredEmpList) {
        empList = filteredEmpList;
        notifyDataSetChanged();
    }
}
