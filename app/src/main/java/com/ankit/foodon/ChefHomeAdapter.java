package com.ankit.foodon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChefHomeAdapter extends RecyclerView.Adapter<ChefHomeAdapter.ViewHolder> {

    private Context mcont;
    private List<UpdateDishModel> updateDishModelList;

    public ChefHomeAdapter(Context context, List<UpdateDishModel> updateDishModelList) {
        this.updateDishModelList = updateDishModelList;
        this.mcont = context;
    }

    @NonNull
    @Override
    public ChefHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcont).inflate(R.layout.chefmenu_update_delete, parent, false);
        return new ChefHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UpdateDishModel updateDishModel = updateDishModelList.get(position);
        holder.dishes.setText(updateDishModel.getDishes());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mcont, UpdateDelete_Dish.class);
            intent.putExtra("updatedeletedish", updateDishModel.getRandomUID());
            mcont.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return updateDishModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishes = itemView.findViewById(R.id.dish_name);
        }
    }
}
