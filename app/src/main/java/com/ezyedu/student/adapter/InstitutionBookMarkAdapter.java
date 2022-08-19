package com.ezyedu.student.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ezyedu.student.Bookmarks_Activity;
import com.ezyedu.student.Course_one_new;
import com.ezyedu.student.R;
import com.ezyedu.student.SeperateInstitution;
import com.ezyedu.student.model.BookMarkInstitutions;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstitutionBookMarkAdapter extends RecyclerView.Adapter<InstitutionBookMarkAdapter.InsHolder> {
    private Context context;
    private List<BookMarkInstitutions> bookMarkInstitutionsList = new ArrayList<>();

    public  static String img_url_base;
    public static  String base_app_url;
    RequestQueue requestQueue;
    String session_id = null;
    ProgressDialog progressDialog;


    public InstitutionBookMarkAdapter(Context context, List<BookMarkInstitutions> bookMarkInstitutionsList) {
        this.context = context;
        this.bookMarkInstitutionsList = bookMarkInstitutionsList;
    }

    @NonNull
    @Override
    public InsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_insti_bookmark_adapter,parent,false);
        return new InsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InsHolder holder, int position)
    {
        BookMarkInstitutions a = bookMarkInstitutionsList.get(position);
        holder.t3.setText(a.getName());
        holder.t4.setText(a.getAddress());
        Glide.with(context).load(img_url_base+a.getLogo()).into(holder.imageView);

        holder.bk_mrk.setColorFilter(ContextCompat.getColor(context,R.color.ezy));

        //hash_id = vendor_id...
        int hash_id = a.getVendor_id();
        holder.bk_mrk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(context);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                try {
                    removeBookMark(hash_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeperateInstitution.class);
                intent.putExtra("ven_id",String.valueOf(a.getVendor_id()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    private void removeBookMark(int hash_id) throws JSONException {

        String url = base_app_url+"api/bookmark";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("item_type",1);
        jsonObject.put("item_id",hash_id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("BIresponse",response.toString());
                if (response.has("message"))
                {
                    try {
                        String message = response.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Bookmarks_Activity.class);
                        context.startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization",session_id);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public int getItemCount() {
        return bookMarkInstitutionsList ==  null ?0:bookMarkInstitutionsList.size();
    }

    public class InsHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView t3,t4;
        RelativeLayout relativeLayout;
        ImageView bk_mrk;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public InsHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_course);

            t3 = itemView.findViewById(R.id.text_course);
            t4 = itemView.findViewById(R.id.course_inst_author);
            relativeLayout = itemView.findViewById(R.id.course_card);

            bk_mrk = itemView.findViewById(R.id.bk_mrk_ic);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
            session_id = sharedPreferences.getString("session_val","");
            Log.i("session_new",session_id);

            requestQueue = CourseVolleySingleton.getInstance(context).getRequestQueue();

        }
    }
}
