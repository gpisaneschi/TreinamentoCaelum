package br.com.pisaneschi.cursolivro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import br.com.pisaneschi.cursolivro.R;
import br.com.pisaneschi.cursolivro.application.CasaDoCodigoApplication;
import br.com.pisaneschi.cursolivro.delegate.LivrosDelegate;
import br.com.pisaneschi.cursolivro.evento.LivroEvent;
import br.com.pisaneschi.cursolivro.fragment.DetalheLivroFragment;
import br.com.pisaneschi.cursolivro.fragment.ListaLivrosFragment;
import br.com.pisaneschi.cursolivro.modelo.Livro;
import br.com.pisaneschi.cursolivro.server.WebClient;


public class MainActivity extends AppCompatActivity implements LivrosDelegate {

    @Inject
    WebClient client;

    private ListaLivrosFragment listaLivrosFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((CasaDoCodigoApplication)getApplication()).getComponent().inject(this);

        if(savedInstanceState == null){

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            listaLivrosFragment = new ListaLivrosFragment();
            transaction.replace(R.id.frame_principal, listaLivrosFragment);
            transaction.commit();

            client.getLivros(0, 10);
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.vai_para_carrinho:
                Intent vaiParaCarrinho = new Intent(this,CarrinhoActivity.class);
                startActivity(vaiParaCarrinho);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void lidaComLivroSelecionado(Livro livro) {
        //Toast.makeText(this,"Livro selecionado: " + livro.getNome(), Toast.LENGTH_SHORT).show();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DetalheLivroFragment detalheLivroFragment = criaDetalhesCom(livro);
        transaction.replace(R.id.frame_principal, detalheLivroFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private DetalheLivroFragment criaDetalhesCom(Livro livro) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("livro", livro);
        DetalheLivroFragment detalhesLivroFragment = new DetalheLivroFragment();
        detalhesLivroFragment.setArguments(bundle);
        return detalhesLivroFragment;
    }

    @Subscribe
    public void lidaComSucesso(LivroEvent livroEvent) {
        listaLivrosFragment.populaListaCom(livroEvent.getLivros());
    }

    @Subscribe
    public void lidaComErro(Throwable erro) {
        Toast.makeText(this, "Não foi possivel carregar os livros...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

}
