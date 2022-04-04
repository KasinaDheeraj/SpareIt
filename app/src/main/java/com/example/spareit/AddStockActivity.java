package com.example.spareit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spareit.adapters.RVItemAdapter;
import com.example.spareit.data.AppDatabase;

public class AddStockActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RVItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        getSupportActionBar().setTitle("Add Stock");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set local attributes to corresponding views
        recyclerView = (RecyclerView) this.findViewById(R.id.recycler_view_stock_activity);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter=new RVItemAdapter(this,true);
        adapter.setListener(new RVItemAdapter.Listener() {
            @Override
            public void onClick(int position) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddStockActivity.this);
                LayoutInflater inflater = AddStockActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_admin, null);
                dialogBuilder.setView(dialogView);

                final EditText countET = dialogView.findViewById(R.id.passAdmin);
                countET.setHint("Stock Quantity");
                countET.setInputType(InputType.TYPE_CLASS_NUMBER);

                dialogBuilder.setTitle("Enter Stock quantity to be added");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        if(!countET.getText().toString().trim().equalsIgnoreCase("")){
                            int count= Integer.parseInt(countET.getText().toString().trim());
                            AppDatabase db=AppDatabase.getDbInstance(AddStockActivity.this);
                            db.userDao().deleteItem(adapter.items.get(position).uid);
                            adapter.items.get(position).count+=count;
                            db.userDao().insertItems(adapter.items.get(position));
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(AddStockActivity.this,"Enter quantity",Toast.LENGTH_LONG).show();
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
        recyclerView.setAdapter(adapter);

    }
}