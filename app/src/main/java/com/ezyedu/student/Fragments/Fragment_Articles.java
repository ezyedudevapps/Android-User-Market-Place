package com.ezyedu.student.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ezyedu.student.R;
import com.ezyedu.student.adapter.FragmentArticleAdapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.fragmentArticle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Articles extends Fragment {

    RecyclerView recyclerView;
    FragmentArticleAdapter fragmentArticleAdapter;
    private RequestQueue requestQueue;
    private List<fragmentArticle> falist = new ArrayList<>();
    ProgressDialog progressDialog;

    GridLayoutManager manager1;
    int pageArticles = 1;
    int totalItemCountArticles;
    int firstVisibleItemCountArticles;
    int visibleItemCountArticles;
    int previousTotalArticles;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    boolean loadArticles = true;

    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        requestQueue = Volley.newRequestQueue(getContext());




        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__articles, container, false);
        recyclerView = view.findViewById(R.id.arti_fragment);

        linearLayout = view.findViewById(R.id.ln_crse);
        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        fragmentArticleAdapter = new FragmentArticleAdapter(getContext(),falist);
    //    LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager1 = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager1);
        recyclerView.setAdapter(fragmentArticleAdapter);
        fetchArticles();


        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        return view;
    }


    private void fetchArticles()
    {
        String url = base_app_url+"api/blog?page="+pageArticles;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length() == 0)
                    {
                     linearLayout.setVisibility(View.VISIBLE);
                    }
                    progressDialog.dismiss();
                    for(int a = 0; a<jsonArray.length();a++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(a);
                        String name = jsonObject.getString("title");
                        String image = jsonObject.getString("image");
                        JSONObject jsonObject1 = jsonObject.getJSONObject("image_url");
                      //  String image = jsonObject1.getString("original");
                        String hashid = jsonObject.getString("hash_id");


                        fragmentArticle post = new fragmentArticle(name,image,hashid);
                        falist.add(post);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
                catch (JSONException e) {
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