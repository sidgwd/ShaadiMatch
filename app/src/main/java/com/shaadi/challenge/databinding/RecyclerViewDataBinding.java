package com.shaadi.challenge.databinding;


import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.shaadi.challenge.adapter.UserAdapter;
import com.shaadi.challenge.model.UserDataModel;

import java.util.List;


public class RecyclerViewDataBinding {

    @BindingAdapter({"app:adapter", "app:data"})
    public void bind(RecyclerView recyclerView, UserAdapter adapter, List<UserDataModel> data) {
        recyclerView.setAdapter(adapter);
        adapter.updateData(data);
    }
}
