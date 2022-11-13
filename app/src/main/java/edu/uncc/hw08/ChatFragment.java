package edu.uncc.hw08;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import edu.uncc.hw08.adaptors.ChatRecyclerViewAdapter;
import edu.uncc.hw08.databinding.FragmentChatBinding;
import edu.uncc.hw08.models.Message;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private static final String ARG_PARAM = "param1";
    ArrayList<Message> messages = new ArrayList<>();
    ChatListener mListener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ChatRecyclerViewAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FragmentChatBinding binding;
    // TODO: Rename and change types of parameters
    private String mConversationId;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String conversationId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, conversationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mConversationId = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setHasFixedSize(true);
        adapter = new ChatRecyclerViewAdapter(getContext(), messages, mListener, mConversationId);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.scrollToPosition(messages.size() - 1);

        db.collection("conversations").document(mConversationId).collection("messages").orderBy("messageAt")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        messages.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Message message = new Message();
                            message.setMessage(doc.getString("message"));
                            message.setMessageBy(doc.getString("messageBy"));
                            message.setMessageAt(doc.getString("messageAt"));
                            message.setMessageId(doc.getString("messageId"));
                            message.setSenderId(doc.getString("senderId"));
                            messages.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String message = binding.editTextMessage.getText().toString();

                if (message.isEmpty()) {
                    MyAlertDialog.show(getContext(), "Error", "Please enter message");
                } else {

                    HashMap<String, Object> map = new HashMap<>();
                    String messageId = UUID.randomUUID().toString();
                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
                    String dateTime = localDateTime.format(formatter);
                    map.put("messageAt", dateTime);
                    map.put("messageId", messageId);
                    map.put("message", message);
                    map.put("senderId", mAuth.getCurrentUser().getUid());
                    map.put("messageBy", mAuth.getCurrentUser().getDisplayName());
                    HashMap<String, Object> conversationMap = new HashMap<>();

                    LocalDateTime now = LocalDateTime.now();
                    String dateTimeString = now.format(formatter);

                    map.put("senderId", mAuth.getCurrentUser().getUid());
                    map.put("latestMessage", message);
                    map.put("latestMessageAt", dateTimeString);
                    DocumentReference ref = db.collection("conversations").document(mConversationId);
                    map.put("latestMessageBy", mAuth.getCurrentUser().getDisplayName());
                    ref.update(conversationMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            ref.collection("messages").document(messageId)
                                    .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });


                        }
                    });
                }
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ChatListener) context;
    }

    public interface ChatListener {
        void cancel();
    }
}