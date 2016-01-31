package toralipse.myapp.likeit;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by tmtiger on 2015/07/21.
 */
public class Image extends RealmObject {
    private String path;
    private String tags;

    @Ignore
    private
    List<String> tagList;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<String> getTagList() {
        tagList = new ArrayList<String>();
        try {
            JSONArray object = new JSONArray(getTags());
            for(int i=0;i<object.length();i++){
                tagList.add(object.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        JSONArray object = new JSONArray();
        for(String tag:tagList){
            object.put(tag);
        }
        setTags(object.toString());
    }
}
