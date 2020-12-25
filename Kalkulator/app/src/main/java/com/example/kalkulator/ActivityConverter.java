package com.example.kalkulator;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Date;


public class ActivityConverter extends AppCompatActivity {
    private Spinner from;
    private Spinner to;
    private TextView displayTo;
    private EditText displayFrom;
    private JSONObject currency;
    private String url = "https://api.ratesapi.io/api/latest";
    private DatePicker date;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        new MyTask().execute();
        setContentView(R.layout.activity_converter);

        from = findViewById(R.id.spinneriz);
        to = findViewById(R.id.spinnerv);
        date = findViewById(R.id.datePicker);
        ArrayAdapter<String> myAdapterFrom = new ArrayAdapter<String>(ActivityConverter.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.currencies));
        myAdapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from.setAdapter(myAdapterFrom);

        ArrayAdapter<String> myAdapterTo = new ArrayAdapter<String>(ActivityConverter.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.currenciesTo));
        myAdapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        to.setAdapter(myAdapterTo);

        from.setSelection(retriveFrom());
        to.setSelection(retriveTo());

        displayTo = findViewById(R.id.textViewv);
        displayTo.setClickable(false);
        displayTo.setEnabled(false);


        displayFrom = findViewById(R.id.textViewiz);
        date.setMaxDate(new Date().getTime());


        displayFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeValues();
            }
        });

        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeValues();
                saveFrom(from.getSelectedItemPosition());

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeValues();
                saveTo(to.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        date.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String dejt = date.getYear() + "-" + (date.getMonth()+1) + "-" + date.getDayOfMonth();
                String def = url.substring(0, 28);
                url = def + dejt;
                new MyTask().execute();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public void change(View view) {
        int first = to.getSelectedItemPosition();
        int second = from.getSelectedItemPosition();
        to.setSelection(second);
        from.setSelection(first);
    }


    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try (InputStream is = new URL(url).openStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }
                JSONObject json = new JSONObject(sb.toString());
                currency = new JSONObject(json.get("rates").toString());
                rd.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            changeValues();
        }
    }

    private float GetVal(String value) {
        float find = 1;
        try {
            find = Float.parseFloat(currency.get(value).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return find;
    }

    private void changeValues(){
        try{
            String From = from.getSelectedItem().toString();
            String To = to.getSelectedItem().toString();
            double convert = Float.parseFloat(displayFrom.getText().toString());
            if(!From.equals("EUR")){
                convert = convert / GetVal(From);
            }
            convert = convert * GetVal(To);
            DecimalFormat precision = new DecimalFormat("###,###,###.00");
            displayTo.setText(precision.format(convert));
            //displayTo.setText(String.valueOf(convert));
        }
        catch (Exception e){
            displayTo.setText(getString (R. string. pretvori));
        }
    }

    void saveFrom(int from){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("from", from);
        editor.apply();
    }

    void saveTo(int to){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("to", to);
        editor.apply();
    }


    int retriveFrom(){
        pref = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        return pref.getInt("from", 0);

    }
    int retriveTo(){
        pref = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        return pref.getInt("to", 0);

    }








}