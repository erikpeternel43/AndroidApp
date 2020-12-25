package com.example.kalkulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.mariuszgromada.math.mxparser.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
    private EditText display;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttons);
        button = findViewById(R.id.converter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });


        display = findViewById(R.id.textView2);
        display.setShowSoftInputOnFocus(false);

    }

    public void openActivity2() {
        Intent intent = new Intent(this, ActivityConverter.class);
        startActivity(intent);


    }


    private void updateText(String addString){
        String stari = display.getText().toString();
        int cursor = display.getSelectionStart();
        String leftStr = stari.substring(0, cursor);
        String rightStr = stari.substring(cursor);
        display.setText(String.format("%s%s%s",leftStr,addString, rightStr));
        display.setSelection(cursor + 1);

    }


    public void zero(View view){
        updateText("0");
    }
    public void one(View view){
        updateText("1");
    }
    public void two(View view){
        updateText("2");
    }
    public void three(View view){
        updateText("3");
    }
    public void four(View view){
        updateText("4");
    }
    public void five(View view){
        updateText("5");
    }
    public void six(View view){
        updateText("6");
    }
    public void seven(View view){
        updateText("7");
    }
    public void eight(View view){
        updateText("8");
    }
    public void nine(View view){
        updateText("9");
    }
    public void plus(View view){
        updateText("+");
    }
    public void minus(View view){
        updateText("-");
    }
    public void factorial(View view){updateText("^");}
    public void procent(View view){
        updateText("%");
    }
    public void decimal(View view){
        updateText(".");
    }
    public void multiply(View view){
        updateText("x");
    }
    public void divide(View view){
        updateText("÷");
    }

    public void equals(View view){
        String userinput = display.getText().toString();

        userinput = userinput.replaceAll("÷", "/");
        userinput = userinput.replaceAll("x", "*");
        userinput = userinput.replaceAll("%", "#");

        Expression exp = new Expression(userinput);
        double mja = exp.calculate();
        String res = String.valueOf((float) mja);

        display.setText(res);
        display.setSelection(res.length());
    }

    public void clear(View view){
        display.setText("");
    }
    public void delete(View view){
        int cursor = display.getSelectionStart();
        int textlen = display.getText().length();
        if(cursor != 0 && textlen != 0){
            SpannableStringBuilder selection = (SpannableStringBuilder) display.getText();
            selection.replace(cursor-1, cursor, "");
            display.setText(selection);
            display.setSelection(cursor-1);
        }
    }





}