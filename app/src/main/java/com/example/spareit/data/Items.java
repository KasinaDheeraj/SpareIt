package com.example.spareit.data;

import android.util.Pair;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Items {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    //Name of item
    @ColumnInfo(name="name")
    public String name;

    //Present count of items available
    @ColumnInfo(name="count")
    public int count;

    //Name of Vendor
    @ColumnInfo(name="vendorName")
    public String vendorName;

    //PhoneNumber of Vendor
    @ColumnInfo(name="phoneNo")
    public String phoneNo;

    //Threshold count for the item
    @ColumnInfo(name="threshold",defaultValue = "10")
    public int threshold;

    //Sales information as pair of date and item count sold.
    @ColumnInfo(name="salesInfo")
    public List<Pair<String, Integer>> salesInfo;

}
