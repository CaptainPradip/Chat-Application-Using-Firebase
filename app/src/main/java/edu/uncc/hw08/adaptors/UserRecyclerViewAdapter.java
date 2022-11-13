package edu.uncc.hw08.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.hw08.databinding.UsersRowItemBinding;
import edu.uncc.hw08.models.Conversation;
import edu.uncc.hw08.models.User;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    ArrayList<User> users = new ArrayList<User>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Context context;

    public UserRecyclerViewAdapter(Context context, ArrayList<User> users) {
        this.users = users;
        this.context = context;
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
            mBinding.textViewName.setText(user.getUserName());
            if (user.isOnlineStatus()) {
                //   mBinding.imageViewOnline.setImageDrawable();
            } else {
                //   mBinding.imageViewOnline.setImageDrawable();
            }

        }
    }
}