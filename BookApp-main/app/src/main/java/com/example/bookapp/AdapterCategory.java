package com.example.bookapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public  class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.HolderCategorry> implements Filterable {

    private Context context;
    public ArrayList<ModelCategory> categoryArrayList, filterList;

    private RowCategoryBinding binding;


    private Filtercategory filter;


    public AdapterCategory(Context context, ArrayList<ModelCategory> categoryArrayList) {
        this.context =context;
        this.categoryArrayList =categoryArrayList;
        this.filterList = categoryArrayList;
    }


    @NonNull
    @Override
    public HolderCategorry onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderCategorry(binding.getRoot());
    }

    @Override
    public  void onBindViewHolder(@NonNull AdapterCategory.HolderCategorry holder, int position){
        ModelCategory model = categoryArrayList.get(position);
        String id = model.getId();
        String category = model.getCategory();
        String uid = model.getUid();
        long timestamp = model.getTimestamp();

        //set data
        holder.categoryTv.setText(category);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Ar you sure you want to delete this category?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Deleting....", Toast.LENGTH_SHORT).show();
                                    deleteCategory(model,holder);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });
    }

    private void deleteCategory(ModelCategory model, HolderCategorry holder) {
        String id = model.getId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(id)
                .removeVale()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused){
                            Toast.makeText(context, "Successfully deleted....", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public  void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new Filtercategory(filterList, this);
        }
        return filter;
    }

    class HolderCategorry extends RecyclerView.ViewHolder{

        TextView categoryTv;
        ImageButton deleteBtn;
        public HolderCategorry(@NonNull View itemView){
            super(itemView);

            categoryTv = binding.categoryTv;
            deleteBtn = binding.deleteBtn;
        }
    }
}