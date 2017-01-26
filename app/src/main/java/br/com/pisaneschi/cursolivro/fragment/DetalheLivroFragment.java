package br.com.pisaneschi.cursolivro.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import br.com.pisaneschi.cursolivro.R;
import br.com.pisaneschi.cursolivro.application.CasaDoCodigoApplication;
import br.com.pisaneschi.cursolivro.infra.Infra;
import br.com.pisaneschi.cursolivro.modelo.Autor;
import br.com.pisaneschi.cursolivro.modelo.Carrinho;
import br.com.pisaneschi.cursolivro.modelo.Item;
import br.com.pisaneschi.cursolivro.modelo.Livro;
import br.com.pisaneschi.cursolivro.modelo.TipoDeCompra;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android6587 on 23/01/17.
 */

public class DetalheLivroFragment extends Fragment {

    @BindView(R.id.detalhes_livro_autores)
    TextView autores;
    @BindView(R.id.detalhes_livro_comprar_ambos)
    Button botaoComprarAmbos;
    @BindView(R.id.detalhes_livro_comprar_ebook)
    Button botaoComprarEbook;
    @BindView(R.id.detalhes_livro_comprar_fisico)
    Button botaoComprarFisico;
    @BindView(R.id.detalhes_livro_data_publicacao)
    TextView dataPublicacao;
    @BindView(R.id.detalhes_livro_descricao)
    TextView descricao;
    @BindView(R.id.detalhes_livro_foto)
    ImageView foto;
    @BindView(R.id.detalhes_livro_isbn)
    TextView isbn;
    @BindView(R.id.detalhes_livro_nome)
    TextView nome;
    @BindView(R.id.detalhes_livro_num_paginas)
    TextView numPaginas;

    @Inject
    Carrinho carrinho;

    private Livro livro;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalhes_livro, container, false);

        ButterKnife.bind(this,view);

        Bundle arguments = getArguments();
        livro = (Livro) arguments.getSerializable("livro");
        populaCamposCom(livro);

        CasaDoCodigoApplication app = (CasaDoCodigoApplication) getActivity().getApplication();
        app.getComponent().inject(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Infra.colocaBotaoBack((AppCompatActivity) getActivity());
    }

    private void populaCamposCom(Livro livro) {
        nome.setText(livro.getNome());
        String listaDeAutores = ""; for(Autor autor : livro.getAutores()){
            if(!listaDeAutores.isEmpty()){
                listaDeAutores += ", ";
            }
            listaDeAutores += autor.getNome();
        }autores.setText(listaDeAutores);

        descricao.setText(livro.getDescricao());
        numPaginas.setText(String.valueOf(livro.getNumPaginas()));
        isbn.setText(livro.getISBN());
        dataPublicacao.setText(livro.getDataPublicacao());

        String textoComprarFisico = String.format("Comprar Livro Físico - R$ %.2f", livro.getValorFisico());
        botaoComprarFisico.setText(textoComprarFisico);

        String textoComprarEbook = String.format("Comprar Livro E-book - R$ %.2f", livro.getValorVirtual());
        botaoComprarEbook.setText(textoComprarEbook);

        String textoComprarAmbos = String.format("Comprar Livro Ambos - R$ %.2f", livro.getValorDoisJuntos());
        botaoComprarAmbos.setText(textoComprarAmbos);

        Picasso.with(getContext()).load(livro.getUrlFoto()).placeholder(R.drawable.livro).into(foto);
    }

    @OnClick(R.id.detalhes_livro_comprar_fisico)
    public void comprarFisico(){
        Toast.makeText(getActivity(), "Livro adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
        carrinho.adiciona(new Item(livro, TipoDeCompra.FISICO));
    }
    @OnClick(R.id.detalhes_livro_comprar_ebook)
    public void comprarEbook(){
        Toast.makeText(getActivity(), "Livro adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
        carrinho.adiciona(new Item(livro, TipoDeCompra.VIRTUAL));
    }
    @OnClick(R.id.detalhes_livro_comprar_ambos)
    public void comprarAmbos(){
        Toast.makeText(getActivity(), "Livro adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
        carrinho.adiciona(new Item(livro, TipoDeCompra.JUNTOS));
    }


}
