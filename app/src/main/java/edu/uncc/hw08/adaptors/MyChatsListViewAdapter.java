package edu.uncc.hw08.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.uncc.hw08.R;
import edu.uncc.hw08.databinding.MyChatsListItemBinding;
import edu.uncc.hw08.models.Conversation;

public class MyChatsListViewAdapter extends ArrayAdapter<Conversation> {

    ArrayList<Conversation> myChats = new ArrayList<Conversation>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    MyChatsListItemBinding mBinding;


    public MyChatsListViewAdapter(@NonNull Context context, int resource, List<Conversation> myChats) {
        super(context, resource, myChats);
        this.myChats = (ArrayList<Conversation>) myChats;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_chats_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewMsgBy = mBinding.textViewMsgBy;
            viewHolder.textViewMsgOn = mBinding.textViewMsgOn;
            viewHolder.textViewMsgText = mBinding.textViewMsgText;
            convertView.setTag(viewHolder);
        }
        Conversation myChat = getItem(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.textViewMsgBy.setText(myChat.latestChat.messageBy);
        viewHolder.textViewMsgOn.setText(myChat.latestChat.message);
        viewHolder.textViewMsgText.setText(myChat.latestChat.messageAt);

        return convertView;
    }

    public HashMap<String, Object> createMap(Conversation myChat) {
        HashMap<String, Object> map = new HashMap<>();

        return map;
    }

    private class ViewHolder {
        TextView textViewMsgBy;
        TextView textViewMsgOn;
        TextView textViewMsgText;
    }
}