package toralipse.myapp.likeit;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by tmtiger on 2015/07/21.
 */
public class MyAdapter extends ArrayAdapter<String> {
    List<String> pathes;

    public MyAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        pathes = objects;
    }

}
