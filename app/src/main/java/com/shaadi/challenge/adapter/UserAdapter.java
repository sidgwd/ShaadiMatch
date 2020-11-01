package com.shaadi.challenge.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.shaadi.challenge.R;
import com.shaadi.challenge.database.DatabaseClient;
import com.shaadi.challenge.database.RoomHelper;
import com.shaadi.challenge.databinding.ItemDataBinding;
import com.shaadi.challenge.model.UserDataModel;
import com.shaadi.challenge.viewmodel.DataItemViewModel;

import java.util.ArrayList;
import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.DataViewHolder> {
    private List<UserDataModel> data;

    public UserAdapter() {
        this.data = new ArrayList<>();
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data,
                new FrameLayout(parent.getContext()), false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        UserDataModel userDataModel = data.get(position);
        holder.itemView.setTag(data.get(position));
        holder.setViewModel(new DataItemViewModel(userDataModel),position);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    @Override
    public void onViewAttachedToWindow(DataViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(DataViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    public void updateData(@Nullable List<UserDataModel> data) {
        if (data == null || data.isEmpty()) {
            this.data.clear();
        } else {
            this.data.clear();
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

      class DataViewHolder extends RecyclerView.ViewHolder {
         ItemDataBinding binding;
         int currentViewPosistion;
          UserDataModel userDataModel;
          DataViewHolder(View itemView) {
            super(itemView);
            bind();
        }

         void bind() {
            if (binding == null) {
                binding = DataBindingUtil.bind(itemView);
                binding.ivApprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateapproveStatusFields(1);
                    }
                });


                binding.ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateapproveStatusFields(2);
                    }
                });
            }
        }

        void updateapproveStatusFields(int statusVal){
            RoomHelper.databaseWriteExecutor.execute(() -> {
                DatabaseClient dbClient= DatabaseClient.getInstance(binding.ivApprove.getContext());
                dbClient.getAppDatabase().UserDao().update(statusVal,
                        ((UserDataModel)itemView.getTag()).getUserId());
            });
            data.get(currentViewPosistion).setApproveStatus(statusVal);
            notifyItemChanged(currentViewPosistion);
        }

         void unbind() {
            if (binding != null) {
                binding.unbind(); // Don't forget to unbind
            }
        }

         void setViewModel(DataItemViewModel viewModel,
                           int currentViewPosition) {
            if (binding != null) {
                binding.setViewModel(viewModel);
            }

             this.currentViewPosistion=currentViewPosition;
        }
    }
}
