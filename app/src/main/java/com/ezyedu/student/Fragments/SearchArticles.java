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
import com.ezyedu.student.adapter.SearchArticlesAdapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.SearchArticlesClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchArticles extends Fragment {

    EditText editText;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    SearchArticlesAdapter searchArticlesAdapter;
    private List<SearchArticlesClass> searchArticlesClassList = new ArrayList<>();
    private String TAG = "newone";


    GridLayoutManager manager1;
    int pageArticles = 1;
    int totalItemCountArticles;
    int firstVisibleItemCountArticles;
    int visibleItemCountArticles;
    int previousTotalArticles;
    boolean loadArticles = true;

    LinearLayout linearLayout;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_articles, container, false);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        linearLayout = view.findViewById(R.id.ln_crse);


        editText = view.findViewById(R.id.search_articles_edit);
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

        requestQueue= Volley.newRequestQueue(getContext());
        recyclerView = view.findViewById(R.id.search_article_recycler);

        manager1 = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager1);
    //    LinearLayoutManager manager = new LinearLayoutManager(getContext());
        searchArticlesAdapter = new SearchArticlesAdapter(getContext(),searchArticlesClassList);
    //    recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(searchArticlesAdapter);
        fetchArticles();


        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        return view;
    }

    private void filter(String text)
    {
        ArrayList<SearchArticlesClass> filteredList = new ArrayList<>();
        for (SearchArticlesClass item : searchArticlesClassList)
        {
            if (item.getTittle().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }
        searchArticlesAdapter.filterList(filteredList);
    }

    private void fetchArticles()
    {
        String url = base_app_url+"api/blog?page="+pageArticles;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    progressDialog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (jsonArray.length()>0) {
                        Log.i("jsonval", String.valueOf(response));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("title");
                            Log.i("nameone", name);
                            String image = jsonObject.getString("image");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("image_url");
                            //  String image = jsonObject1.getString("original");
                            //Log.i("imgone",image);
                            String hashid = jsonObject.getString("hash_id");

                            SearchArticlesClass post = new SearchArticlesClass(image, name, hashid);
                            searchArticlesClassList.add(post);
                            Log.i("Arraylist_articles", String.valueOf(searchArticlesClassList.size()));

                        }
                    }
                    else
                    {
                      //  linearLayout.setVisibility(View.VISIBLE);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e)
                {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    Log.i(TAG,e.toString());
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

        paginationArticles();

    }

    private void paginationArticles()
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItemCountArticles = manager1.findFirstVisibleItemPosition();
                totalItemCountArticles = manager1.getItemCount();
                visibleItemCountArticles = manager1.getChildCount();

                if (loadArticles)
                {
                    if (totalItemCountArticles > previousTotalArticles)
                    {
                        previousTotalArticles = totalItemCountArticles;
                        pageArticles++;
                        loadArticles = false;
                    }
                }
                if (!loadArticles && (firstVisibleItemCountArticles+visibleItemCountArticles) >= totalItemCountArticles)
                {
                    fetchArticles();
                    loadArticles = true;
                }
            }
        });
    }
}