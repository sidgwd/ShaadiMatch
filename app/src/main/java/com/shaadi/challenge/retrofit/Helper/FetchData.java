/**
 * Copyright (C) 2014 Viraat Technology Labs Pvt. Ltd.
 * http://www.viraat.info
 */
package com.shaadi.challenge.retrofit.Helper;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shaadi.challenge.R;
import com.shaadi.challenge.Util.Utility;
import com.shaadi.challenge.database.DatabaseClient;
import com.shaadi.challenge.database.RoomHelper;
import com.shaadi.challenge.model.UserDataModel;
import com.shaadi.challenge.retrofit.rest.ApiClient;
import com.shaadi.challenge.retrofit.rest.ApiInterface;
import com.shaadi.challenge.view.MainActivity;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchData {
    final static String TAG = FetchData.class.getSimpleName().toString();
   static ArrayList<UserDataModel> lstUserdata =new ArrayList<>();
    public static void callWebMethod(final Context context,
                                     String count) throws Exception {

        if(Utility.isInternetConnected(context)){
            // prepare call in Retrofit 2.0
            //initialing interface to parse details
            //Defined interface Object//as  it contains calling web method and parsed htmlData format
            ApiInterface apiService =
                    ApiClient.getClient(context).create(ApiInterface.class);

            Call<ResponseBody> call = apiService.fetchUser(count);
            try {
                //adding request into calle//show prgress bar
                Utility.showProgress(context, "Please wait...", call);

                final String request = Utility.getRequest(call.request().body());

                //calling and getting the response
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        call.request().body();
                        //checking the response code
                        if (response.code() == 200) {
                            //Try to get response body
                            try {
                                String resultJsonn =response.body().string().toString();
                                System.out.println("Data from api:"+ resultJsonn);

                               String resultArr= new JSONObject(resultJsonn).get("results").toString();

                                Type type = new TypeToken<ArrayList<UserDataModel>>() {
                                }.getType();
                                lstUserdata = new Gson().fromJson(resultArr, type);

                              DatabaseClient dbClient= DatabaseClient.getInstance(context);
                                RoomHelper.databaseWriteExecutor.execute(() -> {
//                                    dbClient.getAppDatabase().UserDao().deleteAll();
                                    dbClient.getAppDatabase().UserDao().insert(lstUserdata);
                                    MainActivity.getInstance().populateData();
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Utility.showPopup(context, "Error", response.message());
                        }
                        //hide progress bar
                        Utility.hideProgress();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        //  Toast.makeText(context, t.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                        //this methos is called when the web request execution fails
                        if (call != null && t.getLocalizedMessage() != null) {
                            if (!call.isCanceled()) {
                                Utility.showPopup(context, "Error", t.getLocalizedMessage().toString());
                            }
                        } else {
                            Utility.showPopup(context, "Error", "Unable to connect\nplease contact service administrator.");
                        }
                        //hide progress bar
                        Utility.hideProgress();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                //setting the error if exception has caught
                Utility.showPopup(context, "Error", e.toString());
            }
        }else {
            Utility.showPopup(context,"",context.getResources().getString(R.string.no_internet_msg_text));
        }
    }


}