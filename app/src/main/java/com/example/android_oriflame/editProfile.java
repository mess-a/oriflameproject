package com.example.android_oriflame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class editProfile extends AppCompatActivity implements View.OnClickListener {

    FirebaseFirestore db;
    ImageView bBack;
    EditText name, dob, phonenumber, location, email;
    Button saveProf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        bBack = findViewById(R.id.backButton);
        saveProf = findViewById(R.id.saveProfileButton);

        bBack.setOnClickListener(this);
        saveProf.setOnClickListener(this);

        name = findViewById(R.id.nameedit);
        dob = findViewById(R.id.dobedit);
        email = findViewById(R.id.emailedit);
        location = findViewById(R.id.locationedit);
        phonenumber = findViewById(R.id.numberedit);

        db = FirebaseFirestore.getInstance();

        retrieveEditData();
    }

    @Override
    public void onClick(View v) {
        if (v == bBack) {
            finish();
        } else {

        }
    }

    private void retrieveEditData() {
        db.collection("user_data")
                .whereEqualTo("id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String name = task.getResult().getDocuments().get(0).get("user_name").toString();
                        } else {
                            Toast.makeText(editProfile.this, "Error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}