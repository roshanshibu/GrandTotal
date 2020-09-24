package com.roshanshibu.grandtotal;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences countPreferences;
    private SharedPreferences.Editor countPrefsEditor;
    private Boolean savecount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the status bar icon colors to dark as samsung is not setting to the dark by default
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // edited here
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.background_blue_light));
        }


        //get the user input EditTexts
        final EditText no2000 = (EditText) findViewById(R.id.calc_input_2000);
        final EditText no500 = (EditText) findViewById(R.id.calc_input_500);
        final EditText no200 = (EditText) findViewById(R.id.calc_input_200);
        final EditText no100 = (EditText) findViewById(R.id.calc_input_100);
        final EditText no50 = (EditText) findViewById(R.id.calc_input_50);
        final EditText no20 = (EditText) findViewById(R.id.calc_input_20);
        final EditText no10 = (EditText) findViewById(R.id.calc_input_10);
        final EditText no5 = (EditText) findViewById(R.id.calc_input_5);


        countPreferences = getSharedPreferences("GrandTotalPrefs",MODE_PRIVATE);
        countPrefsEditor = countPreferences.edit();
        savecount = countPreferences.getBoolean("savecount",true);
        countPrefsEditor.apply();

        if(savecount == true){
            no2000.setText(countPreferences.getString("no2000",""));
            no500.setText(countPreferences.getString("no500",""));
            no200.setText(countPreferences.getString("no200",""));
            no100.setText(countPreferences.getString("no100",""));
            no50.setText(countPreferences.getString("no50",""));
            no20.setText(countPreferences.getString("no20",""));
            no10.setText(countPreferences.getString("no10",""));
            no5.setText(countPreferences.getString("no5",""));
            settotal();
        }

        no2000.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                settotal();}
            @Override public void afterTextChanged(Editable editable) {}});

        no500.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                settotal();}
            @Override public void afterTextChanged(Editable editable) {}});

        no200.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                settotal();}
            @Override public void afterTextChanged(Editable editable) {}});

        no100.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                settotal();}
            @Override public void afterTextChanged(Editable editable) {}});

        no50.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                settotal();}
            @Override public void afterTextChanged(Editable editable) {}});

        no20.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                settotal();}
            @Override public void afterTextChanged(Editable editable) {}});

        no10.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                settotal();}
            @Override public void afterTextChanged(Editable editable) {}});

        no5.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                settotal();}
            @Override public void afterTextChanged(Editable editable) {}});

        //get the buttons
        final ImageButton resetbut = (ImageButton) findViewById(R.id.reload_button);
        final ImageButton screencap = (ImageButton) findViewById(R.id.save_button);

        //on reset
        resetbut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                no2000.setText("");
                no500.setText("");
                no200.setText("");
                no100.setText("");
                no50.setText("");
                no20.setText("");
                no10.setText("");
                no5.setText("");
                settotal();
            }
        });

        //on save
        final TableLayout bottom_buttons_layout = (TableLayout) findViewById(R.id.bottom_buttons_layout);
        final TextView screendate = (TextView) findViewById(R.id.date_on_screencap);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy");
        final String formattedDate = df.format(c);
        screendate.setText(formattedDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String filename = sdf.format(c);
        System.out.println("Current time to seconds => " + filename);

        screencap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //do this only if we have the permission to write to storage
                //if we dont have it, as the user
                if (isWriteStoragePermissionGranted()) {
                    screendate.setVisibility(View.VISIBLE);
                    bottom_buttons_layout.setVisibility(View.GONE);
                    View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                    store(getScreenShot(rootView), filename + ".png");
                    Toast.makeText(MainActivity.this, "Screenshot saved!", Toast.LENGTH_SHORT).show();
                    screendate.setVisibility(View.INVISIBLE);
                    bottom_buttons_layout.setVisibility(View.VISIBLE);
                }
                //if permission is not available, then display a toast and ask
                else {
                    Toast.makeText(MainActivity.this, "Need storage permission to save a screenshot", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }
            }
        });
    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }


    public static void store(Bitmap bm, String fileName){
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Total";
        File dir = new File(dirPath);

        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Permission is granted
                return true;
            } else {
                //Permission is revoked
                return false;
            }
        }
        else {
            //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public void settotal(){

        ////////////////////////////2000///////////////////////////
        EditText no2000 = (EditText) findViewById(R.id.calc_input_2000);
        TextView gt2000 = (TextView) findViewById(R.id.calac_total_2000);

        int num2000 = 0;
        if(!no2000.getText().toString().isEmpty())
            num2000 = Integer.parseInt(no2000.getText().toString());
        else
            num2000 = 0;
        int total2000 = num2000*2000;
        gt2000.setText(total2000+"");

        ////////////////////////////500///////////////////////////
        EditText no500 = (EditText) findViewById(R.id.calc_input_500);
        TextView gt500 = (TextView) findViewById(R.id.calac_total_500);

        int num500 = 0;
        if(!no500.getText().toString().isEmpty())
            num500 = Integer.parseInt(no500.getText().toString());
        else
            num500 = 0;
        int total500 = num500*500;
        gt500.setText(total500+"");

        ////////////////////////////200///////////////////////////
        EditText no200 = (EditText) findViewById(R.id.calc_input_200);
        TextView gt200 = (TextView) findViewById(R.id.calac_total_200);

        int num200 = 0;
        if(!no200.getText().toString().isEmpty())
            num200 = Integer.parseInt(no200.getText().toString());
        else
            num200 = 0;
        int total200 = num200*200;
        gt200.setText(total200+"");

        ////////////////////////////100///////////////////////////
        EditText no100 = (EditText) findViewById(R.id.calc_input_100);
        TextView gt100 = (TextView) findViewById(R.id.calac_total_100);

        int num100 = 0;
        if(!no100.getText().toString().isEmpty())
            num100 = Integer.parseInt(no100.getText().toString());
        else
            num100 = 0;
        int total100 = num100*100;
        gt100.setText(total100+"");

        ////////////////////////////50///////////////////////////
        EditText no50 = (EditText) findViewById(R.id.calc_input_50);
        TextView gt50 = (TextView) findViewById(R.id.calac_total_50);

        int num50 = 0;
        if(!no50.getText().toString().isEmpty())
            num50 = Integer.parseInt(no50.getText().toString());
        else
            num50 = 0;
        int total50 = num50*50;
        gt50.setText(total50+"");


        ////////////////////////////20///////////////////////////
        EditText no20 = (EditText) findViewById(R.id.calc_input_20);
        TextView gt20 = (TextView) findViewById(R.id.calac_total_20);

        int num20 = 0;
        if(!no20.getText().toString().isEmpty())
            num20 = Integer.parseInt(no20.getText().toString());
        else
            num20 = 0;
        int total20 = num20*20;
        gt20.setText(total20+"");

        ////////////////////////////10///////////////////////////
        EditText no10 = (EditText) findViewById(R.id.calc_input_10);
        TextView gt10 = (TextView) findViewById(R.id.calac_total_10);

        int num10 = 0;
        if(!no10.getText().toString().isEmpty())
            num10 = Integer.parseInt(no10.getText().toString());
        else
            num10 = 0;
        int total10 = num10*10;
        gt10.setText(total10+"");

        ////////////////////////////5///////////////////////////
        EditText no5 = (EditText) findViewById(R.id.calc_input_5);
        TextView gt5 = (TextView) findViewById(R.id.calac_total_5);

        int num5 = 0;
        if(!no5.getText().toString().isEmpty())
            num5 = Integer.parseInt(no5.getText().toString());
        else
            num5 = 0;
        int total5 = num5*5;
        gt5.setText(total5+"");

        //////////////////////total////////////////////////////
        TextView gt = (TextView) findViewById(R.id.GrandTotalResultTV);
        int total = total2000 + total500 + total200 + total100 + total50 + total20 + total10 + total5;
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
        formatter.applyPattern("#,##,##,###");
        String formattedTotal = formatter.format(total);
        String output = "₹"+formattedTotal+"";
        gt.setText(output);


        countPrefsEditor.putBoolean("savecount", true);
        countPrefsEditor.putString("no2000", no2000.getText().toString());
        countPrefsEditor.putString("no500", no500.getText().toString());
        countPrefsEditor.putString("no200", no200.getText().toString());
        countPrefsEditor.putString("no100", no100.getText().toString());
        countPrefsEditor.putString("no50", no50.getText().toString());
        countPrefsEditor.putString("no20", no20.getText().toString());
        countPrefsEditor.putString("no10", no10.getText().toString());
        countPrefsEditor.putString("no5", no5.getText().toString());
        countPrefsEditor.commit();

    }

}
