package com.foodapp.CustomerFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class CustomerCartAdapter extends RecyclerView.Adapter<CustomerCartAdapter.ViewHolder> {

    private Context mcontext;
    private List<Cart> cartModellist;
    private int total = 0;  // Changed to instance variable to maintain state across items

    public CustomerCartAdapter(Context context, List<Cart> cartModellist) {
        this.cartModellist = cartModellist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.cart_placeorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Cart cart = cartModellist.get(position);
        holder.dishname.setText(cart.getDishName());
        holder.PriceRs.setText("Price: ₹ " + cart.getPrice());
        holder.Qty.setText("× " + cart.getDishQuantity());
        holder.Totalrs.setText("Total: ₹ " + cart.getTotalprice());

        // Update total price
        total += Integer.parseInt(cart.getTotalprice());
        CustomerCartFragment.grandt.setText("Grand Total: ₹ " + total);

        // Set the initial quantity
        holder.quantityInput.setText(String.valueOf(cart.getDishQuantity()));

        // Set OnClickListener for increment button
        holder.incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantityInput.getText().toString());
                quantity++;
                holder.quantityInput.setText(String.valueOf(quantity));
                updateCart(cart, quantity);
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
                    updateCart(cart, quantity);
                }
            }
        });
    }

    private void updateCart(Cart cart, int quantity) {
        final int dishprice = Integer.parseInt(cart.getPrice());
        int totalprice = quantity * dishprice;

        // Update Firebase database
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("DishID", cart.getDishID());
        hashMap.put("DishName", cart.getDishName());
        hashMap.put("DishQuantity", String.valueOf(quantity));
        hashMap.put("Price", String.valueOf(dishprice));
        hashMap.put("Totalprice", String.valueOf(totalprice));
        hashMap.put("ChefId", cart.getChefId());

        // Save or remove from Firebase depending on quantity
        if (quantity > 0) {
            FirebaseDatabase.getInstance().getReference("Cart")
                    .child("CartItems")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(cart.getDishID())
                    .setValue(hashMap);
        } else {
            FirebaseDatabase.getInstance().getReference("Cart")
                    .child("CartItems")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(cart.getDishID())
                    .removeValue();
        }
    }

    @Override
    public int getItemCount() {
        return cartModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dishname, PriceRs, Qty, Totalrs;
        EditText quantityInput;
        Button incrementBtn, decrementBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishname = itemView.findViewById(R.id.Dishname);
            PriceRs = itemView.findViewById(R.id.pricers);
            Qty = itemView.findViewById(R.id.qty);
            Totalrs = itemView.findViewById(R.id.totalrs);
            quantityInput = itemView.findViewById(R.id.quantityInput);
            incrementBtn = itemView.findViewById(R.id.incrementBtn);
            decrementBtn = itemView.findViewById(R.id.decrementBtn);
        }
    }
}
