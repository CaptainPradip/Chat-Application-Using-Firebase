package edu.uncc.hw08;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.function.ObjIntConsumer;

import edu.uncc.hw08.databinding.FragmentMyChatsBinding;

public class MyChatsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FragmentMyChatsBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String mParam1;
    private String mParam2;

    public MyChatsFragment() {
        // Required empty public constructor
    }

    public static MyChatsFragment newInstance(String param1, String param2) {
        MyChatsFragment fragment = new MyChatsFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentMyChatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Chats");

        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                mListener.gotoLogin();
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

    MyChatsListener mListener;
    interface MyChatsListener {
        void gotoLogin();
        void createChat();
    }
}