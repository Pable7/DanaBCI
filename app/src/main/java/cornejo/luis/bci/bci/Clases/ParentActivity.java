package cornejo.luis.bci.bci.Clases;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ParentActivity {

    List<Activity> activities =  new ArrayList<Activity>();

    public void addActiviy(Activity activity){
        activities.add(activity);
    }

    public void destroyActivies(){
        for (Activity a : activities) {
            a.finish();
        }
    }
}
