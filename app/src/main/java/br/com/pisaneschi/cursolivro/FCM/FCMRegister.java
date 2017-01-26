package br.com.pisaneschi.cursolivro.FCM;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by android6587 on 26/01/17.
 */

public class FCMRegister {
    public String registra(){
        FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
        String token = instanceId.getToken();
        return token;
    }
}
