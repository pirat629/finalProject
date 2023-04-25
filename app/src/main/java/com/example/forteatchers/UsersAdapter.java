package com.example.forteatchers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.PrimitiveIterator;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {
    ArrayList<User> users;
    onUserClickListener onUserClickListener;
    Context context;

    public UsersAdapter(ArrayList<User> users, UsersAdapter.onUserClickListener onUserClickListener, Context context) {
        this.users = users;
        this.onUserClickListener = onUserClickListener;
        this.context = context;
    }

    interface onUserClickListener{
        void onUserClicked(int position);
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_holder,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.txtUserName.setText(users.get(position).name + " " + users.get(position).surname);
        Glide.with(context).load(users.get(position).profilePicture).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder{
        TextView txtUserName;
        ImageView imageView;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUserClickListener.onUserClicked(getAdapterPosition());
                }
            });
            txtUserName = itemView.findViewById(R.id.txtUserName);
            itemView = itemView.findViewById(R.id.imgPro);
        }
    }
}
