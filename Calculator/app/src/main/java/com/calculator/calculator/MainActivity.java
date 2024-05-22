package com.calculator.calculator;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnSin, btnCos, btnSquarre, btn7, btn4, btn1, btn0,
            btnInverse, btn8, btn5, btn2, btn00, btnTan, btnPi,
            btn9, btn6, btn3, btnDot, btnAC, btnRootSquare, btnSum,
            btnMinus, btnMultiply, btnDevide, btnDelete, btnPower,
            btnOpenPar, btnClosePar, btnEqual;
    TextView txtScreen, txtHistory;
    ArrayList<StringBuilder> listOp;
    StringBuilder strOp, strHist;
    double dbleRes;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       EdgeToEdge.enable(this);
       setContentView(R.layout.activity_main);
       ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db= openOrCreateDatabase("mobileDevDB",0,null);
        db.execSQL("create table if not exists calculator (id Integer primary key AUTOINCREMENT, operation varchar, resultat varchar)");
        SQLiteStatement s = db.compileStatement("select count(*) from calculator;");
        long count = s.simpleQueryForLong();
        if (count ==0){
            db.execSQL("insert into calculator values (1,'Operation','Resultat')");
        }
        btnSin = findViewById(R.id.btnSin);
        btnCos = findViewById(R.id.btnCos);
        btnSquarre = findViewById(R.id.btnSquarre);
        btn7 = findViewById(R.id.btn7);
        btn4 = findViewById(R.id.btn4);
        btn1 = findViewById(R.id.btn1);
        btn0 = findViewById(R.id.btn0);
        btnInverse = findViewById(R.id.btnInverse);
        btn8 = findViewById(R.id.btn8);
        btn5 = findViewById(R.id.btn5);
        btn2 = findViewById(R.id.btn2);
        btn00 = findViewById(R.id.btn00);
        btnTan = findViewById(R.id.btnTan);
        btnPi = findViewById(R.id.btnPi);
        btn9 = findViewById(R.id.btn9);
        btn6 = findViewById(R.id.btn6);
        btn3 = findViewById(R.id.btn3);
        btnDot = findViewById(R.id.btnDot);
        btnAC = findViewById(R.id.btnAC);
        btnRootSquare = findViewById(R.id.btnRootSquare);
        btnSum = findViewById(R.id.btnSum);
        btnMinus = findViewById(R.id.btnMinus);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDevide = findViewById(R.id.btnDevide);
        btnDelete = findViewById(R.id.btnDelete);
        btnPower = findViewById(R.id.btnPower);
        btnOpenPar = findViewById(R.id.btnOpenPar);
        btnClosePar = findViewById(R.id.btnClosePar);
        btnEqual = findViewById(R.id.btnEqual);

        txtHistory = findViewById(R.id.txtHistory);
        txtScreen =findViewById(R.id.txtScreen);

        strHist = new StringBuilder();
        strOp = new StringBuilder();
        listOp = new ArrayList<>();
        loadHistPannel();

        LinearLayout[] cols = new LinearLayout[]{
                findViewById(R.id.col1),
                findViewById(R.id.col2),
                findViewById(R.id.col3),
                findViewById(R.id.col4)
        };

        for (LinearLayout col : cols) {
            for (int i = 0; i < col.getChildCount(); i++) {
                View child = col.getChildAt(i);
                if (child instanceof Button) {
                    Button button = (Button) child;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String buttonText = button.getText().toString(); // Récupérer le texte du bouton
                            StringBuilder btnTxtBldr = new StringBuilder(buttonText);
                            listOp.add(new StringBuilder(btnTxtBldr));
                            updateScreenText();
                        }
                    });
                }
            }
        }

        btnPi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOp.add(new StringBuilder("3.141592653589"));
                updateScreenText();
            }
        });
        btnPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOp.add(new StringBuilder("^"));
                updateScreenText();

            }
        });
        btnInverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOp.add(new StringBuilder("^(-1)"));
                updateScreenText();

            }
        });
        btnRootSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOp.add(new StringBuilder("sqrt"));
                updateScreenText();

            }
        });
        btnSquarre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOp.add(new StringBuilder("^2"));
                updateScreenText();

            }
        });
        btnAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOp.clear();
                updateScreenText();

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listOp.isEmpty()) {
                    listOp.remove(listOp.size() - 1);
                }
                updateScreenText();

            }
        });
        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    strOp = new StringBuilder();
                    for(StringBuilder e: listOp)
                        strOp.append(e);

                    String opr = strOp.toString();
                    dbleRes = eval(opr);
                    String rslt = dbleRes + "";
                    db.execSQL("insert into calculator (operation, resultat) values (?,?)",new Object[]{opr, rslt});
                    listOp.clear();
                    txtScreen.setText(strOp + " = " + dbleRes);
                    loadHistPannel();

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void loadHistPannel() {
        Cursor a = db.rawQuery("select * from calculator ORDER BY id DESC LIMIT 5", null);
        if (a != null && a.moveToFirst()) {
            int indexOperation = a.getColumnIndex("operation");
            int indexResultat = a.getColumnIndex("resultat");
            if (indexOperation >= 0 && indexResultat >= 0) {
                //strHist = new StringBuilder("\t  ------------------------");
                strHist = new StringBuilder();
                do {
                    String operation = a.getString(indexOperation);
                    String resultat = a.getString(indexResultat);
                    //strHist.append(new StringBuilder(operation + " = " + resultat + "\n" ));
                    strHist.insert(0, new StringBuilder(operation + " = " + resultat + "\n\t  -----\t-----\t-----\n" ));

                } while (a.moveToNext());
            }
        }
        a.close();
        txtHistory.setText(strHist);
    }
    private void updateScreenText() {
        strOp = new StringBuilder();
        if(listOp.size() != 0)
        for(StringBuilder e: listOp)
            strOp.append(e);
        txtScreen.setText(strOp.toString());
    }
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;
            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }
            // Grammar:
// expression = term | expression `+` term | expression `-` term
// term = factor | term `*` factor | term `/` factor
// factor = `+` factor | `-` factor | `(` expression `)` | number
// | functionName `(` expression `)` | functionName factor
// | factor `^` factor
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }
            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus
                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }
                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation
                return x;
            }
        }.parse();
    }


}