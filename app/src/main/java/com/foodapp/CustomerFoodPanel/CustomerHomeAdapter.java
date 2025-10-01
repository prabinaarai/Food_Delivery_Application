package com.foodapp.CustomerFoodPanel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodapp.ChefFoodPanel.UpdateDishModel;
import com.foodapp.R;

import java.util.List;

public class CustomerHomeAdapter extends RecyclerView.Adapter<CustomerHomeAdapter.ViewHolder> {

    private Context mcontext;
    private List<UpdateDishModel> updateDishModellist;

    public CustomerHomeAdapter(Context context, List<UpdateDishModel> updateDishModellist) {
        this.updateDishModellist = updateDishModellist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customer_menudish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final UpdateDishModel updateDishModel = updateDishModellist.get(position);
        Glide.with(mcontext).load(updateDishModel.getImageURL()).into(holder.imageView);
        holder.Dishname.setText(updateDishModel.getDishes());
        holder.price.setText("Price: ₹ " + updateDishModel.getPrice());

        // Set initial quantity to 0
        holder.quantityInput.setText("0");

        // Set OnClickListener for increment button
        holder.incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantityInput.getText().toString());
                quantity++;
                holder.quantityInput.setText(String.valueOf(quantity));
            }
        });

        // Set OnClickListener for decrement button
        holder.decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantityInput.getText().toString());
                if (quantity > 0) {
                    quantity--;
                    holder.quantityInput.setText(String.valueOf(quantity));
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, OrderDish.class);
                intent.putExtra("FoodMenu", updateDishModel.getRandomUID());
                intent.putExtra("ChefId", updateDishModel.getChefId());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return updateDishModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView Dishname, price;
        EditText quantityInput; // Change from ElegantNumberButton to EditText
        Button incrementBtn, decrementBtn; // Buttons for incrementing and decrementing

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.menu_image);
            Dishname = itemView.findViewById(R.id.dishname);
            price = itemView.findViewById(R.id.dishprice);
            quantityInput = itemView.findViewById(R.id.quantityInput);
            incrementBtn = itemView.findViewById(R.id.incrementBtn);
            decrementBtn = itemView.findViewById(R.id.decrementBtn);
        }
    }
}
