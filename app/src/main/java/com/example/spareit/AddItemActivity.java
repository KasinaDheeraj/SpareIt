package com.example.spareit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spareit.data.AppDatabase;
import com.example.spareit.data.Items;

import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity {
    EditText nameET;
    EditText countET;
    EditText vendorNameET;
    EditText vendorPhNoET;
    EditText pPriceET;
    EditText sPriceET;
    EditText addressET;

    String uid=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        nameET=findViewById(R.id.nameET);
        countET=findViewById(R.id.countET);
        vendorNameET=findViewById(R.id.VnameET);
        vendorPhNoET=findViewById(R.id.VnumET);
        pPriceET=findViewById(R.id.amountET);
        sPriceET=findViewById(R.id.saleET);
        addressET=findViewById(R.id.addressET);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            uid=extras.getString("uid");
            nameET.setText(extras.getString("Name"));
            countET.setText(String.valueOf(extras.getInt("count")));
            pPriceET.setText(String.valueOf(extras.getInt("purchasePrice")));
            sPriceET.setText(String.valueOf(extras.getInt("salePrice")));
            vendorNameET.setText(extras.getString("vendorName"));
            vendorPhNoET.setText(extras.getString("phoneNo"));
            addressET.setText(extras.getString("address"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.Save){

            String name=nameET.getText().toString().trim();
            String vendorName=vendorNameET.getText().toString().trim();
            String vendorPhNo=vendorPhNoET.getText().toString().trim();
            String address=addressET.getText().toString().trim();

            if(name.equals("")||countET.getText().toString().trim().equals("")||vendorName.equals("")||vendorPhNo.equals("")||pPriceET.getText().toString().trim().equals("")||sPriceET.getText().toString().trim().equals("")||address.equals("")){
                Toast.makeText(this,"Please enter valid details!",Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
            int count=Integer.parseInt(countET.getText().toString().trim());
            int purchasePrice=Integer.parseInt(pPriceET.getText().toString().trim());
            int salePrice=Integer.parseInt(sPriceET.getText().toString().trim());

            Items items=new Items();
            items.name=name;
            items.count=count;
            items.purchasePrice=purchasePrice;
            items.salePrice=salePrice;
            items.vendorName=vendorName;
            items.phoneNo=vendorPhNo;
            items.address=address;
            items.threshold=10;
            items.salesInfo=new ArrayList<Pair<String,Integer>>();
            if(uid!=null)items.uid=Integer.parseInt(uid);
            AppDatabase.getDbInstance(this).userDao().insertItems(items);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}