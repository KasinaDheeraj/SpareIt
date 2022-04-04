package com.example.spareit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button adminLogin=findViewById(R.id.adminLogin);
        Button employeeLogin=findViewById(R.id.empLogin);

        prefs = getSharedPreferences("MY_PREFS",MODE_PRIVATE);
        prefs.edit().putString("PASSWORD_ADMIN","ADMIN123").apply();
        prefs.edit().putString("19JE0427","SELab").apply();

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(StartActivity.this);
                LayoutInflater inflater = StartActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_admin, null);
                dialogBuilder.setView(dialogView);

                final EditText passwordET = dialogView.findViewById(R.id.passAdmin);

                dialogBuilder.setTitle("Enter Password");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        String password= String.valueOf(passwordET.getText().toString().trim());
                        if(!password.equalsIgnoreCase("")){
                            if(prefs.getString("PASSWORD_ADMIN","").equals(password)){
                                gotoMain();
                            }else{
                                Toast.makeText(StartActivity.this,"Enter correct Password",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(StartActivity.this,"Enter Password",Toast.LENGTH_LONG).show();
                        }

                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                    }
                });
                AlertDialog ad = dialogBuilder.create();
                ad.setCancelable(false);
                ad.show();
            }
        });

        employeeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(StartActivity.this);
                LayoutInflater inflater = StartActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_employee, null);
                dialogBuilder.setView(dialogView);

                final EditText empIDET = dialogView.findViewById(R.id.empID);
                final EditText passwordET = dialogView.findViewById(R.id.passEmp);

                dialogBuilder.setTitle("Enter Details");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        String empID= empIDET.getText().toString().trim();
                        String password= passwordET.getText().toString().trim();
                        if(!password.equalsIgnoreCase("")||!empID.equalsIgnoreCase("")){
                            if(prefs.getString(""+empID,"").equals(password)){
                                gotoEmp();
                            }else{
                                Toast.makeText(StartActivity.this,"Enter correct Password",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(StartActivity.this,"Enter details correctly!",Toast.LENGTH_LONG).show();
                        }

                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                    }
                });
                AlertDialog ad = dialogBuilder.create();
                ad.setCancelable(false);
                ad.show();
            }
        });
    }

    public void gotoMain(){
        Intent intent=new Intent(StartActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);;
        startActivity(intent);
        finish();
    }

    public void gotoEmp(){
        Intent intent=new Intent(StartActivity.this,ItemSelectActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);;
        startActivity(intent);
        finish();
    }

    public void go(View v){
        gotoEmp();
//        Intent intent=new Intent(StartActivity.this,MainActivity.class);
//        startActivity(intent);
    }
}