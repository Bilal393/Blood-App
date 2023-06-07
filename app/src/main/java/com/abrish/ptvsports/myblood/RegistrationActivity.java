package com.abrish.ptvsports.myblood;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {
    private EditText nameEditText, cityEditText, emailEditText, passwordEditText, numberEditText;
    private RadioGroup userTypeRadioGroup;
    private Spinner bloodGroupSpinner;
    private Button registerButton, loginButton;

    private DatabaseReference donorsRef, usersRef;

    private String selectedBloodGroup;
    int minLength = 5;
    int maxLength = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firebase database references
        donorsRef = FirebaseDatabase.getInstance().getReference("donors");
        usersRef = FirebaseDatabase.getInstance().getReference("users");


        // Initialize views
        nameEditText = findViewById(R.id.edit_text_name);
        cityEditText = findViewById(R.id.edit_text_city);
        numberEditText = findViewById(R.id.edit_text_number);
        emailEditText = findViewById(R.id.edit_text_email);
        passwordEditText = findViewById(R.id.edit_text_password);
        userTypeRadioGroup = findViewById(R.id.radio_group_user_type);
        bloodGroupSpinner = findViewById(R.id.spinner_blood_group);
        registerButton = findViewById(R.id.button_register);
        loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

        // Set up blood group spinner
        ArrayAdapter<CharSequence> bloodGroupAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.blood_groups_array,
                android.R.layout.simple_spinner_item
        );
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(bloodGroupAdapter);

        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBloodGroup = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String number = numberEditText.getText().toString().trim();

        int selectedUserTypeId = userTypeRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedUserTypeRadioButton = findViewById(selectedUserTypeId);
        String userType = selectedUserTypeRadioButton.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(city) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(selectedBloodGroup)) {
            Toast.makeText(this, "Please select a blood group", Toast.LENGTH_SHORT).show();
            return;
        }
        if (number.startsWith("03")) {
            if (number.length() != 11) {
                // String length is not within the specified range, show toast
                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (number.startsWith("+923")) {
            if (number.length() != 13) {
                // String length is not within the specified range, show toast
                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (number.startsWith("+9203")) {
            if (number.length() != 14) {
                // String length is not within the specified range, show toast
                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Check if email already exists in the database
        Query emailQuery = usersRef.orderByChild("email").equalTo(email);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isEmailExist = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User existingUser = snapshot.getValue(User.class);
                    if (existingUser != null && existingUser.getEmail().equals(email)) {
                        isEmailExist = true;
                        break;
                    }
                }

                if (isEmailExist) {
                    Toast.makeText(RegistrationActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if number already exists in the database
                    Query numberQuery = usersRef.orderByChild("number").equalTo(number);
                    numberQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean isNumberExist = false;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User existingUser = snapshot.getValue(User.class);
                                if (existingUser != null && existingUser.getNumber().equals(number)) {
                                    isNumberExist = true;
                                    break;
                                }
                            }

                            if (isNumberExist) {
                                Toast.makeText(RegistrationActivity.this, "Number already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                // Generate a unique user ID for each registration
                                String userId = donorsRef.push().getKey();

                                User user = new User(userId, name, selectedBloodGroup, city, number, email, password, userType);

                                // Save the user to Firebase Realtime Database
                                if (userType.equalsIgnoreCase("Donor")) {
                                    donorsRef.child(userId).setValue(user);
                                    usersRef.child(userId).setValue(user);
                                } else {
                                    usersRef.child(userId).setValue(user);
                                }

                                Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                finish(); // Finish the activity and return to the previous screen
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(RegistrationActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegistrationActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
