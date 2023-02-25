package instagram.downloader.com.androiduberremake;


import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SplashScreenActivity extends AppCompatActivity {
    private final static int LOGIN_REQUEST_CODE=7171; //Any number you want
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        //delaySplashScreen();
        init();
    }

    private void init() {
        providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
                firebaseAuth = FirebaseAuth.getInstance();
                listener = myFirebaseAuth -> {
                    FirebaseUser user = myFirebaseAuth.getCurrentUser();
                    if (user!=null)
                    {
                        delaySplashScreen();
                    }
                    else
                    {
                        showLoginLayout();
                    }
                };


    }

    private void showLoginLayout() {
    }

    private void delaySplashScreen() {
        Completable.timer(5, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(() -> Toast.makeText(SplashScreenActivity.this, "Splash Screen Done!!!", Toast.LENGTH_LONG).show());
    }
}
