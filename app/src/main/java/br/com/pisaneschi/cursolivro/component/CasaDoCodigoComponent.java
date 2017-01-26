package br.com.pisaneschi.cursolivro.component;

import javax.inject.Singleton;

import br.com.pisaneschi.cursolivro.activity.CarrinhoActivity;
import br.com.pisaneschi.cursolivro.activity.LoginActivity;
import br.com.pisaneschi.cursolivro.activity.MainActivity;
import br.com.pisaneschi.cursolivro.component.module.CasaDoCodigoModule;
import br.com.pisaneschi.cursolivro.fragment.DetalheLivroFragment;
import dagger.Component;

/**
 * Created by android6587 on 25/01/17.
 */

@Singleton
@Component(modules = CasaDoCodigoModule.class)
public interface CasaDoCodigoComponent {
    void inject(DetalheLivroFragment fragment);
    void inject(CarrinhoActivity activity);

    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);
}
