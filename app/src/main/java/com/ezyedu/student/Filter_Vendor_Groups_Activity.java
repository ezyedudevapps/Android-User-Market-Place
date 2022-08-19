package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Filter_Vendor_Groups_Activity extends AppCompatActivity {

    RequestQueue requestQueue;

    //get all category
    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<Integer> plist = new ArrayList<>();
    TextView categories;
    Dialog dialog;

    ProgressDialog progressDialog;
    //get sub category
    ArrayList<String> glist = new ArrayList<>();
    ArrayList<Integer> hlist = new ArrayList<>();
    TextView sub_category;
    Dialog dialog5;

    String language =null;

    //sort drop down
    Spinner sort;
    ArrayList<String> sortby = new ArrayList<>();
    ArrayAdapter<String> adapter;

    //get all city
    ArrayList<String> clist = new ArrayList<String>();
    ArrayList<Integer> ilist = new ArrayList<>();
    TextView city;
    Dialog dialog1;

    //get all province
    TextView province;
    ArrayList<String> alist = new ArrayList<String>();
    ArrayList<Integer> blist = new ArrayList<>();
    Dialog dialog2;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    // data to collect from filter
    String categoryy;
    int category_hash_id;
    int sub_category_id;
    String sort_val = null;
    int city_id;
    int province_id;


    TextView categry,subcategry,dfsort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter__vendor__groups_);

        //get domain url
        base_app_url = sharedData.getValue();
    //    Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
      //  Log.i("img_url_global",img_url_base);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        fetchCategories();



        categories = findViewById(R.id.txt_categories);
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Filter_Vendor_Groups_Activity.this);
                dialog.setContentView(R.layout.dialog_all_vendor_group_spinner);
                dialog.getWindow().setLayout(650,1200);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);


                ArrayAdapter<String> adapter = new ArrayAdapter<>(Filter_Vendor_Groups_Activity.this,android.R.layout.simple_list_item_1,mylist);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        categories.setText(adapter.getItem(position));
                        categoryy = adapter.getItem(position);
                        int pos = position;
                        Log.i("catPos", String.valueOf(pos));
                        dialog.dismiss();
                        category_hash_id = plist.get(position);
                        Log.i("Hash_id_val", String.valueOf(category_hash_id));

                        if (category_hash_id !=0)
                        {
                            progressDialog = new ProgressDialog(Filter_Vendor_Groups_Activity.this);
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog);
                            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                            glist.clear();
                            hlist.clear();
                            getSubCategory();
                        }
                    }
                });
            }
        });

        //sub-category
        sub_category = findViewById(R.id.txt_sub_categories);
        sub_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog5 = new Dialog(Filter_Vendor_Groups_Activity.this);
                dialog5.setContentView(R.layout.dialog_all_vendor_group_spinner);
                dialog5.getWindow().setLayout(650,1200);
                dialog5.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog5.show();

                EditText editText = dialog5.findViewById(R.id.edit_text);
                ListView listView = dialog5.findViewById(R.id.list_view);


                ArrayAdapter<String> adapter = new ArrayAdapter<>(Filter_Vendor_Groups_Activity.this,android.R.layout.simple_list_item_1,glist);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        sub_category.setText(adapter.getItem(position));
                        int pos = position;
                        Log.i("sub_cat_catPos", String.valueOf(pos));
                        dialog5.dismiss();
                        sub_category_id = hlist.get(position);
                        Log.i("sub_cat_id", String.valueOf(sub_category_id));
                    }
                });
            }
        });

        //city
        getCity();
        city = findViewById(R.id.txt_citys);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1 = new Dialog(Filter_Vendor_Groups_Activity.this);
                dialog1.setContentView(R.layout.dialog_all_vendor_group_spinner);
                dialog1.getWindow().setLayout(850,1200);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();

                EditText editText = dialog1.findViewById(R.id.edit_text);
                ListView listView = dialog1.findViewById(R.id.list_view);


                ArrayAdapter<String> adapter = new ArrayAdapter<>(Filter_Vendor_Groups_Activity.this,android.R.layout.simple_list_item_1,clist);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        city.setText(adapter.getItem(position));
                        int pos = position;
                        Log.i("citypos", String.valueOf(pos));
                        dialog1.dismiss();
                        city_id = ilist.get(position);
                        Log.i("city_id", String.valueOf(city_id));
                    }
                });
            }
        });

        //province
        getProvince();
        province = findViewById(R.id.txt_province);
        province.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2 = new Dialog(Filter_Vendor_Groups_Activity.this);
                dialog2.setContentView(R.layout.dialog_all_vendor_group_spinner);
                dialog2.getWindow().setLayout(850,1200);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();

                EditText editText = dialog2.findViewById(R.id.edit_text);
                ListView listView = dialog2.findViewById(R.id.list_view);


                ArrayAdapter<String> adapter = new ArrayAdapter<>(Filter_Vendor_Groups_Activity.this,android.R.layout.simple_list_item_1,alist);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        province.setText(adapter.getItem(position));
                        int pos = position;
                        Log.i("citypos", String.valueOf(pos));
                        dialog2.dismiss();
                        province_id = blist.get(position);
                        Log.i("province_id", String.valueOf(province_id));
                    }
                });
            }
        });





        //sort
        sort = findViewById(R.id.sort_popup);
        sortby.add("Default Sort");
        sortby.add("Sort by Group Name");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,sortby);
        sort.setAdapter(adapter);

        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sort_val = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageView imageView = findViewById(R.id.lft_arrrow);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Filter_Vendor_Groups_Activity.this,Institution_Groups.class);
                startActivity(intent1);
            }
        });

        Button filter = findViewById(R.id.apply_filter_btn);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (category_hash_id == 0 & sub_category_id ==0 & sort_val.equals("Default Sort") & city_id == 0 & province_id == 0)
                {
                    Intent intent1 = new Intent(Filter_Vendor_Groups_Activity.this,Institution_Groups.class);
                    startActivity(intent1);
                }
                else {
                    Intent intent1 = new Intent(Filter_Vendor_Groups_Activity.this, Filtered_Institution_Groups.class);
                    intent1.putExtra("category_hash_id", category_hash_id);
                    intent1.putExtra("categoryy", categoryy);
                    intent1.putExtra("sub_category_id", sub_category_id);
                    intent1.putExtra("sort_val", sort_val);
                    intent1.putExtra("city_id", city_id);
                    intent1.putExtra("province_id", province_id);
                    startActivity(intent1);
                }
            }
        });

        categry= findViewById(R.id.ct_1);
    subcategry = findViewById(R.id.sct1);
        dfsort = findViewById(R.id.sot1);

        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);

        if (language.equals("Indonesia"))
        {
            categry.setText("Kategori");
            subcategry.setText("Sub-Kategori");
            dfsort.setText("Sortir dari");
            categories.setText("Kategori");
            sub_category.setText("Sub-Kategori");
        }
    }


    private void getSubCategory()
    {
        String url = base_app_url+"api/vendor/subcategory?all=1&vendor_category_id="+category_hash_id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();
                for (int i = 0;i<response.length();i++)
                {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String label = jsonObject.getString("label");
                        glist.add(label);
                        hlist.add(id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void getProvince()
    {
        String url = base_app_url+"api/geograph/province";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0;i<response.length();i++)
                {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String label = jsonObject.getString("name");
                        int hash_id = jsonObject.getInt("id");
                        alist.add(label);
                        blist.add(hash_id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getCity()
    {
        String url = base_app_url+"api/geograph/city?province_id=7";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0;i<response.length();i++)
                {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String label = jsonObject.getString("name");
                        int hash_id = jsonObject.getInt("id");
                        clist.add(label);
                        ilist.add(hash_id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void fetchCategories()
    {
        String url = base_app_url+"api/vendor/category?sort[label]=asc&all=1";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0;i<response.length();i++)
                {
                    try {
                       JSONObject jsonObject = response.getJSONObject(i);
                        String label = jsonObject.getString("label");
                        int id = jsonObject.getInt("id");
                        mylist.add(label);
                        plist.add(id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}