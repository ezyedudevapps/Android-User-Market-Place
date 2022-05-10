package com.ezyedu.student.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ezyedu.student.Fragments.SearchArticles;
import com.ezyedu.student.Fragments.SearchCourse;
import com.ezyedu.student.Fragments.SearchInstitution;

public class SearchFragmentAdapter extends FragmentStateAdapter
{
    public SearchFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        switch (position)
        {
            case 1 :
                return new SearchCourse();
            case 2 :
                return new SearchArticles();
        }
        return new SearchInstitution();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
