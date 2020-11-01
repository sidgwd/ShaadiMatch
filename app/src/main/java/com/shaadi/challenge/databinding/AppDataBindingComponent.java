package com.shaadi.challenge.databinding;

import com.shaadi.challenge.model.UserDataModel;
import com.shaadi.challenge.viewmodel.DataItemViewModel;

public class AppDataBindingComponent implements android.databinding.DataBindingComponent {

    @Override
    public DataItemViewModel getDataItemViewModel() {
        return new DataItemViewModel(new UserDataModel());
    }

    @Override
    public RecyclerViewDataBinding getRecyclerViewDataBinding() {
        return new RecyclerViewDataBinding();
    }
}
