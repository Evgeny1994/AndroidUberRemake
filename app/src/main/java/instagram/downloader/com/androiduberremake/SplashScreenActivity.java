package instagram.downloader.com.androiduberremake;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import instagram.downloader.com.androiduberremake.Model.DriverInfoModel;
import instagram.downloader.com.androiduberremake.Utils.UserUtils;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SplashScreenActivity extends AppCompatActivity {
    private final static int LOGIN_REQUEST_CODE = 7171; //Any number you want
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
    protected void onStart() {
        super.onStart();
        delaySplashScreen();
    }

    @Override
    protected void onStop() {
        if (firebaseAuth != null && listener != null) {
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
            if (user != null) {
                //Update token
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(SplashScreenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        Log.i("TOKEN", instanceIdResult.getToken());
                        UserUtils.updateToken(SplashScreenActivity.this, instanceIdResult.getToken());
                    }
                });

                checkUserFromFirebase();
            } else {
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

    private void checkUserFromFirebase() {
        driverInfoRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                           // Toast.makeText(SplashScreenActivity.this, "User already register", Toast.LENGTH_SHORT).show();
                            DriverInfoModel driverInfoModel = dataSnapshot.getValue(DriverInfoModel.class);
                            goToHomeActivity(driverInfoModel);
                        } else {
                            showRegisterLayout();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(SplashScreenActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToHomeActivity(DriverInfoModel driverInfoModel) {
        Common.currentUser = driverInfoModel; //Init values
        //ПЕРЕХОД НА КАРТЫ
        Intent intent = new Intent(getApplicationContext(), DriverHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void showRegisterLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_register, null);
        TextInputEditText edt_first_name = itemView.findViewById(R.id.edt_first_name);
        TextInputEditText edt_last_name = itemView.findViewById(R.id.edt_last_name);
        TextInputEditText edt_phone = itemView.findViewById(R.id.edt_phone_number);
        Button btn_continue = (Button) itemView.findViewById(R.id.btn_register);
        //Set data
        if (FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null && !TextUtils.isEmpty(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()))
            edt_phone.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        //setView
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edt_first_name.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Please enter first name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(edt_last_name.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Please enter last name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(edt_phone.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Please enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    DriverInfoModel model = new DriverInfoModel();
                    model.setFirstName(edt_first_name.getText().toString());
                    model.setLastName(edt_last_name.getText().toString());
                    model.setRating(0.0);
                    driverInfoRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(model)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(SplashScreenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SplashScreenActivity.this,"Register Successfully!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    goToHomeActivity(model);
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Toast.makeText(this, "[Error]:" + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
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


