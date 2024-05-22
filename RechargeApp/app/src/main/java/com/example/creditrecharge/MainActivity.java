package com.example.creditrecharge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button _btnLogin;
    EditText _txtPseudoName;
    EditText _txtPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _btnLogin = findViewById(R.id.btnLogin);
        _txtPseudoName = findViewById(R.id.txtPseudoName);
        _txtPwd = findViewById(R.id.txtPwd);
        _btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = _txtPwd.getText().toString();
                String pseudoName = _txtPseudoName.getText().toString();
                if((pwd.equals("123456")) && (pseudoName.equals("Najeh"))){
                    Intent I = new Intent(getApplicationContext(), rechargeVue.class);
                    I.putExtra("userName", pseudoName);
                    startActivity(I);
                }
                else{
                    Toast.makeText(getApplicationContext(), "non authentique login mdp", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}