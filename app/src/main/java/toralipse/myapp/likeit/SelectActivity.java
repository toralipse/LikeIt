package toralipse.myapp.likeit;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class SelectActivity extends AppCompatActivity {

    private MySelectView[] images = new MySelectView[12];
    private LocalSave localSave;
    List<Image> list;
    private long start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        start = System.currentTimeMillis();
        initUI();
        localSave = new LocalSave(this);
        list = localSave.getImageRandom(12);
        if(list.size()<12){
            Toast.makeText(this,"regist Images!",Toast.LENGTH_LONG).show();
            finish();
        }else {
            for (int i = 0; i < 12; i++) {
                images[i].setImage(list.get(i));
                images[i].setLocalSava(localSave);
            }
        }
    }

    private void initUI() {
        images[0] = (MySelectView)findViewById(R.id.image1);
        images[1] = (MySelectView)findViewById(R.id.image2);
        images[2] = (MySelectView)findViewById(R.id.image3);
        images[3] = (MySelectView)findViewById(R.id.image4);
        images[4] = (MySelectView)findViewById(R.id.image5);
        images[5] = (MySelectView)findViewById(R.id.image6);
        images[6] = (MySelectView)findViewById(R.id.image7);
        images[7] = (MySelectView)findViewById(R.id.image8);
        images[8] = (MySelectView)findViewById(R.id.image9);
        images[9] = (MySelectView)findViewById(R.id.image10);
        images[10] = (MySelectView)findViewById(R.id.image11);
        images[11] = (MySelectView)findViewById(R.id.image12);

        Button exe = (Button) findViewById(R.id.exec_ok);
        exe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long alltime = System.currentTimeMillis() - start;
                for(int i = 0 ; i<12 ; i++){
                    images[i].regist(alltime);
                }
                finish();
            }
        });
    }

}
