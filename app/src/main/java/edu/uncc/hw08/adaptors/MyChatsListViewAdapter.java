package edu.uncc.hw08.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import edu.uncc.hw08.R;
import edu.uncc.hw08.models.Conversation;

public class MyChatsListViewAdapter extends ArrayAdapter<Conversation> {

    ArrayList<Conversation> myChats = new ArrayList<Conversation>();
    String currentUserId;

    public MyChatsListViewAdapter(@NonNull Context context, int resource, List<Conversation> myChats, String currentUserId) {
        super(context, resource, myChats);
        this.myChats = (ArrayList<Conversation>) myChats;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_chats_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewMsgBy = convertView.findViewById(R.id.textViewMsgBy);
            viewHolder.textViewMsgOn = convertView.findViewById(R.id.textViewMsgOn);
            viewHolder.textViewMsgText = convertView.findViewById(R.id.textViewMsgText);
            convertView.setTag(viewHolder);
        }
        Conversation myChat = getItem(position);
        Log.d("TAG", "getView: " + myChat);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        String id1 = myChat.getSenderId();
        String id2 = myChat.getReceiverId();
        String temp;
        if (!currentUserId.equals(id1))
            temp = id1;
        else
            temp = id2;

        FirebaseFirestore.getInstance().collection("users").document(temp)
                        .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        viewHolder.textViewMsgBy.setText(documentSnapshot.getString("userName"));
                                    }
                                });

        viewHolder.textViewMsgOn.setText(myChat.latestMessageAt);
        viewHolder.textViewMsgText.setText(myChat.latestMessage);

        return convertView;
    }

    private class ViewHolder {
        TextView textViewMsgBy;
        TextView textViewMsgOn;
        TextView textViewMsgText;
    }
}