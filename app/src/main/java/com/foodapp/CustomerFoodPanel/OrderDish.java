package com.foodapp.CustomerFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foodapp.Chef;
import com.foodapp.ChefFoodPanel.UpdateDishModel;
import com.foodapp.Customer;
import com.foodapp.CustomerFoodPanel_BottomNavigation;
import com.foodapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderDish extends AppCompatActivity {

    String RandomId, ChefID;
    ImageView imageView;
    TextView Foodname, ChefName, ChefLoaction, FoodQuantity, FoodPrice, FoodDescription;
    EditText quantityInput;
    Button incrementBtn, decrementBtn;
    DatabaseReference databaseReference, chefdata, reference;
    String State, City, Sub, dishname;
    int dishprice;
    String custID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dish);

        Foodname = findViewById(R.id.food_name);
        ChefName = findViewById(R.id.chef_name);
        ChefLoaction = findViewById(R.id.chef_location);
        FoodQuantity = findViewById(R.id.food_quantity);
        FoodPrice = findViewById(R.id.food_price);
        FoodDescription = findViewById(R.id.food_description);
        imageView = findViewById(R.id.image);
        quantityInput = findViewById(R.id.quantityInput);
        incrementBtn = findViewById(R.id.incrementBtn);
        decrementBtn = findViewById(R.id.decrementBtn);

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dataaa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer cust = dataSnapshot.getValue(Customer.class);
                State = cust.getState();
                City = cust.getCity();
                Sub = cust.getSuburban();

                RandomId = getIntent().getStringExtra("FoodMenu");
                ChefID = getIntent().getStringExtra("ChefId");

                databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(State).child(City).child(Sub).child(ChefID).child(RandomId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
                        Foodname.setText(updateDishModel.getDishes());
                        FoodQuantity.setText(Html.fromHtml("<b>" + "Quantity: " + "</b>" + updateDishModel.getQuantity()));
                        FoodDescription.setText(Html.fromHtml("<b>" + "Description: " + "</b>" + updateDishModel.getDescription()));
                        FoodPrice.setText(Html.fromHtml("<b>" + "Price: ₹ " + "</b>" + updateDishModel.getPrice()));
                        Glide.with(OrderDish.this).load(updateDishModel.getImageURL()).into(imageView);

                        chefdata = FirebaseDatabase.getInstance().getReference("Chef").child(ChefID);
                        chefdata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Chef chef = dataSnapshot.getValue(Chef.class);
                                ChefName.setText(Html.fromHtml("<b>" + "Chef Name: " + "</b>" + chef.getFname() + " " + chef.getLname()));
                                ChefLoaction.setText(Html.fromHtml("<b>" + "Location: " + "</b>" + chef.getSuburban()));
                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            quantityInput.setText(dataSnapshot.child("DishQuantity").getValue(String.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                incrementBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity = Integer.parseInt(quantityInput.getText().toString());
                        quantity++;
                        quantityInput.setText(String.valueOf(quantity));
                    }
                });

                decrementBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity = Integer.parseInt(quantityInput.getText().toString());
                        if (quantity > 0) {
                            quantity--;
                            quantityInput.setText(String.valueOf(quantity));
                        }
                    }
                });

                quantityInput.setOnFocusChangeListener((v, hasFocus) -> {
                    if (!hasFocus) {
                        String inputText = quantityInput.getText().toString();
                        if (inputText.isEmpty() || Integer.parseInt(inputText) < 0) {
                            quantityInput.setText("0");
                        }
                    }
                });

                findViewById(R.id.addToCartButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int num = Integer.parseInt(quantityInput.getText().toString());
                        if (num > 0) {
                            addToCart(num);
                        } else {
                            Toast.makeText(OrderDish.this, "Please select a quantity greater than 0", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void addToCart(int num) {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(State).child(City).child(Sub).child(ChefID).child(RandomId);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UpdateDishModel update = dataSnapshot.getValue(UpdateDishModel.class);
                dishname = update.getDishes();
                dishprice = Integer.parseInt(update.getPrice());
                int totalprice = num * dishprice;

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("DishName", dishname);
                hashMap.put("DishID", RandomId);
                hashMap.put("DishQuantity", String.valueOf(num));
                hashMap.put("Price", String.valueOf(dishprice));
                hashMap.put("Totalprice", String.valueOf(totalprice));
                hashMap.put("ChefId", ChefID);
                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(OrderDish.this, "Added to cart", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
