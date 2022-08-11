package com.example.todo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Activity.MainActivity;
import com.example.todo.Activity.AddNewTask;
import com.example.todo.Model.To_Do_Model;
import com.example.todo.R;
import com.example.todo.util.DatabaseHandler;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.todoViewHolder> {

    private List<To_Do_Model> to_do_models;
    private MainActivity mainActivity;
    private DatabaseHandler databaseHandler;

    public TodoAdapter(DatabaseHandler databaseHandler ,MainActivity mainActivity) {
        this.databaseHandler = databaseHandler;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public TodoAdapter.todoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new todoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_container , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.todoViewHolder holder, int position) {
        databaseHandler.openDatabase();
        To_Do_Model item = to_do_models.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                databaseHandler.updateStatus(item.getId() , 1);
            }else {databaseHandler.updateStatus(item.getId() , 0);}
        });
    }
    private boolean toBoolean(int i){
        return i!=0;
    }


    @Override
    public int getItemCount() {
        return to_do_models.size();
    }

    public Context getContext(){
        return mainActivity;
    }

    public void setTasks(List<To_Do_Model> to_do_models){
        this.to_do_models = to_do_models;
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        To_Do_Model item = to_do_models.get(position);
        databaseHandler.deleteTask(item.getId());
        to_do_models.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        To_Do_Model item = to_do_models.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getId());
        bundle.putString("task" , item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(mainActivity.getSupportFragmentManager() , AddNewTask.TAG);
    }
    

    public static class todoViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        public todoViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.Checkbox);
        }
    }
}
