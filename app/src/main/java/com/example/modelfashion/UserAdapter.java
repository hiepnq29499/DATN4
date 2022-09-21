package com.example.modelfashion;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfashion.databinding.ItemContainerUserBinding;
import com.example.modelfashion.network.UserListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHoder> {

    private final List<User> user;
    private final UserListener userListener;

    public UserAdapter(List<User> user, UserListener userListener) {
        this.user = user;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding inItemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new UserViewHoder(inItemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHoder holder, int position) {
        holder.setUserData(user.get(position));
    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    class UserViewHoder extends RecyclerView.ViewHolder {

        ItemContainerUserBinding binding;

        UserViewHoder(ItemContainerUserBinding itemContainerUserBinding) {
            super(itemContainerUserBinding.getRoot());

            binding = itemContainerUserBinding;
        }

        void setUserData(User user) {
            binding.tvName.setText(user.name);
            binding.tvText.setText(user.email);
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }

    }
}
