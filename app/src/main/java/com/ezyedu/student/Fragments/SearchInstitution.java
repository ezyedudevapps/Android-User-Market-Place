package com.ezyedu.student.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ezyedu.student.R;
import com.ezyedu.student.adapter.SearchInstitutionAdapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.SearchInstitutionClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchInstitution extends Fragment {

    EditText editText;
    RecyclerView recyclerView;
    RequestQueue requestQueue;

    GridLayoutManager manager3;
    int pageinsti = 1;
    int totalItemCountinsti;
    int firstVisibleItemCountinsti;
    int visibleItemCountinsti;
    int previousTotalinsti;
    boolean loadinsti = true;

    LinearLayout linearLayout;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    ProgressDialog progressDialog;
    SearchInstitutionAdapter searchInstitutionAdapter;
    private List<SearchInstitutionClass> searchInstitutionClassList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search_institution, container, false);


        linearLayout = view.findViewById(R.id.ln_crse);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        requestQueue= Volley.newRequestQueue(getContext());

        editText = view.findViewById(R.id.search_institution_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        recyclerView = view.findViewById(R.id.search_institute_recycler);

        manager3 = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager3);
    //    LinearLayoutManager manager = new LinearLayoutManager(getContext());
        searchInstitutionAdapter = new SearchInstitutionAdapter(getContext(),searchInstitutionClassList);
      //  recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(searchInstitutionAdapter);
        fetchInstitutions();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        return view;
    }

    private void filter(String text)
    {
        ArrayList<SearchInstitutionClass> filteredList = new ArrayList<>();
        for (SearchInstitutionClass item : searchInstitutionClassList)
        {
            if (item.getTittle().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }

        searchInstitutionAdapter.filterList(filteredList);
    }





    private void fetchInstitutions()
    {
        String url = base_app_url+"api/vendor?page="+pageinsti;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (jsonArray.length()>0) {


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Integer id = jsonObject.getInt("id");
                            String tittle = jsonObject.getString("name");
                            String logo = jsonObject.getString("logo");
                            String address = jsonObject.getString("address");
                            String hashid = jsonObject.getString("hash_id");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("vendor_group");
                            String learn_type = jsonObject1.getString("group_name");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("vendor_category");
                            String image = jsonObject2.getString("image");
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("image_url");
                            //  String image = jsonObject3.getString("small");

                            JSONObject jsonObject4 = jsonObject.getJSONObject("vendor_reviews");

                            double average_rate = 0;
                            if (!jsonObject4.isNull("average_rate")) {
                                average_rate = jsonObject4.getDouble("average_rate");
                            }

                            int review = jsonObject4.getInt("total_review");

                            SearchInstitutionClass post = new SearchInstitutionClass(learn_type,
                                    tittle,
                                    address, average_rate,
                                    logo, hashid, id,review);
                            searchInstitutionClassList.add(post);
                        }
                    }
                    else
                    {
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonObjectRequest);


        paginationInstitutions();
    }

    private void paginationInstitutions()
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItemCountinsti = manager3.findFirstVisibleItemPosition();
                totalItemCountinsti = manager3.getItemCount();
                visibleItemCountinsti = manager3.getChildCount();

                if (loadinsti)
                {
                    if (totalItemCountinsti > previousTotalinsti)
                    {
                        previousTotalinsti = totalItemCountinsti;
                        pageinsti++;
                        loadinsti = false;
                    }
                }
                if (!loadinsti && (firstVisibleItemCountinsti+visibleItemCountinsti) >= totalItemCountinsti)
                {
                    fetchInstitutions();
                    loadinsti = true;
                }
            }
        });
    }
}