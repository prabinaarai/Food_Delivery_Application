package com.foodapp.ChefFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.storage.OnProgressListener;


import com.bumptech.glide.Glide;
import com.foodapp.Chef;
import com.foodapp.ChefFoodPanel_BottomNavigation;
import com.foodapp.R;
import com.foodapp.UpdateDishModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Update_Delete_Dish extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 100;
    private static final int REQUEST_IMAGE_CROP = 101;
    private static final int REQUEST_STORAGE_PERMISSION = 200;

    TextInputLayout desc, qty, pri;
    TextView Dishname;
    ImageButton imageButton;
    Uri imageUri;
    String dburi;
    private Uri croppedImageUri;
    Button Update_dish, Delete_dish;
    String description, quantity, price, dishes, ChefId;
    String RandomUId;
    StorageReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth FAuth;
    String ID;
    private ProgressDialog progressDialog;
    DatabaseReference dataaa;
    String State, City, Sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__delete__dish);

        desc = findViewById(R.id.description);
        qty = findViewById(R.id.quantity);
        pri = findViewById(R.id.price);
        Dishname = findViewById(R.id.dish_name);
        imageButton = findViewById(R.id.imageupload);
        Update_dish = findViewById(R.id.Updatedish);
        Delete_dish = findViewById(R.id.Deletedish);
        ID = getIntent().getStringExtra("updatedeletedish");

        FAuth = FirebaseAuth.getInstance();
        String userid = FAuth.getCurrentUser() != null ? FAuth.getCurrentUser().getUid() : null;

        if (userid == null) {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
            // Redirect to login or handle accordingly
            finish();
            return;
        }

        dataaa = FirebaseDatabase.getInstance().getReference("Chef").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Chef chefc = dataSnapshot.getValue(Chef.class);
                if (chefc == null) {
                    Toast.makeText(Update_Delete_Dish.this, "Chef data not found.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                State = chefc.getState();
                City = chefc.getCity();
                Sub = chefc.getSuburban();

                setupListeners();
                fetchDishDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Update_Delete_Dish.this, "Failed to retrieve chef data.", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", databaseError.getMessage());
                finish();
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void setupListeners() {
        Update_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description = desc.getEditText().getText().toString().trim();
                quantity = qty.getEditText().getText().toString().trim();
                price = pri.getEditText().getText().toString().trim();

                if (isValid()) {
                    if (imageUri != null) {
                        uploadImage();
                    } else {
                        updatedesc(dburi);
                    }
                }
            }
        });

        Delete_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Update_Delete_Dish.this);
                builder.setMessage("Are you sure you want to delete this dish?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference dishRef = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails")
                                .child(State).child(City).child(Sub).child(FAuth.getCurrentUser().getUid()).child(ID);
                        dishRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    AlertDialog.Builder food = new AlertDialog.Builder(Update_Delete_Dish.this);
                                    food.setMessage("Your Dish has been Deleted");
                                    food.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            startActivity(new Intent(Update_Delete_Dish.this, ChefFoodPanel_BottomNavigation.class));
                                            finish();
                                        }
                                    });
                                    AlertDialog alertt = food.create();
                                    alertt.show();
                                } else {
                                    Toast.makeText(Update_Delete_Dish.this, "Failed to delete dish.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkStoragePermissionAndPickImage();
            }
        });
    }

    private void fetchDishDetails() {
        progressDialog = new ProgressDialog(Update_Delete_Dish.this);
        progressDialog.setMessage("Loading Dish Details...");
        progressDialog.show();

        DatabaseReference dishRef = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails")
                .child(State).child(City).child(Sub).child(FAuth.getCurrentUser().getUid()).child(ID);

        dishRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
                if (updateDishModel != null) {
                    desc.getEditText().setText(updateDishModel.getDescription());
                    qty.getEditText().setText(updateDishModel.getQuantity());
                    Dishname.setText("Dish name: " + updateDishModel.getDishes());
                    dishes = updateDishModel.getDishes();
                    pri.getEditText().setText(updateDishModel.getPrice());
                    Glide.with(Update_Delete_Dish.this).load(updateDishModel.getImageURL()).into(imageButton);
                    dburi = updateDishModel.getImageURL();
                } else {
                    Toast.makeText(Update_Delete_Dish.this, "Dish details not found.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(Update_Delete_Dish.this, "Failed to retrieve dish data.", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", databaseError.getMessage());
                finish();
            }
        });
    }

    private boolean isValid() {
        desc.setErrorEnabled(false);
        desc.setError("");
        qty.setErrorEnabled(false);
        qty.setError("");
        pri.setErrorEnabled(false);
        pri.setError("");

        boolean isValidDescription = false, isValidPrice = false, isValidQuantity = false;
        if (TextUtils.isEmpty(description)) {
            desc.setErrorEnabled(true);
            desc.setError("Description is Required");
        } else {
            desc.setError(null);
            isValidDescription = true;
        }
        if (TextUtils.isEmpty(quantity)) {
            qty.setErrorEnabled(true);
            qty.setError("Quantity is Required");
        } else {
            isValidQuantity = true;
        }
        if (TextUtils.isEmpty(price)) {
            pri.setErrorEnabled(true);
            pri.setError("Price is Required");
        } else {
            isValidPrice = true;
        }

        return isValidDescription && isValidQuantity && isValidPrice;
    }

    private void uploadImage() {
        if (imageUri != null) {
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            RandomUId = UUID.randomUUID().toString();
            ref = storageReference.child("DishImages/" + RandomUId);
            ChefId = FAuth.getCurrentUser().getUid();

            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            updatedesc(uri.toString());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Update_Delete_Dish.this, "Failed to retrieve image URL.", Toast.LENGTH_SHORT).show();
                                            Log.e("ImageUploadError", e.getMessage());
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Update_Delete_Dish.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("ImageUploadError", e.getMessage());
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            progressDialog.setCanceledOnTouchOutside(false);
                        }
                    });
        }
    }

    private void updatedesc(String uri) {
        ChefId = FAuth.getCurrentUser().getUid();
        FoodSupplyDetails info = new FoodSupplyDetails(dishes, quantity, price, description, uri, ID, ChefId);
        DatabaseReference dishRef = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails")
                .child(State).child(City).child(Sub).child(FAuth.getCurrentUser().getUid()).child(ID);

        dishRef.setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(Update_Delete_Dish.this, "Dish Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Update_Delete_Dish.this, "Failed to update dish.", Toast.LENGTH_SHORT).show();
                    Log.e("UpdateDishError", task.getException() != null ? task.getException().getMessage() : "Unknown error");
                }
            }
        });
    }

    private void checkStoragePermissionAndPickImage() {
        if (ContextCompat.checkSelfPermission(Update_Delete_Dish.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(Update_Delete_Dish.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            // Permission granted, proceed to pick image
            pickImageFromGallery();
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    private void cropImage(Uri sourceUri) {
        try {
            // Create a temporary file to store the cropped image
            File croppedImageFile = createImageFile();
            if (croppedImageFile == null) {
                Toast.makeText(this, "Failed to create image file for cropping.", Toast.LENGTH_SHORT).show();
                return;
            }

            croppedImageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", croppedImageFile);

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(sourceUri, "image/*");

            // Grant URI permissions
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            // Set crop properties
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 800);
            cropIntent.putExtra("outputY", 800);
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra("return-data", false);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
            cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            // Check if there is an app to handle the crop intent
            if (cropIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
            } else {
                Toast.makeText(this, "No app found to perform cropping.", Toast.LENGTH_SHORT).show();
                // Fallback: Use the original image without cropping
                imageUri = sourceUri;
                imageButton.setImageURI(imageUri);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error while cropping image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name with timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "CROPPED_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.e("ImageCropper", "Failed to create directory for cropped images.");
                return null;
            }
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, proceed to pick image
                pickImageFromGallery();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied to access storage.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle image pick result
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                cropImage(selectedImageUri);
            }
        }

        // Handle crop result
        if (requestCode == REQUEST_IMAGE_CROP && resultCode == Activity.RESULT_OK) {
            if (croppedImageUri != null) {
                imageUri = croppedImageUri;
                imageButton.setImageURI(imageUri);
                Toast.makeText(this, "Image cropped successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cropped image URI is null.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
