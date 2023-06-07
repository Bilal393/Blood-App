package com.abrish.ptvsports.myblood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phoneNumberTextView, cityTextView,type_TextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_name);
            phoneNumberTextView = itemView.findViewById(R.id.text_view_phone_number);
            cityTextView = itemView.findViewById(R.id.text_view_city);
            type_TextView = itemView.findViewById(R.id.text_view_type);
        }

        public void bind(User user) {
            nameTextView.setText(user.getName());
            phoneNumberTextView.setText(user.getNumber());
            cityTextView.setText(user.getCity());
            type_TextView.setText(user.getUserType());
        }
    }
}

