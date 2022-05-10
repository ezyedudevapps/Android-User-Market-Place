package com.ezyedu.student.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ezyedu.student.Fragments.Cancelled_Orders_Fragment;
import com.ezyedu.student.Fragments.Completed_Orders_Fragment;
import com.ezyedu.student.Fragments.Pending_Orders_Fragment;
import com.ezyedu.student.Fragments.Processing_Orders_Fragment;

public class HIstory_Fragment_Adapter extends FragmentStateAdapter {
    public HIstory_Fragment_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return new Processing_Orders_Fragment();
            case 2:
                return new Completed_Orders_Fragment();
            case 3:
                return new Cancelled_Orders_Fragment();
        }
        return new Pending_Orders_Fragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
