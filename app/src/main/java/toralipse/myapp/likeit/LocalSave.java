package toralipse.myapp.likeit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by tmtiger on 2015/07/21.
 */
public class LocalSave {
    Activity activity;
    Realm realm;
    SharedPreferences sharedPreferences;

    public LocalSave(Activity activity){
        this.activity = activity;
        realm = Realm.getInstance(activity, "toralipse.realm");
        sharedPreferences = activity.getSharedPreferences("toralipse", Context.MODE_PRIVATE);
    }

    public void setUser(User user){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", user.getName());
        editor.putString("password", user.getPassword());
        editor.apply();
    }

    public User getUser(){
        String name = sharedPreferences.getString("name",null);
        String password = sharedPreferences.getString("password",null);
        if(name == null){
            return null;
        }
        return login(name,password);
    }

    public User login(String name,String password){
        RealmResults<User> results =
                realm
                        .where(User.class)
                        .equalTo("name", name)
                        .equalTo("password",password)
                        .findAll();
        if(results.size()==1){
            //Toast.makeText(activity,"Login OK!",Toast.LENGTH_LONG).show();
            return results.first();
        }else{
            //Toast.makeText(activity, "Login error!", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public User regist(String name,String password){
        RealmResults<User> results =
                realm
                        .where(User.class)
                        .equalTo("name", name)
                        .findAll();
        if(results.size()!=0){
            Toast.makeText(activity,"UserName has already registered!",Toast.LENGTH_LONG).show();
            return null;
        }

        realm.beginTransaction();
        User user = realm.createObject(User.class);
        user.setName(name);
        user.setPassword(password);
        user.setTagWeightMap(new HashMap<String, Double>());
        realm.commitTransaction();

        Toast.makeText(activity,"Register OK!",Toast.LENGTH_LONG).show();
        return user;
    }

    public Image registImage(String path,String[] tags){
        RealmResults<Image> results =
                realm
                        .where(Image.class)
                        .equalTo("path", path)
                        .findAll();
        if(results.size()!=0){
            Toast.makeText(activity,"This image has already registered!",Toast.LENGTH_LONG).show();
            if(results.size()==1){
                realm.beginTransaction();
                results.first().setTagList(Arrays.asList(tags));
                realm.commitTransaction();
            }
            return null;
        }

        realm.beginTransaction();
        Image image = realm.createObject(Image.class);
        image.setPath(path);
        image.setTagList(Arrays.asList(tags));
        realm.commitTransaction();

        return image;
    }

    public List<Image> getImages() {
        RealmResults<Image> results =
                realm
                        .where(Image.class)
                        .findAll();
        List<Image> images = new ArrayList<Image>();
        for(int i=0;i<results.size();i++){
            images.add(results.get(i));
        }
        return images;
    }

    public List<Image> getImageRandom(int count){
        List<Image> items = getImages();
        if(count>items.size()){
            return items;
        }
        Collections.shuffle(items);
        return items.subList(0,count);
    }

    public List<Image> getImageForTag(String tag){
        RealmResults<Image> results =
                realm
                        .where(Image.class)
                        .contains("tags",tag)
                        .findAll();
        List<Image> images = new ArrayList<Image>();
        for(int i=0;i<results.size();i++){
            images.add(results.get(i));
        }
        return images;
    }

    public void addImageToUser(User user,Image image,double weight){
        List<String> tags = image.getTagList();
        HashMap<String,Double> tagWeightMap = user.getTagWeightMap();

        for(String tag:tags){
            Double point;
            if( (point = tagWeightMap.get(tag)) == null){
                tagWeightMap.put(tag,weight);
            }else{
                tagWeightMap.put(tag,point+weight);
            }
        }
        realm.beginTransaction();
        user.setTagWeightMap(tagWeightMap);
        realm.commitTransaction();
    }
    public void addImageToUser(User user,Image image,long allTime,long selectedTime){
        addImageToUser(user, image, (double)selectedTime/(double)allTime );
    }
    public void addImageToUser(Image image,long allTime,long selectedTime){
        addImageToUser(getUser(), image, (double)selectedTime/(double)allTime );
    }
}
