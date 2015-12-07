package com.squad.pug;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton bRegister;
    EditText etUsername, etName, etTNumber, etSex, etAge, etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etName = (EditText) findViewById(R.id.etName);
        etTNumber = (EditText) findViewById(R.id.etTNumber);
        etSex = (EditText) findViewById(R.id.etSex);
        etAge = (EditText) findViewById(R.id.etAge);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (ImageButton) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bRegister:

                String username = etUsername.getText().toString();
                String name = etName.getText().toString();
                String tel_number = etTNumber.getText().toString();

                String sex = etSex.getText().toString();
                int age = Integer.parseInt(etAge.getText().toString());
                String password = etPassword.getText().toString();

                User user = new User(username, name, tel_number, sex, age, password);

                registerUser(user);
                break;
        }

    }
    private void registerUser(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storedUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }
}
