package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.AboutFragment;
import com.example.myapplication.fragments.AdminFragment;
import com.example.myapplication.fragments.RegistrationFragment;
import com.example.myapplication.fragments.StudentsFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AdminFragment();
            case 1:
                return new RegistrationFragment();
            case 2:
                return new StudentsFragment();
            case 3:
                return new AboutFragment();
            default:
                return new AdminFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
