package com.ezyedu.student.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezyedu.student.R;
import com.ezyedu.student.model.GetChat;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import java.util.ArrayList;
import java.util.List;

public class GetChatAdapter extends RecyclerView.Adapter<GetChatAdapter.GetChatHolder>
{

    private Context context;
    private List<GetChat> getChatList = new ArrayList<>();
    public  static String img_url_base;
    public static  String base_app_url;
    String ven_chat_id = null;
    SharedPreferences sp;
    public GetChatAdapter(Context context, List<GetChat> getChatList) {
        this.context = context;
        this.getChatList = getChatList;
    }

    @NonNull
    @Override
    public GetChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter, parent, false);
        return new GetChatHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull GetChatHolder holder, int position)
    {

        GetChat a = getChatList.get(position);

        Integer id = a.getSender_Id();
        String id_test = String.valueOf(id);

        if (!id_test.equals(ven_chat_id))
        {
            holder.sendermessage.setText(a.getMessage());
            holder.receivermessage.setVisibility(View.GONE);
        }
        else {
            holder.receivermessage.setText(a.getMessage());
            holder.sendermessage.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return getChatList == null ?0: getChatList.size();
    }

    public class GetChatHolder extends RecyclerView.ViewHolder
    {
        TextView sendermessage,receivermessage;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public GetChatHolder(@NonNull View itemView) {
            super(itemView);
            sendermessage = itemView.findViewById(R.id.sender_message);
            receivermessage = itemView.findViewById(R.id.reciver_msg);

            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("ven_ch_ide", Context.MODE_PRIVATE);
            ven_chat_id = sharedPreferences.getString("vendor_chat_id","");
            Log.i("Session_main_activity",ven_chat_id);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);


        }
    }
}
