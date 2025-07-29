package hoops.metrics.api.controller;

import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.domain.Tecnico;
import hoops.metrics.api.dto.clube.DadosAtualizacaoClube;
import hoops.metrics.api.dto.clube.DadosCadastroClube;
import hoops.metrics.api.dto.clube.DadosDetalhamentoClube;
import hoops.metrics.api.dto.clube.DadosListagemClube;
import hoops.metrics.api.repository.ClubeRepository;
import hoops.metrics.api.repository.TecnicoRepository;
import hoops.metrics.api.service.ClubeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClubeControllerTest {

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private TecnicoRepository tecnicoRepository;

    @Mock
    private ClubeService clubeService;

    @InjectMocks
    private ClubeController clubeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarClubeComSucesso() {
        DadosCadastroClube dados = new DadosCadastroClube("Clube A", "CLUBE", "Cidade", "Estado", 1L, null);
        DadosDetalhamentoClube dadosListagem = mock(DadosDetalhamentoClube.class);

        when(clubeService.cadastrar(dados)).thenReturn(dadosListagem);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");
        ResponseEntity response = clubeController.cadastrarClube(dados, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertNotNull(response.getBody());
    }

    @Test
    void deveListarClubesAtivos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<DadosListagemClube> page = new PageImpl<>(List.of(mock(DadosListagemClube.class)));

        when(clubeService.listarAtivos(pageable)).thenReturn(page);

        ResponseEntity<Page<DadosListagemClube>> response = clubeController.listarClubes(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void deveAtualizarClube() {
        DadosAtualizacaoClube dados = new DadosAtualizacaoClube(1L, "Clube", "Sigla", "Cidade", "Estado", mock(Tecnico.class));
        DadosDetalhamentoClube dadosListagem = mock(DadosDetalhamentoClube.class);

        when(clubeService.atualizar(dados)).thenReturn(dadosListagem);

        ResponseEntity response = clubeController.atualizarClube(dados);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deveDeletarClube() {
        Long id = 1L;
        doNothing().when(clubeService).excluir(id);

        ResponseEntity response = clubeController.deletarClube(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deveContarVitoriasPorClube() {
        Long id = 3L;
        when(clubeService.contarVitorias(id)).thenReturn(12L);

        ResponseEntity<Long> response = clubeController.contarVitoriasPorClube(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(12L, response.getBody());
    }
}