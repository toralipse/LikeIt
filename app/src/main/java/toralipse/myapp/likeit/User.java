package toralipse.myapp.likeit;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by tmtiger on 2015/07/21.
 */
public class User extends RealmObject {
    private String name;
    private String password;
    private String tagWeight;

    @Ignore
    private HashMap<String,Double> tagWeightMap;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashMap<String, Double> getTagWeightMap() {
        tagWeightMap = new HashMap<String,Double>();
        try {
            JSONObject object = new JSONObject(getTagWeight());
            Iterator<String> iterator = object.keys();
            while (iterator.hasNext()){
                String key = iterator.next();
                tagWeightMap.put(key,object.getDouble(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tagWeightMap;
    }

    public void setTagWeightMap(HashMap<String, Double> tagWeightMap) {
        try{
            JSONObject object = new JSONObject();
            Iterator<String> iterator = tagWeightMap.keySet().iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                object.put(key,tagWeightMap.get(key));
            }
            setTagWeight(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTagWeight() {
        return tagWeight;
    }

    public void setTagWeight(String tagWeight) {
        this.tagWeight = tagWeight;
    }

}
