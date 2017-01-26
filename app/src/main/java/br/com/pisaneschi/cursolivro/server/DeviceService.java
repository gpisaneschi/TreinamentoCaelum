package br.com.pisaneschi.cursolivro.server;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by android6587 on 26/01/17.
 */

public interface DeviceService {
    @GET("/device/register/{email}/{id}")
    Call<String> mandaTokenParaServer(@Path("email") String email, @Path("id") String id);
}
