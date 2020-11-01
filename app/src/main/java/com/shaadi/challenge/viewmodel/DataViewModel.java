package com.shaadi.challenge.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.shaadi.challenge.BR;
import com.shaadi.challenge.adapter.UserAdapter;
import com.shaadi.challenge.database.DatabaseClient;
import com.shaadi.challenge.database.RoomHelper;
import com.shaadi.challenge.model.UserDataModel;

import java.util.ArrayList;
import java.util.List;


public class DataViewModel extends BaseObservable {
    private static final String TAG = "DataViewModel";
    private UserAdapter adapter;
    private List<UserDataModel> data;
    Context context;

    public DataViewModel() {
        data = new ArrayList<>();
        adapter = new UserAdapter();
    }

    public void setUp(Context context) {
        // perform set up tasks, such as adding listeners, data population, etc.
        this.context=context;
        populateData();
    }

    public void tearDown() {
        // perform tear down tasks, such as removing listeners
    }

    @Bindable
    public List<UserDataModel> getData() {
        return this.data;
    }

    @Bindable
    public UserAdapter getAdapter() {
        return this.adapter;
    }

    private void populateData() {
        RoomHelper.databaseWriteExecutor.execute(() -> {
            DatabaseClient dbClient= DatabaseClient.getInstance(context);
            data = dbClient.getAppDatabase().UserDao().getAllUserData();
            notifyPropertyChanged(BR.data);
        });
    }
}
