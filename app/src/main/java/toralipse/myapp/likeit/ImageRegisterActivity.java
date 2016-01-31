package toralipse.myapp.likeit;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ImageRegisterActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> list = new ArrayList<String>();
    private List<Image> list_image = new ArrayList<Image>();
    private SelectAdapter selectAdapter;
    private RegisteredAdapter registeredAdapter;
    private LocalSave localSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_register);
        selectAdapter = new SelectAdapter(this,0,list);
        registeredAdapter = new RegisteredAdapter(this,0,list_image);
        localSave = new LocalSave(this);
        initUI();
    }

    private void initUI() {
        listView = (ListView)findViewById(R.id.listview);
        Button import_local = (Button)findViewById(R.id.import_local);
        import_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setAdapter(selectAdapter);
                selectAdapter.clear();
                String[] proj1 = {
                        MediaStore.Images.Thumbnails.DATA
                };
                Cursor c1 = getContentResolver().query(
                        MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                        proj1, null, null, null);
                int count1=0;

                if(c1.moveToFirst()){
                    do{
                        selectAdapter.add(c1.getString(0));
                        if(count1++ > 500) break;
                    }while (c1.moveToNext());
                }
                selectAdapter.notifyDataSetChanged();
            }
        });
        final Button registered_item = (Button)findViewById(R.id.registered_item);
        registered_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setAdapter(registeredAdapter);
                registeredAdapter.clear();

                List<Image> images = localSave.getImages();
                //List<Image> images = localSave.getImageRandom(10);
                //List<Image> images = localSave.getImageForTag("party");

                for(Image image:images){
                    list_image.add(image);
                }
                registeredAdapter.notifyDataSetChanged();
            }
        });
    }

    public class SelectAdapter extends ArrayAdapter<String> {

        public SelectAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final String path = getItem(position);
            if(convertView == null) {
                final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.register_row, null);
            }

            TextView textView = (TextView)convertView.findViewById(R.id.row_path);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.row_image);
            final EditText editText = (EditText)convertView.findViewById(R.id.row_tag);
            editText.setText("");
            Button button = (Button)convertView.findViewById(R.id.row_regist);

            textView.setText(path);
            imageView.setImageBitmap(BitmapFactory.decodeFile(path));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(editText.getText().toString().equals("")) return;
                    String[] tags = editText.getText().toString().split(",");
                    String tags_str = "";
                    for(String tag:tags){
                        tags_str += "#"+tag+" ";
                    }
                    localSave.registImage(path,tags);
                    Toast.makeText(getContext(),"tag "+tags_str+"registed",Toast.LENGTH_LONG).show();
                }
            });

            return convertView;
        }
    }

    public class RegisteredAdapter extends ArrayAdapter<Image> {

        public RegisteredAdapter(Context context, int resource, List<Image> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Image image = getItem(position);
            if(convertView == null) {
                final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.register_row, null);
            }

            TextView textView = (TextView)convertView.findViewById(R.id.row_path);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.row_image);
            final EditText editText = (EditText)convertView.findViewById(R.id.row_tag);
            editText.setText(image.getTags());
            editText.setEnabled(false);
            Button button = (Button)convertView.findViewById(R.id.row_regist);
            button.setEnabled(false);

            textView.setText(image.getPath());
            imageView.setImageBitmap(BitmapFactory.decodeFile(image.getPath()));

            return convertView;
        }
    }
}
