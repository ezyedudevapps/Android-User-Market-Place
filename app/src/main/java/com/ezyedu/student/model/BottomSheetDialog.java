package com.ezyedu.student.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ezyedu.student.R;

import org.json.JSONException;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetListner mlistner;
    SharedPreferences sharedPreferences;
    String status_code = null;
    Button chat_btn,cancel_btn;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("order_status", Context.MODE_PRIVATE);
        status_code = sharedPreferences.getString("status_code","");
        Log.i("Session_bottom_activity",status_code);

        if (status_code.equals("1"))
        {
            chat_btn.setVisibility(View.GONE);
        }
        else if (status_code.equals("2"))
        {
            chat_btn.setText("Complete your Order");
            cancel_btn.setText("Request to Cancel");
        }
        else if (status_code.equals("3"))
        {
            chat_btn.setVisibility(View.GONE);
            cancel_btn.setText("Request to Cancel");
        }
        else if (status_code.equals("7"))
        {
            chat_btn.setVisibility(View.GONE);
            cancel_btn.setText("Undo Cancel Request");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet,container,false);

         chat_btn = view.findViewById(R.id.ct_now);
         cancel_btn = view.findViewById(R.id.cancel_btm);

        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mlistner.onButtonClicked("proceed");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mlistner.onButtonClicked("cancel");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });
        return view;
    }



    public interface BottomSheetListner{
        void onButtonClicked(String text) throws JSONException;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mlistner = (BottomSheetListner) context;
    }
}
