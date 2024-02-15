package com.example.creditrecharge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

public class rechargeVue extends AppCompatActivity {
    String operateur;
    TextView _txtVLigne, _txtLogin, _txtConsultSolde;
    EditText _txtTelNum, _txtClickableCodeRecharge, _txtCodeRecharge;
    Button _btnValideRecharge, _btnConsulte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_vue);
        _txtVLigne = findViewById(R.id.txtVLigne);
        _txtTelNum = findViewById(R.id.txtTelNum);
        _txtLogin = findViewById(R.id.txtLogin);
        _txtConsultSolde =findViewById(R.id.txtConsultSolde);
        _txtClickableCodeRecharge =findViewById(R.id.txtClickableCodeRecharge);
        _txtCodeRecharge =findViewById(R.id.txtCodeRecharge);
        Bundle extras = getIntent().getExtras();
        _txtLogin.setText(extras.getString("userName"));
        _txtTelNum.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {
          }
          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
              int telNum = Integer.parseInt(s.toString());
              if(telNum<59999999 && telNum>50000000) {
                  operateur = "Orange";
                  _txtConsultSolde.setText("*111#");
                  _txtConsultSolde.setBackgroundColor(Color.parseColor("#ffbd33"));
                  _txtConsultSolde.setBackgroundColor(Color.parseColor("#ffbd33"));
                  _txtVLigne.setBackgroundColor(Color.parseColor("#ffbd33"));
                  _txtClickableCodeRecharge.setBackgroundColor(Color.parseColor("#ffbd33"));
                  _txtCodeRecharge.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
              }
              else if(telNum<29999999 && telNum>20000000) {
                  operateur = "Ooreedoo";
                  _txtConsultSolde.setText("*100#");
                  _txtConsultSolde.setBackgroundColor(Color.parseColor("#ff2d00"));
                  _txtConsultSolde.setBackgroundColor(Color.parseColor("#ff2d00"));
                  _txtVLigne.setBackgroundColor(Color.parseColor("#ff2d00"));
                  _txtClickableCodeRecharge.setBackgroundColor(Color.parseColor("#ff2d00"));
                  _txtCodeRecharge.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
              }
              else if(telNum<99999999 && telNum>90000000) {
                  operateur = "TunTel";
                  _txtConsultSolde.setText("*122#");
                  _txtConsultSolde.setBackgroundColor(Color.parseColor("#0053ff"));
                  _txtConsultSolde.setBackgroundColor(Color.parseColor("#0053ff"));
                  _txtVLigne.setBackgroundColor(Color.parseColor("#0053ff"));
                  _txtClickableCodeRecharge.setBackgroundColor(Color.parseColor("#0053ff"));
                  _txtCodeRecharge.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
              }
              else
                  operateur = "Operateur non valide";
              _txtVLigne.setText("Votre Ligne est: "+ operateur);
          }
          @Override
          public void afterTextChanged(Editable s) {
          }
        });
        _txtCodeRecharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int telNum = Integer.parseInt(_txtTelNum.getText().toString());
                if(telNum<59999999 && telNum>50000000)
                    _txtClickableCodeRecharge.setText("*100*"+_txtCodeRecharge.getText().toString()+"#");

                else if(telNum<29999999 && telNum>20000000)
                    _txtClickableCodeRecharge.setText("*101*"+_txtCodeRecharge.getText().toString()+"#");

                else if(telNum<99999999 && telNum>90000000)
                    _txtClickableCodeRecharge.setText("*123*"+_txtCodeRecharge.getText().toString()+"#");

                else
                    operateur = "Operateur non valide";
                _txtVLigne.setText("Votre Ligne est: "+ operateur);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        _btnConsulte = findViewById(R.id.btnConsulte);
        _btnConsulte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iTelephone = new Intent(Intent.ACTION_DIAL);
                iTelephone.setData(Uri.parse("tel:" + Uri.encode(_txtConsultSolde.getText().toString())));
                startActivity(iTelephone);
            }
        });
        _btnValideRecharge = findViewById(R.id.btnValideRecharge);
        _btnValideRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iTelephone = new Intent(Intent.ACTION_DIAL);
                iTelephone.setData(Uri.parse("tel:" + Uri.encode(_txtClickableCodeRecharge.getText().toString())));
                startActivity(iTelephone);
            }
        });

    }
}