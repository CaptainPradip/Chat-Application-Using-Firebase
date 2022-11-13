package edu.uncc.hw08.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.hw08.ChatFragment;
import edu.uncc.hw08.MyAlertDialog;
import edu.uncc.hw08.databinding.ChatListItemBinding;
import edu.uncc.hw08.models.Message;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private final String mConversationId;
    ArrayList<Message> chats = new ArrayList<Message>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ChatFragment.ChatListener mListener;
    Context context;

    public ChatRecyclerViewAdapter(Context context, ArrayList<Message> messages, ChatFragment.ChatListener mListener, String mConversationId) {
        this.chats = messages;
        this.context = context;
        this.mListener = mListener;
        this.mConversationId = mConversationId;
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
            if (chat.getSenderId() == mAuth.getCurrentUser().getUid()) {
                mBinding.textViewMsgBy.setText("Me");
            } else {
                mBinding.textViewMsgBy.setText(chat.messageBy);
            }
            mBinding.textViewMsgText.setText(chat.message);
            mBinding.textViewMsgOn.setText(chat.messageAt);
            if (chat.senderId == null || !chat.getSenderId().equals(mAuth.getCurrentUser().getUid())) {
                mBinding.imageViewDelete.setVisibility(View.INVISIBLE);
            } else {
                mBinding.imageViewDelete.setVisibility(View.VISIBLE);
                mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("conversations").document(mConversationId).collection("messages").document(chat.getMessageId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        MyAlertDialog.show(context, "Error", e.getMessage());
                                    }
                                });
                    }
                });
            }
        }
    }
}