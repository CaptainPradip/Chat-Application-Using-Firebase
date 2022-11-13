package edu.uncc.hw08;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.uncc.hw08.adaptors.UserRecyclerViewAdapter;
import edu.uncc.hw08.databinding.FragmentCreateChatBinding;
import edu.uncc.hw08.models.Conversation;
import edu.uncc.hw08.models.Message;
import edu.uncc.hw08.models.User;

public class CreateChatFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ArrayList<User> mUsers = new ArrayList<>();
    UserRecyclerViewAdapter adapter;
    User sender, receiver;

    FragmentCreateChatBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CreateChatFragment() {
        // Required empty public constructor
    }

    public static CreateChatFragment newInstance(String param1, String param2) {
        CreateChatFragment fragment = new CreateChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("New Chat");

        adapter = new UserRecyclerViewAdapter(getContext(), mUsers, this);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        db.collection("users")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                mUsers.clear();
                                for (QueryDocumentSnapshot doc: value) {
                                    User user = new User();
                                    user.setUserId(doc.getString("userId"));
                                    user.setUserName(doc.getString("userName"));
                                    user.setOnline((Boolean) doc.get("isOnline"));
                                    user.setConversations((ArrayList<String>) doc.get("conversations"));
                                    if (!doc.getId().equals(mAuth.getCurrentUser().getUid())) {
                                        mUsers.add(user);
                                    } else {
                                        sender = user;
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String message = binding.editTextMessage.getText().toString();
                if (message.isEmpty())
                    MyAlertDialog.show(getContext(), "Invalid Message", "Message field cannot be empty");
                else if (receiver == null)
                    MyAlertDialog.show(getContext(), "Error", "Please select a user");
                else {
                    HashSet<String> senderConversations = new HashSet<>(sender.getConversations());
                    HashSet<String> receiverConversations = new HashSet<>(receiver.getConversations());
                    senderConversations.retainAll(receiverConversations);

                    if (senderConversations.isEmpty())
                        createConversation(message);
                    else {
                        continueConversation(message, senderConversations.stream().findFirst().get());
                    }
                }
            }
        });


        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancel();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateChatListener) context;
    }

    public void setSelectedUser(User user) {
        binding.textViewSelectedUser.setText(user.getUserName());
        Log.d("TAG", "setSelectedUser: "+user);
        receiver = user;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void createConversation(String message) {
        HashMap<String, Object> map = new HashMap<>();
        DocumentReference docRef = db.collection("conversations").document();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        String dateTimeString = now.format(formatter);

        map.put("id", docRef.getId());
        map.put("senderId", sender.getUserId());
        map.put("receiverId", receiver.getUserId());
        map.put("latestMessage", message);
        map.put("latestMessageAt", dateTimeString);
        map.put("latestMessageBy", sender.getUserName());
        //map.put("messages", new ArrayList<>());

        docRef.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful())
                    MyAlertDialog.show(getContext(), "Error", task.getException().getMessage());
            }
        });

        db.collection("users").document(sender.getUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> conversations = sender.getConversations();
                            conversations.add(docRef.getId());

                            db.collection("users").document(sender.getUserId())
                                    .update("conversations", conversations)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                        }
                    }
                });

        db.collection("users").document(receiver.getUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> conversations = receiver.getConversations();
                            conversations.add(docRef.getId());

                            db.collection("users").document(receiver.getUserId())
                                    .update("conversations", conversations)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void continueConversation(String message, String conversationId) {
        DocumentReference docRef = db.collection("conversations").document(conversationId);

        HashMap<String, Object> map = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        String dateTimeString = now.format(formatter);

        map.put("id", docRef.getId());
        map.put("senderId", sender.getUserId());
        map.put("receiverId", receiver.getUserId());
        map.put("latestMessage", message);
        map.put("latestMessageAt", dateTimeString);
        map.put("latestMessageBy", sender.getUserName());
        docRef.set(map);

        DocumentReference msgDocRef = docRef.collection("messages").document();
        Message newMessage = new Message();
        newMessage.setMessage(message);
        newMessage.setMessageAt(dateTimeString);
        newMessage.setMessageId(msgDocRef.getId());
        newMessage.setMessageBy(sender.getUserName());
        newMessage.setSenderId(sender.getUserId());
        msgDocRef.set(newMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }

    CreateChatListener mListener;
    public interface CreateChatListener {
        void cancel();
        void chat();
    }
}