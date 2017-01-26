package br.com.pisaneschi.cursolivro.converter;


import java.io.IOException;
import java.util.List;

import br.com.pisaneschi.cursolivro.modelo.Livro;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class LivroServiceConverter implements Converter<ResponseBody, List<Livro>> {

    @Override
    public List<Livro> convert(ResponseBody value) throws IOException {

        String json = value.string();
        LivroConverter livroConverter = new LivroConverter();

        List<Livro> livros = livroConverter.fromJson(json);

        return livros;
    }
}
