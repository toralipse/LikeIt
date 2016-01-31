package toralipse.myapp.likeit;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MySelectView extends ImageView {

    private boolean selected = false;
    private Image src;
    private long sum = 0;
    private long tmp = 0;
    private LocalSave localSava;

    public MySelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MySelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySelectView(Context context) {
        super(context);
        init();
    }

    private void init(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ;
            }
        });
    }

    public void setLocalSava(LocalSave localSava) {
        this.localSava = localSava;
    }

    public void setImage(Image src){
        this.src = src;
        setImageBitmap(BitmapFactory.decodeFile(src.getPath()));
    }

    public void regist(long alltime){
        if(selected){
            sum += System.currentTimeMillis() - tmp;
        }
        localSava.addImageToUser(src,alltime,sum);
    }

    @Override
    public boolean performClick() {
        selected=!selected;
        if(selected){
            setColorFilter(Color.parseColor("#88ffffff"));
            tmp = System.currentTimeMillis();
        }else{
            setColorFilter(null);
            sum += System.currentTimeMillis() - tmp;
        }
        return super.performClick();
    }


}
