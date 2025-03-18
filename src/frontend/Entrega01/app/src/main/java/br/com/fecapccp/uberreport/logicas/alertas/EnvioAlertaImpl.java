package br.com.fecapccp.uberreport.logicas.alertas;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EnvioAlertaImpl implements EnvioAlerta {
    @Override
    public void enviarAlerta(String tipoAlerta, String dataHora) {
        // Lógica para enviar o alerta (ex: salvar no banco de dados ou enviar para o back-end)
        // Aqui apenas imprimimos no console para simplificação
        System.out.println("Alerta enviado: " + tipoAlerta + " em " + dataHora);
    }

    public String getDataHoraAtual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
