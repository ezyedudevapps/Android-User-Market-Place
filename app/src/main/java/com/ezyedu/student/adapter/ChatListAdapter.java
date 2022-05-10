package com.ezyedu.student.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezyedu.student.Chat_Activity;
import com.ezyedu.student.R;
import com.ezyedu.student.model.ChatList;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListHoldser>
{
    private Context context;
private List<ChatList> chatListList = new ArrayList<>();

    public ChatListAdapter(Context context, List<ChatList> chatListList) {
        this.context = context;
        this.chatListList = chatListList;
    }

    @NonNull
    @Override
    public ChatListHoldser onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_adapter,parent,false);
        return new ChatListHoldser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListHoldser holder, int position)
    {
        ChatList a = chatListList.get(position);
        holder.Name.setText(a.getName());
        holder.message.setText(a.getMessage());
        holder.time.setText(a.getTime());
        Integer cc = a.getUnread_Count();
        String ccc  = String.valueOf(cc);
        Log.i("stringCount",ccc);
        if (ccc.equals("0"))
        {
            holder.Count.setVisibility(View.GONE);
        }
        else {
            holder.Count.setText(ccc);
        }
        Glide.with(context).load(a.getImage()).into(holder.imageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat_Activity.class);
                intent.putExtra("Institution_name",a.getName());
                intent.putExtra("Vendor_id",a.getReceiver_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatListList == null ?0: chatListList.size();
    }

    public class ChatListHoldser extends RecyclerView.ViewHolder {
        TextView Name, message,time,Count;
        RelativeLayout relativeLayout;
        ImageView imageView;


        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();
        String img_url_base;


        public ChatListHoldser(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.chat_ven_name);
            message = itemView.findViewById(R.id.txt_messages);
            time = itemView.findViewById(R.id.time_txt);
            Count = itemView.findViewById(R.id.count_unread);
            imageView = itemView.findViewById(R.id.img_rec);
            relativeLayout = itemView.findViewById(R.id.rel_chat_list);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}
