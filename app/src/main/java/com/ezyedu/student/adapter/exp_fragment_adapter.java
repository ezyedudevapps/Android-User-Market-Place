package com.ezyedu.student.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ezyedu.student.Fragments.Fragment_Articles;
import com.ezyedu.student.Fragments.Fragment_Explore;
import com.ezyedu.student.Fragments.Fragment_promos;

public class exp_fragment_adapter extends FragmentStateAdapter
{

    public exp_fragment_adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 1:
                return new  Fragment_promos();
            case 2:
                return new  Fragment_Articles();
        }
        return new Fragment_Explore();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
