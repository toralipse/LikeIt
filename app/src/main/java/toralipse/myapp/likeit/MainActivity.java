package toralipse.myapp.likeit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private LocalSave localSave;
    private User user;
    private ListView listview;
    private List<Image> list = new ArrayList<Image>();
    private ShowAdapter adapter;
    private TextView userText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        localSave = new LocalSave(this);
        user = localSave.getUser();
        if(user == null){
            Intent next = new Intent(this,LoginActivity.class);
            startActivity(next);
            finish();
        }
    }

    private void initUI() {
        listview = (ListView)findViewById(R.id.imageList);
        adapter = new ShowAdapter(this,0,list);
        listview.setAdapter(adapter);
        findViewById(R.id.choose_act).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(MainActivity.this,SelectActivity.class);
                startActivity(next);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_register) {
            Intent intent = new Intent(this,ImageRegisterActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_showUserDetail) {
            userText.setVisibility(View.VISIBLE);
            return true;
        }
        if (id == R.id.menu_logout) {
            localSave.setUser(new User());
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.menu_deleteUserData) {
            localSave.realm.beginTransaction();
            user.setTagWeightMap(new HashMap<String, Double>());
            localSave.realm.commitTransaction();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userText = (TextView)findViewById(R.id.user_data);
        userText.setText(user.toString());
        list.clear();
        list.addAll(localSave.getImages());
        Collections.sort(list, new Comparator<Image>() {
            @Override
            public int compare(Image lhs, Image rhs) {
                return (int)((MatchUtils.calcPoint(user,rhs)-MatchUtils.calcPoint(user,lhs))*100);
            }
        });
        /*adapter.sort(new Comparator<Image>() {
            @Override
            public int compare(Image lhs, Image rhs) {
                return (int)((MatchUtils.calcPoint(user,rhs)-MatchUtils.calcPoint(user,lhs))*100);
            }
        });*/
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        adapter.notifyDataSetChanged();
    }

    public class ShowAdapter extends ArrayAdapter<Image> {

        public ShowAdapter(Context context, int resource, List<Image> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Image image = getItem(position);
            if(convertView == null) {
                final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.main_row, null);
            }

            ImageView imageView = (ImageView)convertView.findViewById(R.id.main_row_image);
            Bitmap bitmap = BitmapFactory.decodeFile(image.getPath());
            convertView.setMinimumHeight((int) (convertView.getWidth() * ((double) bitmap.getHeight() / (double) bitmap.getWidth())));
            imageView.setImageBitmap(bitmap);


            return convertView;
        }
    }
}
