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

import edu.uncc.hw08.databinding.MyChatsListItemBinding;
import edu.uncc.hw08.models.MyChat;

public class MyChatsRecyclerViewAdapter extends RecyclerView.Adapter<MyChatsRecyclerViewAdapter.ViewHolder> {

    ArrayList<MyChat> myChats = new ArrayList<MyChat>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Context context;

    public MyChatsRecyclerViewAdapter(Context context, ArrayList<MyChat> comments) {
        this.myChats = comments;
        this.context = context;
    }

    public HashMap<String, Object> createMap(MyChat myChat) {
        HashMap<String, Object> map = new HashMap<>();

        return map;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyChatsListItemBinding binding = MyChatsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyChat myChat = myChats.get(position);
        holder.setupUI(myChat);
    }

    @Override
    public int getItemCount() {
        return myChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MyChatsListItemBinding mBinding;

        public ViewHolder(MyChatsListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void setupUI(MyChat myChat) {
            mBinding.textViewMsgBy.setText(myChat.latestChat.chat);
            mBinding.textViewMsgText.setText(myChat.latestChat.chat);
            mBinding.textViewMsgOn.setText(myChat.latestChat.chatAt);
        }
    }
}