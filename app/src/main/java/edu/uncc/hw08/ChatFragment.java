package edu.uncc.hw08;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import edu.uncc.hw08.adaptors.ChatRecyclerViewAdapter;
import edu.uncc.hw08.databinding.FragmentChatBinding;
import edu.uncc.hw08.models.Conversation;
import edu.uncc.hw08.models.Message;
import edu.uncc.hw08.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private static final String ARG_PARAM = "param1";
    private static final String ARG_PARAM_CONV = "CONV";

    ArrayList<Message> messages = new ArrayList<>();
    ChatListener mListener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ChatRecyclerViewAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FragmentChatBinding binding;
    private String mConversationId;
    Conversation mConversation;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String conversationId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, conversationId);
        //args.putSerializable(ARG_PARAM_CONV, conversation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mConversationId = getArguments().getString(ARG_PARAM);
            //mConversation = (Conversation) getArguments().getSerializable(ARG_PARAM_CONV);
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


        db.collection("conversations").document(mConversationId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String id1 = documentSnapshot.getString("senderId");
                        String id2 = documentSnapshot.getString("receiverId");
                        String temp;
                        if (!mAuth.getCurrentUser().getUid().equals(id1))
                            temp = id1;
                        else
                            temp = id2;

                        FirebaseFirestore.getInstance().collection("users").document(temp)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        getActivity().setTitle("Chat - " + documentSnapshot.getString("userName"));
                                    }
                                });
                    }
                });



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
                        Collections.sort(messages);
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
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
                    String dateTime = localDateTime.format(formatter);
                    map.put("messageAt", dateTime);
                    map.put("messageId", messageId);
                    map.put("message", message);
                    map.put("senderId", mAuth.getCurrentUser().getUid());
                    map.put("messageBy", mAuth.getCurrentUser().getDisplayName());
                    HashMap<String, Object> conversationMap = new HashMap<>();
                    LocalDateTime now = LocalDateTime.now();
                    String dateTimeString = now.format(formatter);
                    conversationMap.put("latestMessage", message);
                    conversationMap.put("latestMessageAt", dateTimeString);
                    DocumentReference ref = db.collection("conversations").document(mConversationId);
                    conversationMap.put("latestMessageBy", mAuth.getCurrentUser().getDisplayName());
                    ref.update(conversationMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            ref.collection("messages").document(messageId)
                                    .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            binding.editTextMessage.setText("");
                                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //Find the currently focused view, so we can grab the correct window token from it.
                                            View view = getActivity().getCurrentFocus();
                                            //If no view currently has focus, create a new one, just so we can grab a window token from it
                                            if (view == null) {
                                                view = new View(getActivity());
                                            }
                                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                        }
                                    });
                        }
                    });
                }
            }
        });

        binding.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancel();
            }
        });

        binding.buttonDeleteChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*db.collection("conversations").document(mConversationId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String senderId = documentSnapshot.getString("senderId");
                                String receiverId = documentSnapshot.getString("receiverId");

                                db.collection("users").document(senderId)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            conversations.clear();
                                            temp.clear();
                                            conversations = (ArrayList<String>) documentSnapshot.get("conversations");
                                            for (String id: conversations) {
                                                if (id != mConversationId)
                                                    temp.add(id);
                                            }
                                            map.clear();
                                            map.put("userId", senderId);
                                            map.put("isOnline", documentSnapshot.getBoolean("isOnline"));
                                            map.put("userName", documentSnapshot.getString("userName"));
                                            map.put("conversations", temp);
                                        }
                                    });

                                db.collection("users").document(senderId)
                                        .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });

                                db.collection("users").document(receiverId)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                conversations.clear();
                                                temp.clear();
                                                conversations = (ArrayList<String>) documentSnapshot.get("conversations");
                                                for (String id: conversations) {
                                                    if (id != mConversationId)
                                                        temp.add(id);
                                                }
                                                map.clear();
                                                map.put("userId", receiverId);
                                                map.put("isOnline", documentSnapshot.getBoolean("isOnline"));
                                                map.put("userName", documentSnapshot.getString("userName"));
                                                map.put("conversations", temp);
                                            }
                                        });

                                db.collection("users").document(receiverId)
                                        .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });

                            }
                        });*/

                db.collection("conversations")
                        .document(mConversationId)
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                mListener.cancel();
                            }
                        });

                for (Message message : messages
                ) {
                    db.collection("conversations").document(mConversationId).collection("messages").document(message.getMessageId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    MyAlertDialog.show(getContext(), "Error", e.getMessage());
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