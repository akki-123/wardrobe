package practice.wardrobe;

import android.app.Application;

public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseManager.init(this);

//		ImageHelper.getInstance().init(this);

    }
}