package edu.uncc.hw08;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import edu.uncc.hw08.models.User;

public class MainActivity extends AppCompatActivity implements MyChatsFragment.MyChatsListener,
        CreateChatFragment.CreateChatListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().hasExtra("user") && getIntent().getSerializableExtra("user") != null) {
            User user = (User) getIntent().getSerializableExtra("user");
            Log.d(TAG, "onCreate: " + user);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, MyChatsFragment.newInstance(user))
                    .commit();
        }
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