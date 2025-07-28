package hoops.metrics.api.controller;

import hoops.metrics.api.domain.estatisticas.*;
import hoops.metrics.api.domain.jogador.Jogador;
import hoops.metrics.api.domain.jogador.JogadorRepository;
import hoops.metrics.api.domain.partida.DadosResultadoPartida;
import hoops.metrics.api.domain.partida.Partida;
import hoops.metrics.api.domain.partida.PartidaRepository;
import jakarta.servlet.http.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class EstatisticaControllerTest {

    @Mock
    private EstatisticaRepository estatisticaRepository;

    @Mock
    private JogadorRepository jogadorRepository;

    @Mock
    private PartidaRepository partidaRepository;

    @InjectMocks
    private EstatisticaController estatisticaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarEstatisticaComSucesso() {
        DadosCadastroEstatistica dados = new DadosCadastroEstatistica(1L, 2L, 20, 5, 7, 2, 1, 2);
        Jogador jogador = mock(Jogador.class);
        Partida partida = mock(Partida.class);
        Estatistica estatistica = new Estatistica(dados, jogador, partida);

        when(jogadorRepository.getReferenceById(1L)).thenReturn(jogador);
        when(partidaRepository.getReferenceById(2L)).thenReturn(partida);
        when(estatisticaRepository.save(any())).thenReturn(estatistica);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        ResponseEntity<DadosDetalhamentoEstatistica> response = estatisticaController.cadastrarEstatistica(dados, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        URI location = response.getHeaders().getLocation();
        assertNotNull(location);
        assertNotNull(response.getBody());
    }

    @Test
    void deveListarEstatisticasPaginadas() {
        Estatistica estatistica = mock(Estatistica.class);
        Page<Estatistica> page = new PageImpl<>(List.of(estatistica));
        Pageable pageable = PageRequest.of(0, 10);

        Jogador jogador = mock(Jogador.class);
        Partida partida = mock(Partida.class);

        when(estatisticaRepository.findAll(pageable)).thenReturn(page);

        when(estatistica.getJogador()).thenReturn(jogador);
        when(estatistica.getPartida()).thenReturn(partida);

        ResponseEntity<Page<DadosListagemEstatistica>> response = estatisticaController.listar(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void deveAtualizarEstatistica() {
        DadosAtualizacaoEstatistica dados = new DadosAtualizacaoEstatistica(1L, 25, 6, 8, 3, 2, 1);
        Estatistica estatistica = mock(Estatistica.class);

        Jogador jogador = mock(Jogador.class);
        Partida partida = mock(Partida.class);

        when(estatisticaRepository.getReferenceById(1L)).thenReturn(estatistica);

        when(estatistica.getJogador()).thenReturn(jogador);
        when(estatistica.getPartida()).thenReturn(partida);

        ResponseEntity<DadosDetalhamentoEstatistica> response = estatisticaController.atualizar(dados);

        verify(estatistica).atualizarInformacoes(dados);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deveDeletarEstatistica() {
        doNothing().when(estatisticaRepository).deleteById(1L);

        ResponseEntity<Void> response = estatisticaController.deletar(1L);

        verify(estatisticaRepository).deleteById(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }










}
