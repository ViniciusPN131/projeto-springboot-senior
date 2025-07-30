
package hoops.metrics.api.controller;

import hoops.metrics.api.domain.*;
import hoops.metrics.api.dto.clube.DadosAtualizacaoClube;
import hoops.metrics.api.dto.clube.DadosCadastroClube;
import hoops.metrics.api.dto.clube.DadosDetalhamentoClube;
import hoops.metrics.api.dto.clube.DadosListagemClube;
import hoops.metrics.api.dto.estatistica.DadosGeraisEstatistica;
import hoops.metrics.api.dto.jogador.*;
import hoops.metrics.api.repository.ClubeRepository;
import hoops.metrics.api.repository.JogadorRepository;
import hoops.metrics.api.service.JogadorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JogadorControllerTest {

    @InjectMocks
    private JogadorController jogadorController;

    @Mock
    private JogadorRepository jogadorRepository;

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private JogadorService jogadorService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jogadorController).build();
    }

    @Test
    void deveCadastrarJogadorComSucesso() {
        DadosPostJogador dados = new DadosPostJogador("Clube A", "CLUBE", LocalDate.of(2000, 01, 01), 190, 60.0f, Posicao.ARMADOR, 1L);
        DadosDetalhamentoJogador dadosJogador = mock(DadosDetalhamentoJogador.class);

        Jogador jogador = mock(Jogador.class);

        when(jogadorService.cadastrar(dados)).thenReturn(jogador);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");
        ResponseEntity response = jogadorController.cadastrarJogador(dados, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertNotNull(response.getBody());
    }

    @Test
    void deveAtualizarJogador() {
        DadosAtualizacaoJogador dados = new DadosAtualizacaoJogador(1L, "aaaaaaaa", 50.0f, 190, mock(Clube.class), Posicao.ARMADOR);
        DadosDetalhamentoJogador dadosListagem = mock(DadosDetalhamentoJogador.class);

        when(jogadorService.atualizar(dados)).thenReturn(dadosListagem);

        ResponseEntity response = jogadorController.atualizarJogador(dados);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deveDeletarJogador() {
        Long idInvalido = 1L;

        when(jogadorRepository.existsById(idInvalido)).thenReturn(false);

        ResponseEntity<?> response = jogadorController.deletarJogador(idInvalido);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveListarJogadores() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<DadosListagemJogador> page = new PageImpl<>(List.of(mock(DadosListagemJogador.class)));

        when(jogadorService.listar(pageable)).thenReturn(page);

        ResponseEntity<Page<DadosListagemJogador>> response = jogadorController.listarJogadores(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void deveBuscarMvpDaPartida() {
        Estatistica estatistica = mock(Estatistica.class);
        when(jogadorRepository.buscarMvpDaPartida(10L)).thenReturn(List.of(estatistica));

        ResponseEntity<List<Estatistica>> response = jogadorController.buscarMvpDaPartida(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void deveBuscarEstatisticasGeraisPorJogador() {
        DadosGeraisEstatistica dados = mock(DadosGeraisEstatistica.class);
        Jogador jogador = mock(Jogador.class);

        when(jogadorRepository.estatisticasGeraisPorJogador(1L)).thenReturn(dados);
        when(jogadorRepository.findById(1L)).thenReturn(Optional.of(jogador));

        ResponseEntity<DadosGeraisEstatistica> response = jogadorController.estatisticasGeraisPorJogador(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dados, response.getBody());
    }

    @Test
    void deveRetornarBadRequestComIdInvalidoParaEstatsiticasGerais() {
        DadosGeraisEstatistica dados = mock(DadosGeraisEstatistica.class);
        Jogador jogador = mock(Jogador.class);

        when(jogadorRepository.estatisticasGeraisPorJogador(1L)).thenReturn(dados);
        when(jogadorRepository.findById(1L)).thenReturn(Optional.of(jogador));

        ResponseEntity<DadosGeraisEstatistica> response = jogadorController.estatisticasGeraisPorJogador(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dados, response.getBody());
    }

    //====================cadastrar invalido============================================================================
    @Test
    void deveRetornarBadRequestAoCadastrarJogadorComDadosInvalidos() {
        DadosPostJogador dadosPost = new DadosPostJogador(
                "", // Nome vazio
                "",
                null, // Data de nascimento nula
                0, // Altura inválida
                0, // Peso inválido
                null, // Posição nula
                null // ID do clube nulo
        );

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");
        ResponseEntity<?> response = jogadorController.cadastrarJogador(dadosPost, uriBuilder);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    //====================cadastrar ja existente============================================================================
    @Test
    void deveRetornarBadRequestQuandoCpfJaEstiverCadastrado() throws Exception {
        String jsonRequest = """
        {
            "nome": "Roberval",
            "cpf": "1234567809",
            "data_nascimento": "2007-07-28",
            "altura": 188,
            "peso": 75.0,
            "posicao": "ALA",
            "clube_id": 1
        }
        """;

        when(jogadorRepository.existsByCpf("1234567809")).thenReturn(true);

        mockMvc.perform(post("/jogadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    //====================atualizar invalido============================================================================
    @Test
    void deveRetornarBadRequestAoAtualizarJogadorComDadosInvalidos() {
        DadosAtualizacaoJogador dados = new DadosAtualizacaoJogador(1L, "Clube", 45.5f, 190, mock(Clube.class), Posicao.ARMADOR);
        DadosDetalhamentoJogador dadosListagem = mock(DadosDetalhamentoJogador.class);

        when(jogadorService.atualizar(dados)).thenReturn(dadosListagem);

        ResponseEntity response = jogadorController.atualizarJogador(dados);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    //====================deletar invalido==============================================================================
    @Test
    void deveRetornarNotFoundAoDeletarJogadorInexistente() {
        Long idInexistente = 999L;

        when(jogadorRepository.existsById(idInexistente)).thenReturn(false);

        ResponseEntity<?> response = jogadorController.deletarJogador(idInexistente);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
