package instagram.downloader.com.androiduberremake;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SplashScreenActivity extends AppCompatActivity {
    private final static int LOGIN_REQUEST_CODE=7171; //Any number you want
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;

    @BindView(R.id.progressBar)
    ProgressBar progress_bar;

    FirebaseDatabase database;
    DatabaseReference driverInfoRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //delaySplashScreen();
        init();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        delaySplashScreen();
    }

    @Override
    protected void onStop()
    {

        if (firebaseAuth!=null && listener!=null)
        {
            firebaseAuth.removeAuthStateListener(listener);
        }
        super.onStop();
    }










    private void init() {
        ButterKnife.bind(this);
        database = FirebaseDatabase.getInstance();
        driverInfoRef = database.getReference(Common.DRIVER_INFO_REFERENCES);
        providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
                firebaseAuth = FirebaseAuth.getInstance();
                listener = myFirebaseAuth -> {
                    FirebaseUser user = myFirebaseAuth.getCurrentUser();
                    if (user!=null)
                    {
                        checkUserFromFirebase();
                    }
                    else
                    {
                        showLoginLayout();
                    }
                };


    }

    private void showLoginLayout() {
        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout
                .Builder(R.layout.layout_sign_in)
                .setPhoneButtonId(R.id.btn_phone_sign_in)
                .setGoogleButtonId(R.id.btn_google_sign_in)
                .build();

        startActivityForResult(AuthUI.getInstance()
        .createSignInIntentBuilder()
                .setAuthMethodPickerLayout(authMethodPickerLayout)
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(providers)
                .build(), LOGIN_REQUEST_CODE);
    }

    private void checkUserFromFirebase()
    {
        driverInfoRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            Toast.makeText(SplashScreenActivity.this,"User already register", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            showRegisterLayout();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(SplashScreenActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void showRegisterLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_register, null);
        TextInputEditText edt_first_name = itemView.findViewById(R.id.edt_first_name);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            }
            else
            {
                Toast.makeText(this, "[Error]:"  +response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void delaySplashScreen() {
        progress_bar.setVisibility(View.VISIBLE);
        Completable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(() ->
                //After show Splash Screen, ask Login if not Login
                firebaseAuth.addAuthStateListener(listener));
    }
}


