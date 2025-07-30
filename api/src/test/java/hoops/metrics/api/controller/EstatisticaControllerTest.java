package hoops.metrics.api.controller;

import hoops.metrics.api.domain.Estatistica;
import hoops.metrics.api.domain.Jogador;
import hoops.metrics.api.domain.Partida;
import hoops.metrics.api.dto.estatistica.DadosAtualizacaoEstatistica;
import hoops.metrics.api.dto.estatistica.DadosCadastroEstatistica;
import hoops.metrics.api.dto.estatistica.DadosDetalhamentoEstatistica;
import hoops.metrics.api.dto.estatistica.DadosListagemEstatistica;
import hoops.metrics.api.repository.EstatisticaRepository;
import hoops.metrics.api.service.EstatisticaService;
import jakarta.persistence.EntityNotFoundException;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstatisticaControllerTest {

    @Mock
    private EstatisticaService estatisticaService;

    @InjectMocks
    private EstatisticaController estatisticaController;

    @Mock
    private EstatisticaRepository estatisticaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarEstatisticaComSucesso() {
        DadosCadastroEstatistica dados = new DadosCadastroEstatistica(1L, 2L, 20, 5, 7, 2, 1, 2);
        DadosDetalhamentoEstatistica detalhamento = new DadosDetalhamentoEstatistica(1L, 1L, 2L, 20, 5, 7, 2, 1, 2);

        when(estatisticaService.cadastrar(dados)).thenReturn(detalhamento);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");
        ResponseEntity<DadosDetalhamentoEstatistica> response = estatisticaController.cadastrar(dados, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertNotNull(response.getBody());
        assertEquals(detalhamento, response.getBody());
    }

    @Test
    void deveAtualizarEstatistica() {
        DadosAtualizacaoEstatistica dados = new DadosAtualizacaoEstatistica(1L, 25, 6, 8, 3, 2, 1);
        DadosDetalhamentoEstatistica detalhamento = new DadosDetalhamentoEstatistica(1L, 1L, 2L, 25, 6, 8, 3, 2, 1);

        when(estatisticaService.atualizar(dados)).thenReturn(detalhamento);

        ResponseEntity<DadosDetalhamentoEstatistica> response = estatisticaController.atualizar(dados);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(detalhamento, response.getBody());
    }

    @Test
    void deveDeletarEstatistica() {
        Long id = 1L;
        when(estatisticaService.excluir(id)).thenReturn(true);

        ResponseEntity<Void> response = estatisticaController.deletar(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(estatisticaService).excluir(id);
    }

    @Test
    void deveListarEstatisticasPaginadas() {

        Jogador jogador = mock(Jogador.class);
        Partida partida = mock(Partida.class);
        Estatistica estatistica = mock(Estatistica.class);

        when(estatistica.getJogador()).thenReturn(jogador);
        when(estatistica.getPartida()).thenReturn(partida);
        when(jogador.getNome()).thenReturn("Jogador Teste");
        when(partida.getDataHora()).thenReturn(LocalDateTime.now());

        DadosListagemEstatistica dados = new DadosListagemEstatistica(estatistica);
        Page<DadosListagemEstatistica> page = new PageImpl<>(List.of(dados));
        Pageable pageable = PageRequest.of(0, 10);

        when(estatisticaService.listar(pageable)).thenReturn(page);

        ResponseEntity<Page<DadosListagemEstatistica>> response = estatisticaController.listar(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(dados, response.getBody().getContent().get(0));
    }

    //====================cadastrar invalido============================================================================
    @Test
    void deveRetornarBadRequestAoCadastrarEstatisticaComDadosInvalidos() {
        DadosCadastroEstatistica dados = new DadosCadastroEstatistica(
                null,
                null,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1
        );

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");
        ResponseEntity<DadosDetalhamentoEstatistica> response = estatisticaController.cadastrar(dados, uriBuilder);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    //====================cadastrar ja existente============================================================================
    @Test
    void deveRetornarConflictAoCadastrarEstatisticaExistente() {
        DadosCadastroEstatistica dados = new DadosCadastroEstatistica(1L, 2L, 20, 5, 7, 2, 1, 2);

        when(estatisticaRepository.existsByJogadorIdAndPartidaId(1L, 2L)).thenReturn(true);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");
        ResponseEntity<DadosDetalhamentoEstatistica> response = estatisticaController.cadastrar(dados, uriBuilder);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    //====================atualizar invalido============================================================================
    @Test
    void deveRetornarBadRequestAoAtualizarEstatisticaComDadosInvalidos() {
        DadosAtualizacaoEstatistica dados = new DadosAtualizacaoEstatistica(
                null,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1
        );

        ResponseEntity<DadosDetalhamentoEstatistica> response = estatisticaController.atualizar(dados);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    //====================deletar invalido==============================================================================
    @Test
    void deveRetornarNotFoundAoDeletarEstatisticaInexistente() {
        Long idInexistente = 999L;

        when(estatisticaRepository.existsById(idInexistente)).thenReturn(false);

        ResponseEntity<Void> response = estatisticaController.deletar(idInexistente);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}