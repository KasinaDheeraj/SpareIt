package com.example.spareit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spareit.adapters.RVItemAdapter;
import com.example.spareit.adapters.RVItemSelectAdapter;
import com.example.spareit.data.AppDatabase;
import com.example.spareit.data.Items;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ItemSelectActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RVItemSelectAdapter adapter;
    SimpleDateFormat sdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_select);

        getSupportActionBar().setTitle("Select Items");

        String pattern="MM-dd-YYYY";
        sdf=new SimpleDateFormat(pattern);

        TextView total=findViewById(R.id.total);

        // Set local attributes to corresponding views
        recyclerView = (RecyclerView) this.findViewById(R.id.recycler_view_item_select_activity);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter=new RVItemSelectAdapter(this);
        adapter.setListener(new RVItemAdapter.Listener() {
            @Override
            public void onClick(int position) {
                total.setText("Total : Rs."+adapter.Total);
                if(position==-1){
                    Toast.makeText(ItemSelectActivity.this,"Stock Unavailable",Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void placeOrder(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(ItemSelectActivity.this);
        builder.setMessage("Are you sure you want to bill?")
                .setTitle("Bill Details")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AppDatabase db=AppDatabase.getDbInstance(ItemSelectActivity.this);

                        for(int i=0;i<adapter.counts.size();i++){
                            if(adapter.counts.get(i)>0){
                                db.userDao().deleteItem(adapter.items.get(i).uid);
                                adapter.items.get(i).count-=adapter.counts.get(i);

                                //Formatting date to store in database
                                String date=sdf.format(new Date());
                                adapter.items.get(i).salesInfo.add(new Pair<>(date,adapter.counts.get(i)));
                                updateThreshold(i);
                                db.userDao().insertItems(adapter.items.get(i));

                            }
                        }

                        adapter.notifyDataSetChanged();

                        Intent intent=new Intent(ItemSelectActivity.this,OrderPlacedActivity.class);
                        startActivity(intent);

                        ItemSelectActivity.this.recreate();

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

    public void updateThreshold(int i){

        //Get Map as list of sales in each week.
        Map<String,List<Pair<String,Integer>>> map=adapter.items.get(i).salesInfo.stream()
                .collect(Collectors.groupingBy(s->{
                    try {
                        Date date=sdf.parse(s.first);
                        Calendar cal=Calendar.getInstance();
                        cal.setTime(date);
                        return (cal.get(Calendar.YEAR)+1)+"~"+cal.get(Calendar.WEEK_OF_YEAR);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return "";
                }));

        //Map of each week and count sold
        Map<String, Double> finalResult = map.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue()
                                .stream()
                                .mapToDouble(s -> s.second)
                                .sum()));
        //Assigning average
        adapter.items.get(i).threshold = (int)finalResult.entrySet()
                .stream()
                .mapToDouble(x -> x.getValue())
                .average()
                .orElse(10); ;

        //Log.e("Update Threshold",""+adapter.items.get(i).threshold);

    }
}