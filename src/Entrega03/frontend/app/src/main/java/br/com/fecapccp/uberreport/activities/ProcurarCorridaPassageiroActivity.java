package br.com.fecapccp.uberreport.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import br.com.fecapccp.uberreport.BuildConfig;
import br.com.fecapccp.uberreport.R;
import br.com.fecapccp.uberreport.activities.perfil.InformacoesPassageiroActivity;
import br.com.fecapccp.uberreport.models.Usuario;
import br.com.fecapccp.uberreport.services.alertas.controller.AcidentesAlertaController;
import br.com.fecapccp.uberreport.services.alertas.controller.CrimesAlertaController;
import br.com.fecapccp.uberreport.services.alertas.marcador.AlertasManager;
import br.com.fecapccp.uberreport.services.animacoes.AnimacaoBotao;
import br.com.fecapccp.uberreport.services.alertas.ControladorAlerta;
import br.com.fecapccp.uberreport.services.alertas.controller.ClimaAlertaController;
import br.com.fecapccp.uberreport.services.alertas.EnvioAlertaImpl;
import br.com.fecapccp.uberreport.services.alertas.model.Alerta;
import br.com.fecapccp.uberreport.services.usuario.ObterUsuarioImpl;
import br.com.fecapccp.uberreport.utils.SharedPreferencesManager;

import android.animation.ValueAnimator;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ProcurarCorridaPassageiroActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Variáveis de instância
    private GoogleMap gMap;
    private LinearLayout pesquisaCorridaContainer;
    private LinearLayout containerAlertas;
    private TextView corridaText;
    private ImageButton centralizar;
    private ImageButton alertButton;
    private ImageButton botaoClimaAlerta;
    private ImageButton botaoAcidenteAlerta;
    private ImageButton botaoCrimeAlerta;
    private ImageButton backButton;
    private View inputDestino;
    private View inputLocalAtual;
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
    private Polyline rotaAtual;
    private ImageButton btnPerfilUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurar_corrida_passageiro);

        // TODO: Inicializa o client de localização
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // TODO: Inicializa os componentes da UI
        inicializaUiComponentes();
        configuraBotoesAlertas();
        inicializaMaps();
        inicializaPlacesApiMaps();
        configurarBotaoConfirmar();
        configurarBotaoCentralizar();
        getPerfilUsuario();
    }

    private void conferePermissaoLocalizacaoUsuario() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getUserLocation();
        }
    }

    private void inicializaUiComponentes() {
        pesquisaCorridaContainer = findViewById(R.id.search_container);
        alertButton = findViewById(R.id.alert_button);
        centralizar = findViewById(R.id.centralizar);
        backButton = findViewById(R.id.back_button);
        inputDestino = findViewById(R.id.input_destino);
        inputLocalAtual = findViewById(R.id.input_local_atual);
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
        layoutAlertasClima = findViewById(R.id.layout_alertas_clima);
        layoutAlertasAcidentes = findViewById(R.id.layout_alertas_acidentes);
        layoutAlertasCrimes = findViewById(R.id.layout_alertas_crimes);
        btnPerfilUsuario = findViewById(R.id.btnPerfilUsuario);
    }

    private void configuraBotoesAlertas() {
        Button botaoPesquisaDestino = findViewById(R.id.botaoPesquisaDestino);
        botaoPesquisaDestino.setOnClickListener(v -> expandirPesquisaContainer());

        backButton.setOnClickListener(v -> diminuirContainer());

        alertButton.setOnClickListener(v -> {
            AnimacaoBotao.animarBotao(v);
            expandeAlertas();
        });

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

        configureAlertButtons();
    }

    private void configureAlertButtons() {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);

        ImageButton[] botoes = {
                findViewById(R.id.botao_alagamento),
                findViewById(R.id.botao_deslizamento),
                findViewById(R.id.botao_temporal),
                findViewById(R.id.botao_acidente_carro),
                findViewById(R.id.botao_acidente_pedestre),
                findViewById(R.id.botao_assaltos),
                findViewById(R.id.botao_tiroteio),
                findViewById(R.id.botao_arrastao)
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

        for (ImageButton botao : botoes) {
            botao.setOnClickListener(botaoClickListener);
        }

        Button botaoCancelar = findViewById(R.id.botao_cancelar);
        botaoCancelar.setOnClickListener(v -> {
            for (ImageButton botao : botoes) {
                botao.setBackgroundResource(R.drawable.circular_button);
            }
            botaoContinuar.setEnabled(false);
            diminuirContainer();
        });

        botaoContinuar.setOnClickListener(v -> {
            String dataHoraAtual = new EnvioAlertaImpl(this).getDataHoraAtual();

            int idUser = sharedPreferencesManager.obterIdUsuario();

            Alerta alerta = new Alerta(
                    nomeAlertaSelecionado,
                    tipoAlertaSelecionado,
                    dataHoraAtual,
                    userLatitude,
                    userLongitude,
                    idUser
            );
            new EnvioAlertaImpl(this).enviarAlerta(alerta);
            exibirPopUp();
        });
    }

    private void inicializaMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);
    }

    private void inicializaPlacesApiMaps() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        }
        PlacesClient placesClient = Places.createClient(this);
        configurarAutoComplete();
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            userLatitude = location.getLatitude();
                            userLongitude = location.getLongitude();
                            atualizarMapaComLocalizacaoUsuario(); // Atualiza o mapa aqui
                            preencherEditTextComEndereco(location); // Altera o EditText com localização atual
                        } else {
                            // Lidar com localização nula
                            Log.e("Localização", "Localização nula");
                            atualizarMapaComLocalizacaoPadrao();
                        }
                    });
        } else {
            // Lidar com o caso em que a permissão não foi concedida
            Log.e("Permissão", "Permissão de localização não concedida");
            atualizarMapaComLocalizacaoPadrao();
        }
    }

    private void preencherEditTextComEndereco(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String enderecoCompleto = address.getAddressLine(0);
                ((EditText) inputLocalAtual).setText(enderecoCompleto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                atualizarMapaComLocalizacaoPadrao();
            }
        }
    }

    private void configurarAutoComplete() {
        EditText edtDestino = findViewById(R.id.input_destino);

        edtDestino.setOnClickListener(v -> {
            List<Place.Field> campos = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, campos)
                    .build(ProcurarCorridaPassageiroActivity.this);

            startActivityForResult(intent, 100);
        });
    }

    private void expandeAlertas() {
        int startHeight = pesquisaCorridaContainer.getHeight();
        int endHeight = getResources().getDisplayMetrics().heightPixels / 2;

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams params = pesquisaCorridaContainer.getLayoutParams();
            params.height = (int) animation.getAnimatedValue();
            pesquisaCorridaContainer.setLayoutParams(params);
        });

        animator.setDuration(300);
        animator.start();

        backButton.setVisibility(View.VISIBLE);
        containerAlertas.setVisibility(View.VISIBLE);
        reportTexto.setVisibility(View.VISIBLE);

        Button botaoPesquisaDestino = findViewById(R.id.botaoPesquisaDestino);
        botaoPesquisaDestino.setVisibility(View.GONE);

        corridaText.setVisibility(View.GONE);
        inputDestino.setVisibility(View.GONE);
        inputLocalAtual.setVisibility(View.GONE);
        botaoConfirmar.setVisibility(View.GONE);
        handleBotao.setVisibility(View.GONE);
        alertButton.setVisibility(View.GONE);
        centralizar.setVisibility(View.GONE);
    }

    private void expandirPesquisaContainer() {
        int startHeight = pesquisaCorridaContainer.getHeight();
        int endHeight = getResources().getDisplayMetrics().heightPixels / 2;

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams params = pesquisaCorridaContainer.getLayoutParams();
            params.height = (int) animation.getAnimatedValue();
            pesquisaCorridaContainer.setLayoutParams(params);
        });

        animator.setDuration(300);
        animator.start();

        alertButton.animate().translationY(-endHeight / 2).setDuration(300).start();
        centralizar.animate().translationY((float) (-endHeight / 2.17)).setDuration(300).start();
        backButton.setVisibility(View.VISIBLE);

        Button botaoPesquisaDestino = findViewById(R.id.botaoPesquisaDestino);
        botaoPesquisaDestino.setVisibility(View.GONE);

        pesquisaCorridaContainer.postDelayed(() -> {
            inputDestino.setVisibility(View.VISIBLE);
            inputLocalAtual.setVisibility(View.VISIBLE);
            // handleBotao.setVisibility(View.VISIBLE);
            botaoConfirmar.setVisibility(View.VISIBLE);
        }, 300);
    }

    private void exibirPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enviado com sucesso")
                .setPositiveButton("OK", (dialog, id) -> diminuirContainer());
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
        centralizar.animate().translationY(0).setDuration(300).start();
        backButton.setVisibility(View.GONE);

        Button botaoPesquisaDestino = findViewById(R.id.botaoPesquisaDestino);
        botaoPesquisaDestino.setVisibility(View.VISIBLE);

        corridaText.setVisibility(View.VISIBLE);
        alertButton.setVisibility(View.VISIBLE);
        centralizar.setVisibility(View.VISIBLE);

        inputDestino.setVisibility(View.GONE);
        inputLocalAtual.setVisibility(View.GONE);
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
                edtDestino.setText(place.getName());

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
        conferePermissaoLocalizacaoUsuario();

        AlertasManager alertasManager = new AlertasManager(gMap, this, marker -> {
            Alerta alerta = (Alerta) marker.getTag();
            if (alerta != null) {
                exibirDetalhesAlerta(alerta);
            }
        });

        gMap.setOnCameraIdleListener(() -> {
            LatLng cameraPosition = gMap.getCameraPosition().target;
            alertasManager.fetchAndDisplayAlertas(cameraPosition.latitude, cameraPosition.longitude, 3000);
        });
    }

    private void exibirDetalhesAlerta(Alerta alerta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getTituloPersonalizado(alerta.getNomeAlerta()));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        TextView detalhesTexto = new TextView(this);
        detalhesTexto.setText("🚨 Tipo do alerta: " + alerta.getTipoAlerta() + "\n" +
                "🗓️ Data e hora: " + alerta.getDataHoraAlerta() + "\n" +
                "↔️ Latitude: " + alerta.getLatitude() + "\n" +
                "↕️ Longitude: " + alerta.getLongitude());
        detalhesTexto.setTextSize(16);
        layout.addView(detalhesTexto);

        final TextView contadorTexto = new TextView(this);
        contadorTexto.setTextSize(16);
        layout.addView(contadorTexto);

        builder.setView(layout);
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        // Recupera o tempo final com base na chave única
        String alertaKey = gerarChaveUnica(alerta.getLatitude(), alerta.getLongitude());
        SharedPreferences prefs = getSharedPreferences("ContadorPrefs", MODE_PRIVATE);
        long tempoFinal = prefs.getLong(alertaKey, 0);

        if (tempoFinal == 0) {
            contadorTexto.setText("⏳ Tempo do alerta não disponível.");
            return;
        }

        long tempoRestante = tempoFinal - System.currentTimeMillis();

        new CountDownTimer(tempoRestante, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                contadorTexto.setText("⏳ Alerta expirando em: " + formatarTempo(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                prefs.edit().remove(alertaKey).apply();
            }
        }.start();
    }

    private String formatarTempo(long millis) {
        long minutos = (millis / 1000) / 60;
        long segundos = (millis / 1000) % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    private String gerarChaveUnica(Double latitude, Double longitude) {
        return latitude + "_" + longitude;
    }

    private String getTituloPersonalizado(String nomeAlerta) {
        switch (nomeAlerta) {
            case "botao_alagamento":
                return "🌊 Alagamento";
            case "botao_deslizamento":
                return "🪵 Deslizamento";
            case "botao_temporal":
                return "⛈️ Temporal";
            case "botao_acidente_carro":
                return "🚘 Acidente de Carro";
            case "botao_acidente_pedestre":
                return "🚴‍♂️ Acidente com Pedestre";
            case "botao_assaltos":
                return "💰 Assalto";
            case "botao_tiroteio":
                return "🔫 Tiroteio";
            case "botao_arrastao":
                return "🏃‍♂️ Arrastão";
            default:
                return "Alerta";
        }
    }

    private void atualizarMapaComLocalizacaoUsuario() {
        if (gMap != null) {
            LatLng userLocation = new LatLng(userLatitude, userLongitude);
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            MarkerOptions options = new MarkerOptions().position(userLocation).title("Sua localização");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            gMap.addMarker(options);
        }
    }

    private void atualizarMapaComLocalizacaoPadrao() {
        if (gMap != null) {
            LatLng liberdade = new LatLng(-23.563133, -46.635048);
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(liberdade, 15));
            MarkerOptions options = new MarkerOptions().position(liberdade).title("Liberdade, São Paulo");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            gMap.addMarker(options);
        }
    }

    private void configurarBotaoConfirmar() {
        Button botaoConfirmarRota = findViewById(R.id.botao_confirmar);
        botaoConfirmarRota.setOnClickListener(v -> tracarRota());
    }

    private void tracarRota() {
        String origem = ((EditText) inputLocalAtual).getText().toString();
        String destino = ((EditText) inputDestino).getText().toString();

        if (origem.isEmpty() || destino.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha ambos os campos de endereço!", Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressesOrigem = geocoder.getFromLocationName(origem, 1);
            List<Address> addressesDestino = geocoder.getFromLocationName(destino, 1);

            if (addressesOrigem != null && !addressesOrigem.isEmpty() && addressesDestino != null && !addressesDestino.isEmpty()) {
                LatLng latLngOrigem = new LatLng(addressesOrigem.get(0).getLatitude(), addressesOrigem.get(0).getLongitude());
                LatLng latLngDestino = new LatLng(addressesDestino.get(0).getLatitude(), addressesDestino.get(0).getLongitude());

                // Adiciona marcador no destino
                MarkerOptions destinoMarker = new MarkerOptions()
                        .position(latLngDestino)
                        .title("Destino")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                gMap.addMarker(destinoMarker);

                exibirRota(latLngOrigem, latLngDestino);
            } else {
                Toast.makeText(this, "Endereços inválidos. Por favor, tente novamente! ", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exibirRota(LatLng origem, LatLng destino) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(BuildConfig.MAPS_API_KEY)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();

        DirectionsApiRequest request = DirectionsApi.newRequest(context)
                .origin(new com.google.maps.model.LatLng(origem.latitude, origem.longitude))
                .destination(new com.google.maps.model.LatLng(destino.latitude, destino.longitude))
                .mode(TravelMode.DRIVING);

        new Thread(() -> {
            try {
                DirectionsResult result = request.await();

                runOnUiThread(() -> {
                    if (result.routes != null && result.routes.length > 0) {
                        // Removendo a rota anterior, caso existir no maps!
                        if (rotaAtual != null) {
                            rotaAtual.remove();
                        }

                        // Desenhando a rota nova. No momento, na cor "vermelho_fraco".
                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.addAll(PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath()));
                        polylineOptions.color(ContextCompat.getColor(this, R.color.vermelho_fraco));
                        rotaAtual = gMap.addPolyline(polylineOptions);

                        // Movendo a câmera para a origem.
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origem, 15));
                    } else {
                        Toast.makeText(this, "Rota não encontrada. Por favor, tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void configurarBotaoCentralizar() {
        centralizar.setOnClickListener(v -> {
            if (userLatitude != 0 && userLongitude != 0) {
                LatLng userLocation = new LatLng(userLatitude, userLongitude);
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            } else {
                Toast.makeText(this, "Localização atual não disponível.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPerfilUsuario() {
        btnPerfilUsuario.setOnClickListener(v -> {
            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
            int userId = sharedPreferencesManager.obterIdUsuario();

            if (userId != -1) {
                ObterUsuarioImpl obterUsuarioImpl = new ObterUsuarioImpl(this);
                obterUsuarioImpl.obterUsuario(userId, usuario -> {
                    Intent intent = new Intent(this, InformacoesPassageiroActivity.class);
                    intent.putExtra("usuario", usuario);
                    startActivity(intent);
                }, errorMessage -> {
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}