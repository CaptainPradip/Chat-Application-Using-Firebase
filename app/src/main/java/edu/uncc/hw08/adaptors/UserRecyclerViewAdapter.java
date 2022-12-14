package edu.uncc.hw08.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.hw08.CreateChatFragment;
import edu.uncc.hw08.R;
import edu.uncc.hw08.databinding.UsersRowItemBinding;
import edu.uncc.hw08.models.Conversation;
import edu.uncc.hw08.models.User;

/*
 * Homework 08
 * UserRecyclerViewAdapter.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    ArrayList<User> users = new ArrayList<User>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Context context;
    CreateChatFragment createChatFragment;

    public UserRecyclerViewAdapter(Context context, ArrayList<User> users, CreateChatFragment createChatFragment) {
        this.users = users;
        this.context = context;
        this.createChatFragment = createChatFragment;
    }

    public HashMap<String, Object> createMap(Conversation myChat) {
        HashMap<String, Object> map = new HashMap<>();
        return map;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UsersRowItemBinding binding = UsersRowItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.setupUI(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UsersRowItemBinding mBinding;

        public ViewHolder(UsersRowItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void setupUI(User user) {

            mBinding.userRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createChatFragment.setSelectedUser(user);
                }
            });

            mBinding.textViewName.setText(user.getUserName());
            mBinding.imageViewOnline.setImageResource(R.drawable.ic_online);
            if (user.isOnline()) {
                mBinding.imageViewOnline.setVisibility(View.VISIBLE);
            } else {
                mBinding.imageViewOnline.setVisibility(View.INVISIBLE);
            }

        }
    }

    interface UserAdapterListener {
        void setSelectedUser(User user);
    }
}