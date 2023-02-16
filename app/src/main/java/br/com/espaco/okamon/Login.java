package br.com.espaco.okamon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private TextView tela_cadastro;
    private Button botao_entrar;
    private EditText email_cadastro, senha_cadastro;
    private ProgressBar progressBar;
    String[] mensagens = {"É necessário preencher todos os campos obrigatórios",
            "Login efetuado com sucesso."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciarComponentes();

        botao_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_cadastro.getText().toString();
                String senha = senha_cadastro.getText().toString();

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(Login.this, mensagens[0], Toast.LENGTH_LONG).show();
                } else {
                    logarUsuario(email, senha);
                }
            }
        });

        tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
            }
        });
    }

    private void logarUsuario(String email, String senha) {

        String email1 = email_cadastro.getText().toString();
        String senha1 = senha_cadastro.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email1, senha1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            telaPrincial();
                        }
                    }, 2000);
                } else {
                    String erro;
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (Exception e) {
                        erro = "Erro ao logar o usuario";
                    }
                    Toast.makeText(Login.this, erro, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void telaPrincial() {
        Intent intent = new Intent(Login.this, Perfil.class);
        startActivity(intent);
        finish();
    }

    private void iniciarComponentes() {
        email_cadastro = findViewById(R.id.edit_email);
        senha_cadastro = findViewById(R.id.edit_senha);
        botao_entrar = findViewById(R.id.botao_entrar);
        tela_cadastro = findViewById(R.id.texto_cadastro);
        progressBar = findViewById(R.id.progress_bar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            telaPrincial();
        }
    }
}