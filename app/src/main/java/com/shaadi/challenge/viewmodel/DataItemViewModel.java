package com.shaadi.challenge.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.shaadi.challenge.R;
import com.shaadi.challenge.model.UserDataModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class DataItemViewModel {
    public UserDataModel userDataModel;

    public DataItemViewModel(UserDataModel userDataModel) {
        this.userDataModel = userDataModel;
    }


    public String getEmail() {
        return  userDataModel.getEmail();
    }

    public String getName(){
        return userDataModel.getName().getTitle() + ". "+userDataModel.getName().getFirst() + " "
                + userDataModel.getName().getLast() +" ["+ (userDataModel.getGender()
                .equalsIgnoreCase("male")?"M":"F")+"] Age: " + getAge();
    }
    public String getMobile(){
        return userDataModel.getCell()+"/ "+userDataModel.getPhone();
    }

    public String getLocation(){
        return userDataModel.getLocation().getCity()+",  "
                +userDataModel.getLocation().getState()+",  "+
                userDataModel.getLocation().getCountry();
    }

    public int getAge(){
        return userDataModel.getDob().getAge();
    }

    public String getImageurl() {
        // The URL will usually come from a model (i.e Profile)
        return userDataModel.getPicture().getLarge();
    }
    @BindingAdapter({"imageUrl"})
    public void loadImage(CircleImageView view, String imageUrl){
        if(imageUrl!=null&&!imageUrl.isEmpty())
            Picasso.with(view.getContext()).load(imageUrl)
                    .placeholder(R.drawable.placeholder).into(view);
    }

    public int isApproved(){
        return userDataModel.getApproveStatus();
    }

    public String appruvalStatus(){
        return userDataModel.getApproveStatus()==0 ?"":
                userDataModel.getApproveStatus()==1 ?"Accepted":
                        userDataModel.getApproveStatus()==2 ?"Declined":"";
    }




}
