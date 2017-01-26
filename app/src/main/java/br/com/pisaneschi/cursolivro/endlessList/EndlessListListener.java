package br.com.pisaneschi.cursolivro.endlessList;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by android6587 on 24/01/17.
 */

public abstract class EndlessListListener extends RecyclerView.OnScrollListener {

    private int quantidadeTotalItens;
    private int primeiroItemVisivel;
    private int quantidadeItensVisiveis;
    boolean carregando = true;
    private int totalAnterior = 0;

    public abstract void carregaMaisItens();

    @Override
    public void onScrolled(RecyclerView recyclerView, int qtdScrollHorizontal, int qtdScrollVertical) {
        super.onScrolled(recyclerView, qtdScrollHorizontal, qtdScrollVertical);

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        quantidadeTotalItens = layoutManager.getItemCount();
        primeiroItemVisivel = layoutManager.findFirstVisibleItemPosition();
        quantidadeItensVisiveis = recyclerView.getChildCount();

        int indiceLimiteParaCarregar = quantidadeTotalItens - quantidadeItensVisiveis - 5;
        if (carregando){
            if (quantidadeTotalItens > totalAnterior){
                totalAnterior = quantidadeTotalItens;
                carregando = false;
            }
        }
        if(!carregando && primeiroItemVisivel >= indiceLimiteParaCarregar){
            carregaMaisItens();
            carregando = true;
        }
    }

}
