package br.com.fecapccp.uberreport.logicas;

import android.view.View;

public class AnimacaoBotao {
    public static void animarBotao(View view) {
        view.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(150)
                .withEndAction(() -> view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start())
                .start();
    }
}
