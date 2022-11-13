package edu.uncc.hw08;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

import edu.uncc.hw08.adaptors.MyChatsListViewAdapter;
import edu.uncc.hw08.databinding.FragmentMyChatsBinding;
import edu.uncc.hw08.models.Conversation;
import edu.uncc.hw08.models.User;

public class MyChatsFragment extends Fragment {
    public static final String TAG = "MyChatsFragment";
    private static final String ARG_PARAM = "param1";
    ArrayList<Conversation> conversations = new ArrayList<Conversation>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    MyChatsListViewAdapter adapter;
    FragmentMyChatsBinding binding;
    MyChatsListener mListener;
    Conversation conversation;
    User mUser;

    public MyChatsFragment() {
        // Required empty public constructor
    }

    public static MyChatsFragment newInstance(User param) {
        MyChatsFragment fragment = new MyChatsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, param);
        fragment.setArguments(args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyChatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Chats");

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        mUser = new User();
                        mUser.setUserId(value.getString("userId"));
                        mUser.setUserName(value.getString("userName"));
                        mUser.setOnline((Boolean) value.get("isOnline"));
                        mUser.setConversations((ArrayList<String>) value.get("conversations"));
                        Log.d(TAG, "onEvent: " + mUser);
                        if (mUser.getConversations().size() != 0) {
                            CollectionReference ref = db.collection("conversations");
                            ArrayList<String> conversationIds = mUser.getConversations();
                            Log.d(TAG, "onEvent: " + Arrays.asList(mUser.getConversations()));
                            ref.whereArrayContains("id", mUser.getConversations());
                            ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                }
                            });


                        }

                    }
                });

        binding.listView.setAdapter(adapter);
        adapter = new MyChatsListViewAdapter(getActivity(), R.layout.my_chats_list_item, conversations);
        binding.listView.setAdapter(adapter);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                conversation = conversations.get(position);
                Log.d(TAG, "onItemClick: " + conversation);
                mListener.gotoConversation(conversation.id);
            }
        });

        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
                docRef.update("isOnline", false)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                mAuth.signOut();
                                mListener.gotoLogin();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                MyAlertDialog.show(getContext(), "Error", e.getMessage());
                            }
                        });

            }
        });

        binding.buttonNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.createChat();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (MyChatsListener) context;
    }

    interface MyChatsListener {
        void gotoLogin();

        void gotoConversation(String conversationId);

        void createChat();
    }
}