package com.ezyedu.student.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ezyedu.student.Fragments.Fragment_bookmark_articles;
import com.ezyedu.student.Fragments.fragment_bookmark_course;

public class Bookmarks_fragment_adapter extends FragmentStateAdapter {
    public Bookmarks_fragment_adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return new Fragment_bookmark_articles();
        }
        return new fragment_bookmark_course();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
