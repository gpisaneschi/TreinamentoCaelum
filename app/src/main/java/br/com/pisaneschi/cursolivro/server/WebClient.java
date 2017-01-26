package br.com.pisaneschi.cursolivro.server;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import br.com.pisaneschi.cursolivro.evento.LivroEvent;
import br.com.pisaneschi.cursolivro.modelo.Livro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android6587 on 24/01/17.
 */

public class WebClient {

    /*private final LivrosDelegate delegate;

    public WebClient(LivrosDelegate delegate){
        this.delegate = delegate;
    }*/

    LivrosService service;
    DeviceService deviceServer ;

    @Inject
    public WebClient(LivrosService service, DeviceService deviceServer){

        this.service = service;
        this.deviceServer = deviceServer;
    }



    public void getLivros(int indicePrimeiroLivro, int qtdLivros){
        //Retrofit client = new Retrofit.Builder().baseUrl(SERVER_URL)
        //                                        .addConverterFactory(new LivroServiceConverterFactory())
        //                                        .build();

        //LivrosService service = client.create(LivrosService.class);


        Call<List<Livro>> call = service.listaLivros(indicePrimeiroLivro, qtdLivros);
        call.enqueue(new Callback<List<Livro>>() {
            @Override
            public void onResponse(Call<List<Livro>> call, Response<List<Livro>> response) {
                //delegate.lidaComSucesso(response.body());
                EventBus.getDefault().post(new LivroEvent(response.body()));
            }

            @Override
            public void onFailure(Call<List<Livro>> call, Throwable t) {
                //delegate.lidaComErro(t);
                EventBus.getDefault().post(t);
            }
        });
    }

    public void enviaDadosFirebaseParaServidor(String email, String id) {
        Call<String> call = deviceServer.mandaTokenParaServer(email,id);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("CasaDoCodigo", "code : " + response.code());
                Log.i("CasaDoCodigo", "" + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("CasaDoCodigo", t.getMessage());
            }
        });
    }
}
