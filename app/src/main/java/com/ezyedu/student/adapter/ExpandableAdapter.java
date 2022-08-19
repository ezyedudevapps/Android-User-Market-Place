package com.ezyedu.student.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ezyedu.student.MainActivity;
import com.ezyedu.student.New_Checkout_Page;
import com.ezyedu.student.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    ArrayList<String> listGroup;
    HashMap<String,ArrayList<String>> listChild;
    ArrayList<Integer> plist;

    public ExpandableAdapter(ArrayList<String> listGroup, HashMap<String, ArrayList<String>> listChild, ArrayList<Integer> plist) {
        this.listGroup = listGroup;
        this.listChild = listChild;
        this.plist = plist;
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChild.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return listGroup == null ?0: listGroup.size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return listChild == null ?0: listChild.size();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_expandable_list_item_1,parent,false);
        TextView textView = convertView.findViewById(android.R.id.text1);
        String sGroup = String.valueOf(getGroup(groupPosition));
        textView.setText(sGroup);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(R.color.orange);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_expandable_list_item_1,parent,false);
        TextView textView = convertView.findViewById(android.R.id.text1);
        String SChild = String.valueOf(getChild(groupPosition,childPosition));

        String [] a = SChild.split(":");
        String idd = a[0];
        int id = Integer.parseInt(idd);
        textView.setText(a[1]);

        textView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                //pass data
                Intent intent1 = new Intent("custom-message");
                intent1.putExtra("quantity",SChild);
                LocalBroadcastManager.getInstance(parent.getContext()).sendBroadcast(intent1);





            /*    Intent intent = new Intent(parent.getContext(), New_Checkout_Page.class);
                intent.putExtra("bank_rate_id",id);
                parent.getContext().startActivity(intent);
                Log.i("childpos",idd);
             // Toast.makeText(parent.getContext(), SChild, Toast.LENGTH_SHORT).show();

             */
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
