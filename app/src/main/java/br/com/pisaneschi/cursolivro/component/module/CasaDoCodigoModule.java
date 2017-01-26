package br.com.pisaneschi.cursolivro.component.module;

import javax.inject.Singleton;

import br.com.pisaneschi.cursolivro.converter.LivroServiceConverterFactory;
import br.com.pisaneschi.cursolivro.modelo.Carrinho;
import br.com.pisaneschi.cursolivro.server.DeviceService;
import br.com.pisaneschi.cursolivro.server.LivrosService;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by android6587 on 25/01/17.
 */

@Module
public class CasaDoCodigoModule {

    @Provides
    @Singleton
    public Carrinho getCarrinho(){
        return new Carrinho();
    }

    @Singleton
    @Provides
    public Retrofit getRetrofit(){
        return new Retrofit.Builder().baseUrl("http://cdcmob.herokuapp.com/")
                .addConverterFactory(new LivroServiceConverterFactory())
                .build();
    }

    @Provides
    @Singleton
    public LivrosService getLivroService(Retrofit retrofit){
        return retrofit.create(LivrosService.class);
    }

    @Provides
    @Singleton
    public DeviceService getDeviceService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.141.199:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DeviceService service = retrofit.create(DeviceService.class);
        return  service;

    }

}
