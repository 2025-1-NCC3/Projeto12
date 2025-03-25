// File: app/src/main/java/br/com/fecapccp/uberreport/telas/fluxoEntrada/PaginaHome.java
package br.com.fecapccp.uberreport;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import br.com.fecapccp.uberreport.alertas.AcidentesAlertaController;
import br.com.fecapccp.uberreport.alertas.CrimesAlertaController;
import br.com.fecapccp.uberreport.logicas.AnimacaoBotao;
import br.com.fecapccp.uberreport.alertas.ControladorAlerta;
import br.com.fecapccp.uberreport.alertas.ClimaAlertaController;
import br.com.fecapccp.uberreport.logicas.alertas.EnvioAlertaImpl;
import br.com.fecapccp.uberreport.logicas.alertas.model.Alerta;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class TelaProcurarCorridaPassageiro extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;
    private LinearLayout pesquisaCorridaContainer;
    private LinearLayout containerAlertas;
    private TextView corridaText;
    private ImageButton alertButton;
    private ImageButton botaoClimaAlerta;
    private ImageButton botaoAcidenteAlerta;
    private ImageButton botaoCrimeAlerta;
    private ImageButton backButton;
    private View inputDestino;
    private View botaoConfirmar;
    private View handleBotao;
    private TextView reportTexto;
    private RelativeLayout layoutAlertaReport;
    private Button botaoContinuar;
    private String tipoAlertaSelecionado;
    private String nomeAlertaSelecionado;
    private LinearLayout containerAlertasBotoes;
    private LinearLayout layoutAlertasClima;
    private LinearLayout layoutAlertasAcidentes;
    private LinearLayout layoutAlertasCrimes;

    private FusedLocationProviderClient fusedLocationClient;
    private double userLatitude;
    private double userLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_procurar_corrida_passageiro);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getUserLocation();
        }

        pesquisaCorridaContainer = findViewById(R.id.search_container);
        alertButton = findViewById(R.id.alert_button);
        backButton = findViewById(R.id.back_button);
        inputDestino = findViewById(R.id.input_destino);
        botaoConfirmar = findViewById(R.id.botao_confirmar);
        handleBotao = findViewById(R.id.imageView2);
        containerAlertas = findViewById(R.id.container_alertas);
        containerAlertasBotoes = findViewById(R.id.container_alertas_botoes);
        corridaText = findViewById(R.id.textView);
        reportTexto = findViewById(R.id.report_texto);
        layoutAlertaReport = findViewById(R.id.layoutAlertaReport);
        botaoClimaAlerta = findViewById(R.id.botao_clima);
        botaoAcidenteAlerta = findViewById(R.id.botao_acidentes);
        botaoCrimeAlerta = findViewById(R.id.botao_crimes);
        botaoContinuar = findViewById(R.id.botao_continuar);
        EnvioAlertaImpl envioAlerta = new EnvioAlertaImpl(this);

        layoutAlertasClima = findViewById(R.id.layout_alertas_clima);
        layoutAlertasAcidentes = findViewById(R.id.layout_alertas_acidentes);
        layoutAlertasCrimes = findViewById(R.id.layout_alertas_crimes);


        Button botaoPesquisaDestino = findViewById(R.id.botaoPesquisaDestino);
        botaoPesquisaDestino.setOnClickListener(v -> expandirPesquisaContainer());

        // Botão para voltar ao estado inicial
        backButton.setOnClickListener(v -> diminuirContainer());

        // Botão para expandir os alertas
        alertButton.setOnClickListener(v -> {
            AnimacaoBotao.animarBotao(v);

            expandeAlertas();
        });

        // Botão alertas CLIMA
        botaoClimaAlerta.setOnClickListener(v -> {
            ControladorAlerta climaAlertaController = new ClimaAlertaController(
                    containerAlertas,
                    containerAlertasBotoes,
                    layoutAlertasClima,
                    layoutAlertasAcidentes,
                    layoutAlertasCrimes
            );
            climaAlertaController.controlarAlertas();
        });

        // Botão alertas para ACIDENTES
        botaoAcidenteAlerta.setOnClickListener(v -> {
            ControladorAlerta acidentesAlertaController = new AcidentesAlertaController(
                    containerAlertas,
                    containerAlertasBotoes,
                    layoutAlertasClima,
                    layoutAlertasAcidentes,
                    layoutAlertasCrimes
            );
            acidentesAlertaController.controlarAlertas();
        });

        // Botão alertas para CRIMES
        botaoCrimeAlerta.setOnClickListener(v -> {
            ControladorAlerta crimesAlertaController = new CrimesAlertaController(
                    containerAlertas,
                    containerAlertasBotoes,
                    layoutAlertasClima,
                    layoutAlertasAcidentes,
                    layoutAlertasCrimes
            );
            crimesAlertaController.controlarAlertas();
        });

        // Inicializa o fragmento do mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);

        // Inicializar Places API
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "${MAPS_API_KEY}");
        }
        // Criar um cliente do places API
        PlacesClient placesClient = Places.createClient(this);

        configurarAutoComplete();

        // Logica de seleção de alertas, (TODO: Mudar para outra classe)
        ImageButton botaoAlagamento = findViewById(R.id.botao_alagamento);
        ImageButton botaoDeslizamento = findViewById(R.id.botao_deslizamento);
        ImageButton botaoTemporal = findViewById(R.id.botao_temporal);
        ImageButton botaoAcidenteCarro = findViewById(R.id.botao_acidente_carro);
        ImageButton botaoAcidentePedestre = findViewById(R.id.botao_acidente_pedestre);
        ImageButton botaoCrimeAssaltos = findViewById(R.id.botao_assaltos);
        ImageButton botaoCrimeTiroteio = findViewById(R.id.botao_tiroteio);
        ImageButton botaoCrimeArrastao = findViewById(R.id.botao_arrastao);
        Button botaoContinuar = findViewById(R.id.botao_continuar);
        Button botaoCancelar = findViewById(R.id.botao_cancelar);

        ImageButton[] botoes = {
                botaoAlagamento,
                botaoDeslizamento,
                botaoTemporal,
                botaoAcidenteCarro,
                botaoAcidentePedestre,
                botaoCrimeAssaltos,
                botaoCrimeTiroteio,
                botaoCrimeArrastao
        };

        View.OnClickListener botaoClickListener = v -> {
            for (ImageButton botao : botoes) {
                if (botao == v) {
                    botao.setBackgroundResource(R.drawable.botao_selecionado);
                    botaoContinuar.setEnabled(true);
                    tipoAlertaSelecionado = (String) botao.getContentDescription();
                    nomeAlertaSelecionado = getResources().getResourceEntryName(botao.getId());
                } else {
                    botao.setBackgroundResource(R.drawable.circular_button);
                }
            }
        };

        botaoAlagamento.setOnClickListener(botaoClickListener);
        botaoDeslizamento.setOnClickListener(botaoClickListener);
        botaoTemporal.setOnClickListener(botaoClickListener);
        botaoAcidenteCarro.setOnClickListener(botaoClickListener);
        botaoAcidentePedestre.setOnClickListener(botaoClickListener);
        botaoCrimeAssaltos.setOnClickListener(botaoClickListener);
        botaoCrimeTiroteio.setOnClickListener(botaoClickListener);
        botaoCrimeArrastao.setOnClickListener(botaoClickListener);

        // Botão para voltar ao estado inicial
        backButton.setOnClickListener(v -> {
            diminuirContainer();
            for (ImageButton botao : botoes) {
                botao.setBackgroundResource(R.drawable.circular_button);
            }
            botaoContinuar.setEnabled(false);
        });

        // Botão cancelar para voltar ao estado inicial
        botaoCancelar.setOnClickListener(v -> {
            for (ImageButton botao : botoes) {
                botao.setBackgroundResource(R.drawable.circular_button);
            }
            botaoContinuar.setEnabled(false);
        });

        botaoContinuar.setOnClickListener(v -> {
            String dataHoraAtual = envioAlerta.getDataHoraAtual();

            Alerta alerta = new Alerta(
                    nomeAlertaSelecionado,
                    tipoAlertaSelecionado,
                    dataHoraAtual,
                    userLatitude,
                    userLongitude,
                    1
            );
            envioAlerta.enviarAlerta(alerta);
            exibirPopUp();
        });

    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        userLatitude = location.getLatitude();
                        userLongitude = location.getLongitude();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            }
        }
    }

    private void configurarAutoComplete() {
        EditText edtDestino = findViewById(R.id.input_destino); // ID do campo de destino

        edtDestino.setOnClickListener(v -> {
            // Criar o intent para abrir a caixa de busca
            List<Place.Field> campos = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, campos)
                    .build(TelaProcurarCorridaPassageiro.this);

            startActivityForResult(intent, 100); // Código de requisição 100
        });
    }

    private void expandeAlertas() {
        int startHeight = pesquisaCorridaContainer.getHeight();
        int endHeight = getResources().getDisplayMetrics().heightPixels / 3;

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams params = pesquisaCorridaContainer.getLayoutParams();
            params.height = (int) animation.getAnimatedValue();
            pesquisaCorridaContainer.setLayoutParams(params);
        });

        animator.setDuration(300);
        animator.start();

        // Adiciona elementos para o container
        backButton.setVisibility(View.VISIBLE);
        containerAlertas.setVisibility(View.VISIBLE);
        reportTexto.setVisibility(View.VISIBLE);

        // Esconde o botaoPesquisaDestino
        Button botaoPesquisaDestino = findViewById(R.id.botaoPesquisaDestino);
        botaoPesquisaDestino.setVisibility(View.GONE);

        // Esconde os elementos adicionais
        corridaText.setVisibility(View.GONE);
        inputDestino.setVisibility(View.GONE);
        botaoConfirmar.setVisibility(View.GONE);
        handleBotao.setVisibility(View.GONE);
        alertButton.setVisibility(View.GONE);
    }

    private void expandirPesquisaContainer() {
        int startHeight = pesquisaCorridaContainer.getHeight();
        int endHeight = getResources().getDisplayMetrics().heightPixels * 3 / 4;

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams params = pesquisaCorridaContainer.getLayoutParams();
            params.height = (int) animation.getAnimatedValue();
            pesquisaCorridaContainer.setLayoutParams(params);
        });

        animator.setDuration(300);
        animator.start();

        alertButton.animate().translationY(-endHeight / 2).setDuration(300).start();
        backButton.setVisibility(View.VISIBLE);

        // Esconde o botaoPesquisaDestino
        Button botaoPesquisaDestino = findViewById(R.id.botaoPesquisaDestino);
        botaoPesquisaDestino.setVisibility(View.GONE);

        pesquisaCorridaContainer.postDelayed(() -> {
            inputDestino.setVisibility(View.VISIBLE);
            botaoConfirmar.setVisibility(View.VISIBLE);
        }, 300); // Tempo para garantir que a animação terminou antes de exibir os componentes
    }

    private void exibirPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enviado com sucesso")
                .setPositiveButton("OK", (dialog, id) -> {
                    // Voltar ao estado inicial
                    diminuirContainer();
                });
        builder.create().show();
    }

    private void diminuirContainer() {
        int startHeight = pesquisaCorridaContainer.getHeight();
        int endHeight = getResources().getDimensionPixelSize(R.dimen.original_height);

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams params = pesquisaCorridaContainer.getLayoutParams();
            params.height = (int) animation.getAnimatedValue();
            pesquisaCorridaContainer.setLayoutParams(params);
        });

        animator.setDuration(300);
        animator.start();

        alertButton.animate().translationY(0).setDuration(300).start();
        backButton.setVisibility(View.GONE);

        // Torna o botaoPesquisaDestino visível novamente
        Button botaoPesquisaDestino = findViewById(R.id.botaoPesquisaDestino);
        botaoPesquisaDestino.setVisibility(View.VISIBLE);

        // Exibe os componentes originais
        corridaText.setVisibility(View.VISIBLE);
        alertButton.setVisibility(View.VISIBLE);

        // Esconde os elementos adicionais
        inputDestino.setVisibility(View.GONE);
        botaoConfirmar.setVisibility(View.GONE);
        handleBotao.setVisibility(View.GONE);
        containerAlertas.setVisibility(View.GONE);
        reportTexto.setVisibility(View.GONE);
        containerAlertasBotoes.setVisibility(View.GONE);
    }

    private void atualizarMapa(LatLng latLng) {
        if (latLng != null && gMap != null) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                EditText edtDestino = findViewById(R.id.input_destino);
                edtDestino.setText(place.getName()); // Exibir o endereço no campo

                // Atualizar o mapa para a nova posição
                atualizarMapa(place.getLatLng());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("ERRO_AUTOCOMPLETE", status.getStatusMessage());
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        // Define a localização do mapa
        LatLng liberdade = new LatLng(-23.563133, -46.635048);

        // Adiciona um marcador no mapa e move a camera para a localização
        gMap.addMarker(new MarkerOptions().position(liberdade).title("Liberdade, São Paulo"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(liberdade, 15));
    }
}