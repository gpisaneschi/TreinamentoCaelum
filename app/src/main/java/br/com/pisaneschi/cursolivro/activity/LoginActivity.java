package br.com.pisaneschi.cursolivro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import br.com.pisaneschi.cursolivro.FCM.FCMRegister;
import br.com.pisaneschi.cursolivro.R;
import br.com.pisaneschi.cursolivro.application.CasaDoCodigoApplication;
import br.com.pisaneschi.cursolivro.component.CasaDoCodigoComponent;
import br.com.pisaneschi.cursolivro.event.SingInEvent;
import br.com.pisaneschi.cursolivro.server.WebClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android6587 on 25/01/17.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_email)
    TextView login;
    @BindView(R.id.login_senha)
    TextView senha;

    private WebClient client;

    @Inject
    public void setWebClient(WebClient client){
        this.client = client;
    }

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean flagLogado = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        CasaDoCodigoApplication application = (CasaDoCodigoApplication) getApplication();
        CasaDoCodigoComponent component = application.getComponent();
        component.inject(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && !flagLogado) {
                    // User is signed in
                    String id = new FCMRegister().registra();
                    client.enviaDadosFirebaseParaServidor(user.getEmail(),id);

                    EventBus.getDefault().post(new SingInEvent());
                } else {
                    // User is signed out

                }
                // ...
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

    }


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Subscribe
    public void vaiParaMain(SingInEvent event){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.login_novo)
    public void cadastrar(){
        String email = this.login.getText().toString().trim();
        String senha = this.senha.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @OnClick(R.id.login_logar)
    public void login(){
        String email = this.login.getText().toString().trim();
        String senha = this.senha.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Usuario não cadastrado.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
