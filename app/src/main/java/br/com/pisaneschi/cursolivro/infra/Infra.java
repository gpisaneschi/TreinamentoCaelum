package br.com.pisaneschi.cursolivro.infra;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by android6587 on 24/01/17.
 */

public class Infra {

    public static void colocaBotaoBack(AppCompatActivity activity){
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public static void retiraBotaoBack(AppCompatActivity activity){
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}


