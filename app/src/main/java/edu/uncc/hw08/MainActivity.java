package edu.uncc.hw08;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.uncc.hw08.models.Conversation;

public class MainActivity extends AppCompatActivity implements MyChatsFragment.MyChatsListener, ChatFragment.ChatListener,
        CreateChatFragment.CreateChatListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new MyChatsFragment(), "MyChatsFragment")
                .commit();
    }


    @Override
    public void gotoConversation(String conversationId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, ChatFragment.newInstance(conversationId), "ChatFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoLogin() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void createChat() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new CreateChatFragment(), "CreateChatFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancel() {
        /*ChatFragment fragment = (ChatFragment) getSupportFragmentManager().findFragmentByTag("ChatFragment");
        if (fragment != null)
            getSupportFragmentManager().popBackStack();*/
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoChatFragment(String conversationId) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, ChatFragment.newInstance(conversationId), "ChatFragment")
                .commit();
    }
}