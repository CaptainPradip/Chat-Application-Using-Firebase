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

import edu.uncc.hw08.ChatFragment;
import edu.uncc.hw08.databinding.ChatListItemBinding;
import edu.uncc.hw08.models.Message;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    ArrayList<Message> chats = new ArrayList<Message>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ChatFragment.ChatListener mListener;
    Context context;

    public ChatRecyclerViewAdapter(Context context, ArrayList<Message> messages, ChatFragment.ChatListener mListener) {
        this.chats = messages;
        this.context = context;
        this.mListener = mListener;
    }

    public HashMap<String, Object> createMap(Message chat) {
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
        Message chat = chats.get(position);
        holder.setupUI(chat);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ChatListItemBinding mBinding;

        public ViewHolder(ChatListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void setupUI(Message chat) {
            mBinding.textViewMsgBy.setText(chat.messageBy);
            mBinding.textViewMsgText.setText(chat.message);
            mBinding.textViewMsgOn.setText(chat.messageAt);
        }
    }
}