package com.example.recommenddemo.viewmodel;

import androidx.lifecycle.LifecycleOwner;

import java.util.HashMap;

/**
 * LifecycleOwner的存储器
 */
public class LifecycleStore {

    private final HashMap<String, LifecycleOwner> mMap = new HashMap<>();

    final void put(String key, LifecycleOwner lifecycleOwner) {
        mMap.put(key, lifecycleOwner);
    }

    final LifecycleOwner get(String key) {
        return mMap.get(key);
    }

    final void remove(String key) {
        mMap.remove(key);
    }

    final int getSize() {
        return mMap.size();
    }

    public final void clear() {
        mMap.clear();
    }
}
