package com.ezyedu.student.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ezyedu.student.Fragments.fragment_insti_1;
import com.ezyedu.student.Fragments.fragment_insti_2;
import com.ezyedu.student.Fragments.fragment_insti_3;

public class fragmentInstiAdapter extends FragmentStateAdapter
{
    public fragmentInstiAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return new fragment_insti_2();
            case 2:
                return new fragment_insti_3();
        }
        return new fragment_insti_1();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
