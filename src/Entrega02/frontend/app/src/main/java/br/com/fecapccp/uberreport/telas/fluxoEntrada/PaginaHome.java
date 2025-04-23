// File: app/src/main/java/br/com/fecapccp/uberreport/telas/fluxoEntrada/PaginaHome.java
package br.com.fecapccp.uberreport.telas.fluxoEntrada;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
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

import br.com.fecapccp.uberreport.R;
import br.com.fecapccp.uberreport.logicas.AnimacaoBotao;
import br.com.fecapccp.uberreport.alertas.ControladorAlerta;
import br.com.fecapccp.uberreport.alertas.ClimaAlertaController;

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

public class PaginaHome extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;
    private LinearLayout pesquisaCorridaContainer;
    private LinearLayout containerAlertas;
    private TextView corridaText;
    private ImageButton alertButton;
    private ImageButton botaoClimaAlerta;
    private ImageButton backButton;
    private View inputDestino;
    private View botaoConfirmar;
    private View handleBotao;
    private TextView reportTexto;
    private RelativeLayout layoutAlertaReport;
    private LinearLayout containerAlertasClima;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_home_main);

        pesquisaCorridaContainer = findViewById(R.id.search_container);
        alertButton = findViewById(R.id.alert_button);
        backButton = findViewById(R.id.back_button);
        inputDestino = findViewById(R.id.input_destino);
        botaoConfirmar = findViewById(R.id.botao_confirmar);
        handleBotao = findViewById(R.id.imageView2);
        containerAlertas = findViewById(R.id.container_alertas);
        containerAlertasClima = findViewById(R.id.container_alertas_clima);
        corridaText = findViewById(R.id.textView);
        reportTexto = findViewById(R.id.report_texto);
        layoutAlertaReport = findViewById(R.id.layoutAlertaReport);
        botaoClimaAlerta = findViewById(R.id.botao_clima);

        Button botaoPesquisaDestino = findViewById(R.id.botaoPesquisaDestino);
        botaoPesquisaDestino.setOnClickListener(v -> expandirPesquisaContainer());

        // Botão para voltar ao estado inicial
        backButton.setOnClickListener(v -> diminuirContainer());

        // Botão para expandir os alertas
        alertButton.setOnClickListener(v -> {
            AnimacaoBotao.animarBotao(v);

            expandeAlertas();
        });

        botaoClimaAlerta.setOnClickListener(v -> {
            ControladorAlerta climaAlertaController = new ClimaAlertaController(
                    containerAlertas, containerAlertasClima);
            climaAlertaController.controlarAlertas();
        });

        // Inicializa o fragmento do mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);

        // Inicializar Places API
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "${MAPS_API_KEY}");
        }
        // Criar um cliente do Places API
        PlacesClient placesClient = Places.createClient(this);

        configurarAutoComplete();
    }

    private void configurarAutoComplete() {
        EditText edtDestino = findViewById(R.id.input_destino); // ID do campo de destino

        edtDestino.setOnClickListener(v -> {
            // Criar o intent para abrir a caixa de busca
            List<Place.Field> campos = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, campos)
                    .build(PaginaHome.this);

            startActivityForResult(intent, 100); // Código de requisição 100
        });
    }

    private void expandeAlertas() {
        int startHeight = pesquisaCorridaContainer.getHeight();
        int endHeight = getResources().getDisplayMetrics().heightPixels / 4;

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
        containerAlertasClima.setVisibility(View.GONE);
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