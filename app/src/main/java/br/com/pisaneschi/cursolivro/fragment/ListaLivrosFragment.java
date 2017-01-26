package br.com.pisaneschi.cursolivro.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


import br.com.pisaneschi.cursolivro.R;
import br.com.pisaneschi.cursolivro.activity.LoginActivity;
import br.com.pisaneschi.cursolivro.adapter.LivroAdapter;
import br.com.pisaneschi.cursolivro.endlessList.EndlessListListener;
import br.com.pisaneschi.cursolivro.infra.Infra;
import br.com.pisaneschi.cursolivro.modelo.Livro;
import br.com.pisaneschi.cursolivro.server.WebClient;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android6587 on 23/01/17.
 */

public class ListaLivrosFragment extends Fragment {

    @Inject
    WebClient client;

    @BindView(R.id.lista_livros)RecyclerView recyclerView;
    @BindView(R.id.viewCarregando)View include;

    private ArrayList<Livro> livros = new ArrayList<>();
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    public void onResume() {
        super.onResume();
        removeInclude();
        carregaLista();
        Infra.retiraBotaoBack((AppCompatActivity) getActivity());
    }

    private void carregaLista(){
        if (mFirebaseRemoteConfig.getBoolean("Portugues")) {
            Toast.makeText(getContext(), "Portugues", Toast.LENGTH_SHORT).show();
        }else if((mFirebaseRemoteConfig.getBoolean("Ingles"))) {
            Toast.makeText(getContext(), "Ingles", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "Outro", Toast.LENGTH_SHORT).show();
        }

    }

    private void removeInclude() {
        if (!livros.isEmpty()){
            include.setVisibility(View.GONE);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("lista", livros);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.getSerializable("lista") != null){
            livros = (ArrayList<Livro>)savedInstanceState.getSerializable("lista");
        }
        criaEConfiguraRemoteConfig();

    }

    public void criaEConfiguraRemoteConfig(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.preferencia);
        mFirebaseRemoteConfig.fetch(20).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(getContext(), "Falha", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_livros,container,false);
        ButterKnife.bind(this,view);

        //RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.lista_livros);
        recyclerView.setAdapter(new LivroAdapter(livros));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addOnScrollListener(new EndlessListListener() {
                @Override
                public void carregaMaisItens() {
                 client.getLivros(livros.size(), 10);
            }
        });

        return view;
    }

    public void populaListaCom(List<Livro> livros){
        //this.livros.clear();
        this.livros.addAll(livros);
        recyclerView.getAdapter().notifyDataSetChanged();
        removeInclude();

    }
}
