package com.example.modelfashion.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.response.Rating;
import com.example.modelfashion.Model.response.User.User;
import com.example.modelfashion.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.ViewHolder> {
    Context context;
    ArrayList<Rating> arrRating = new ArrayList<>();
    public UserCommentAdapter(Context context, ArrayList<Rating> arrRating){
        this.context = context;
        this.arrRating = arrRating;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_comment_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ApiRetrofit.apiRetrofit.GetUserById(arrRating.get(position).getUserId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                holder.tv_user_name.setText(response.body().getUserName()+": ");
                holder.tv_user_comment.setText(arrRating.get(position).getComment());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrRating.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_user_name, tv_user_comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_user_comment = itemView.findViewById(R.id.tv_user_comment);
        }
    }
}
