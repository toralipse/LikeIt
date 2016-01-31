package toralipse.myapp.likeit;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tmtiger on 2015/07/26.
 */
public class MatchUtils {

    public static double calcPoint(User user,Image image){
        List<String> tags = image.getTagList();
        HashMap<String,Double> tagWeightMap = user.getTagWeightMap();
        double point=0.0;
        for(String tag:tags){
            Double weight = tagWeightMap.get(tag);
            if(weight != null) point += weight;
        }
        return point;
    }
}
