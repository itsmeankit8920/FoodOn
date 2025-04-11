package com.ankit.foodon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class UpdateDelete_Dish extends AppCompatActivity {

    TextInputLayout desc, qty, pri;
    TextView Dishname;
    ImageButton imageButton;
    Uri imageUri;
    String dbUri;
    Button updateDish, deleteDish;
    String description, quantity, price, dishes, chefId;
    String randomUID;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    String dishId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_dish);

        desc = findViewById(R.id.description);
        qty = findViewById(R.id.Quantity);
        pri = findViewById(R.id.price);
        Dishname = findViewById(R.id.dish_name);
        imageButton = findViewById(R.id.image_upload);
        updateDish = findViewById(R.id.Updatedish);
        deleteDish = findViewById(R.id.Deletedish);
        dishId = getIntent().getStringExtra("updatedeletedish");

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        final String userId = auth.getCurrentUser().getUid();
        databaseReference = firebaseDatabase.getReference("FoodDetails").child(userId).child(dishId);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Dish...");
        progressDialog.setCancelable(false);

        updateDish.setOnClickListener(v -> {
            description = desc.getEditText().getText().toString().trim();
            quantity = qty.getEditText().getText().toString().trim();
            price = pri.getEditText().getText().toString().trim();

            if (isValid()) {
                if (imageUri != null) {
                    uploadImage();
                } else {
                    updateDishDetails(dbUri); // Update without image if none selected
                }
            }
        });

        deleteDish.setOnClickListener(v -> {
            new AlertDialog.Builder(UpdateDelete_Dish.this)
                    .setMessage("Are you sure you want to Delete this Dish?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteDish();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.cancel())
                    .show();
        });

        // Handle image upload click
        imageButton.setOnClickListener(v -> openImagePicker());
    }

    private void deleteDish() {
        firebaseDatabase.getReference("FoodDetails").child(auth.getCurrentUser().getUid()).child(dishId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UpdateDelete_Dish.this, "Dish Deleted Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateDelete_Dish.this, ChefFoodPanel_BottomNavigation.class));
                    } else {
                        Toast.makeText(UpdateDelete_Dish.this, "Failed to delete the dish.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateDishDetails(String imageUri) {
        chefId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FoodDetails foodDetails = new FoodDetails(dishes, quantity, price, description, imageUri, dishId, chefId);
        firebaseDatabase.getReference("FoodDetails").child(chefId).child(dishId).setValue(foodDetails)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateDelete_Dish.this, "Dish Updated Successfully!", Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadImage() {
        if (imageUri != null) {
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            randomUID = UUID.randomUUID().toString();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(randomUID);
            storageReference.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        updateDishDetails(uri.toString());
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateDelete_Dish.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean isValid() {
        desc.setErrorEnabled(false);
        qty.setErrorEnabled(false);
        pri.setErrorEnabled(false);

        boolean isValidDescription = !TextUtils.isEmpty(description);
        boolean isValidQuantity = !TextUtils.isEmpty(quantity);
        boolean isValidPrice = !TextUtils.isEmpty(price);

        if (!isValidDescription) {
            desc.setError("Description is required");
        }
        if (!isValidQuantity) {
            qty.setError("Enter the quantity");
        }
        if (!isValidPrice) {
            pri.setError("Price is required");
        }

        return isValidDescription && isValidQuantity && isValidPrice;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            imageButton.setImageURI(imageUri);  // Display selected image
        }
    }
}
