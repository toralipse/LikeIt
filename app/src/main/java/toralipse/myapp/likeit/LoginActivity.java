package toralipse.myapp.likeit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button login;
    private Button signup;
    private LocalSave localSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        localSave = new LocalSave(this);
        initUI();
    }

    private void initUI() {
        userName = (EditText)findViewById(R.id.user_name);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(loginClick);
        signup = (Button)findViewById(R.id.signup);
        signup.setOnClickListener(signupClick);
    }

    private void saveUser(User user){
        localSave.setUser(user);
        Intent next = new Intent(this,MainActivity.class);
        startActivity(next);
        finish();
    }

    private View.OnClickListener loginClick= new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            view.setEnabled(false);
            User user = localSave.login(userName.getText().toString(),password.getText().toString());
            if(user == null){
                view.setEnabled(true);
                return;
            }
            saveUser(user);
        }
    };

    private View.OnClickListener signupClick= new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            view.setEnabled(false);
            User user = localSave.regist(userName.getText().toString(),password.getText().toString());
            if(user == null){
                view.setEnabled(true);
                return;
            }
            saveUser(user);
        }
    };


}
