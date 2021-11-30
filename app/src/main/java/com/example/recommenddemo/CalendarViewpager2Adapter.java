package com.example.recommenddemo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class CalendarViewpager2Adapter extends FragmentStateAdapter {
    private int calendarDay;
    public CalendarViewpager2Adapter(@NonNull CalendarActivity fragment,int calendarDay) {
        super(fragment);
        this.calendarDay = calendarDay;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        CalendarFragment calendarFragment =new CalendarFragment(position);
        return calendarFragment;
    }

    @Override
    public int getItemCount() {
        return calendarDay;
    }
}
