import android.app.Application;

import com.androidnetworking.AndroidNetworking;

public class CropAppNetwork extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
