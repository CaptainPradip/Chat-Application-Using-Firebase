package edu.uncc.hw08;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MyChatsFragment.MyChatsListener,
        CreateChatFragment.CreateChatListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new MyChatsFragment())
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
                .replace(R.id.rootView, new CreateChatFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancel() {
        getSupportFragmentManager().popBackStack();
    }
}