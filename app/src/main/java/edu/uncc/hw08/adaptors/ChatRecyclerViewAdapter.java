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

import edu.uncc.hw08.databinding.ChatListItemBinding;
import edu.uncc.hw08.models.Chat;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    ArrayList<Chat> chats = new ArrayList<Chat>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Context context;

    public ChatRecyclerViewAdapter(Context context, ArrayList<Chat> comments) {
        this.chats = comments;
        this.context = context;
    }

    public HashMap<String, Object> createMap(Chat chat) {
        HashMap<String, Object> map = new HashMap<>();

        return map;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatListItemBinding binding = ChatListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.setupUI(chat);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ChatListItemBinding mBinding;

        public ViewHolder(ChatListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void setupUI(Chat chat) {
            mBinding.textViewMsgBy.setText(chat.chatBy);
            mBinding.textViewMsgText.setText(chat.chat);
            mBinding.textViewMsgOn.setText(chat.chatAt);
        }
    }
}