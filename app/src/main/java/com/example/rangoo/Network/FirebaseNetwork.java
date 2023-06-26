package com.example.rangoo.Network;

import androidx.annotation.NonNull;

import com.example.rangoo.Interfaces.AuthCallback;
import com.example.rangoo.Model.LoginData;
import com.example.rangoo.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseNetwork {

    private FirebaseAuth auth;
    private DatabaseReference database;

    /***
     * Inicializa a instância do FirebaseAuth.
     */
    private void initAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
    }

    /***
     * Inicializa a instância do DatabaseReference com o caminho especificado.
     *
     * @param data O caminho para o nó no banco de dados do Firebase.
     */
    private void initDatabase(String data) {
        if (database == null) {
            database = FirebaseDatabase.getInstance().getReference().child(data);
        }
    }

    /***
     * Registra um novo usuário no Firebase Authentication e salva os dados do usuário no banco de dados.
     *
     * @param user     O objeto User contendo os dados do usuário.
     * @param callback O AuthCallback para retornar o resultado da operação.
     */
    public void signUpUser(User user, AuthCallback callback) {
        if (user != null) {
            initAuth();

            auth.createUserWithEmailAndPassword(user.getLoginData().getEmail(), user.getLoginData().getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                saveDataUser(auth.getCurrentUser().getUid(), user, callback);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.onError(e.getLocalizedMessage());
                        }
                    });
        }
    }

    /***
     * Salva os dados do usuário no banco de dados do Firebase.
     *
     * @param UID      O UID do usuário atualmente autenticado.
     * @param user     O objeto User contendo os dados do usuário.
     * @param callback O AuthCallback para retornar o resultado da operação.
     */
    public void saveDataUser(String UID, User user, AuthCallback callback) {
        initDatabase("users");
        if (database != null) {
            database.child(UID).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            callback.onSuccess(UID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.onError(e.getLocalizedMessage());
                        }
                    });
        }
    }

    /***
     * Realiza o login do usuário usando o Firebase Authentication.
     *
     * @param loginData O objeto LoginData contendo as credenciais de login do usuário.
     * @param callback  O AuthCallback para retornar o resultado da operação.
     */
    public void signIn(LoginData loginData, AuthCallback callback) {
        initAuth();
        auth.signInWithEmailAndPassword(loginData.getEmail(), loginData.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(auth.getCurrentUser().getUid());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError(e.getLocalizedMessage());
                    }
                });
    }

    /***
     * Realiza o logout do usuário.
     */
    public void signOut() {
        initAuth();
        auth.signOut();
    }
}