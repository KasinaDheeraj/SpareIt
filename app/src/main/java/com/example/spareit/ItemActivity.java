package com.example.spareit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spareit.adapters.RVItemAdapter;
import com.example.spareit.data.AppDatabase;
import com.example.spareit.data.Items;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ItemActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RVItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        getSupportActionBar().setTitle("Items");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // Set local attributes to corresponding views
        recyclerView = (RecyclerView) this.findViewById(R.id.recycler_view_item_activity);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter=new RVItemAdapter(this,true);
//        adapter.setListener(new RVItemAdapter.Listener() {
//            @Override
//            public void onClick(int position) {
//                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ItemActivity.this);
//                LayoutInflater inflater = ItemActivity.this.getLayoutInflater();
//                final View dialogView = inflater.inflate(R.layout.dialog_admin, null);
//                dialogBuilder.setView(dialogView);
//
//                final EditText countET = dialogView.findViewById(R.id.passAdmin);
//                countET.setHint("Stock Quantity");
//                countET.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//                dialogBuilder.setTitle("Enter Stock quantity to be added");
//                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener()
//                {
//                    public void onClick(DialogInterface dialog, int whichButton)
//                    {
//                        if(!countET.getText().toString().trim().equalsIgnoreCase("")){
//                            int count= Integer.parseInt(countET.getText().toString().trim());
//                            AppDatabase db=AppDatabase.getDbInstance(ItemActivity.this);
//                            db.userDao().deleteItem(adapter.items.get(position).uid);
//                            adapter.items.get(position).count+=count;
//                            db.userDao().insertItems(adapter.items.get(position));
//                            adapter.notifyDataSetChanged();
//                        }else{
//                            Toast.makeText(ItemActivity.this,"Enter quantity",Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                });
//                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
//                {
//                    public void onClick(DialogInterface dialog, int whichButton)
//                    {
//
//                    }
//                });
//                AlertDialog ad = dialogBuilder.create();
//                ad.setCancelable(false);
//                ad.show();
//            }
//        });
        recyclerView.setAdapter(adapter);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(ItemActivity.this,R.color.green))
                        .addSwipeRightActionIcon(R.drawable.ic_edit)
                        .setSwipeRightActionIconTint(ContextCompat.getColor(ItemActivity.this,R.color.white))
                        .addSwipeRightLabel("EDIT")
                        .setSwipeRightLabelColor(ContextCompat.getColor(ItemActivity.this,R.color.white))
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(ItemActivity.this, R.color.red))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete)
                        .setSwipeLeftActionIconTint(ContextCompat.getColor(ItemActivity.this, R.color.white))
                        .addSwipeLeftLabel("DELETE")
                        .setSwipeLeftLabelColor(ContextCompat.getColor(ItemActivity.this, R.color.white))
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Left dirction=4 and Right direction=8
                if(direction==8){
                    //Edit by delete and add
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
                    builder.setMessage("Are you sure you want to edit?")
                            .setTitle("Edit Details")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    AppDatabase db=AppDatabase.getDbInstance(ItemActivity.this);
                                    int p=viewHolder.getAbsoluteAdapterPosition();
                                    Items s=adapter.items.get(p);

                                    db.userDao().deleteItem(s.uid);

                                    adapter.removeItem(p);

                                    Intent intent =new Intent(ItemActivity.this,AddItemActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("uid",String.valueOf(s.uid));
                                    extras.putString("Name",s.name);
                                    extras.putInt("count",s.count);
                                    extras.putInt("purchasePrice",s.purchasePrice);
                                    extras.putInt("salePrice",s.salePrice);
                                    extras.putString("vendorName",s.vendorName);
                                    extras.putString("phoneNo",s.phoneNo);
                                    extras.putString("address",s.address);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else if(direction==4){

                    //Delete from database
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
                    builder.setMessage("Are you sure you want to delete this item?")
                            .setTitle("Delete Spare part details")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AppDatabase db=AppDatabase.getDbInstance(ItemActivity.this);
                                    int p=viewHolder.getAbsoluteAdapterPosition();
                                    Items s=adapter.items.get(p);

                                    db.userDao().deleteItem(s.uid);

                                    adapter.removeItem(p);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        menu.getItem(0).setTitle("ADD");
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_baseline_library_add));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.Save){
            Intent intent =new Intent(ItemActivity.this,AddItemActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        adapter.dataSetChanged();
        super.onResume();
    }
}