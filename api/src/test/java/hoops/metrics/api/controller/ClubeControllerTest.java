package hoops.metrics.api.controller;

import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.domain.Tecnico;
import hoops.metrics.api.dto.clube.*;
import hoops.metrics.api.repository.ClubeRepository;
import hoops.metrics.api.repository.TecnicoRepository;
import hoops.metrics.api.service.ClubeService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ClubeControllerTest {

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private TecnicoRepository tecnicoRepository;

    @Mock
    private ClubeService clubeService;

    @InjectMocks
    private ClubeController clubeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clubeController).build();
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
    void deveRetornarBadRequestAoCadastrarClubeComCorpoVazio() throws Exception {

        String jsonRequest = """
                {
                    "tecnico_id":1,
                    "nome":"",
                    "sigla":"",
                    "cidade":"",
                    "estado":""
                }
                """;

        mockMvc.perform(put("/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deveLancarExcecaoAoCadastrarClubeComTecnicoJaEmUso() throws Exception {

        //Quando cadastrar clube ao tentar cadastrar com id ja em uso retornar excessao de tecnico ja em uso.

        String jsonRequest = """
                {
                    "nome":"111",
                    "sigla":"111",
                    "cidade":"1",
                    "estado":"1",
                    "tecnico_id": 1
                }
                """;

        when(clubeRepository.verificarSeTecnicoEstaDisponivel(1L)).thenReturn(false);

        mockMvc.perform(put("/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());


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
    void deveRetornarExcecaoAoAtualizarClubeInvalido() {
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
        when(clubeService.excluir(id)).thenReturn(true);

        ResponseEntity response = clubeController.deletarClube(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

   @Test
   void deveRetornarBadRequestAoTentarDeletarClubeInvalido() throws Exception {

       Long idInvalido = 999L;

       when(clubeRepository.existsById(idInvalido)).thenReturn(false);

       ResponseEntity<?> response = clubeController.deletarClube(idInvalido);

       assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

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
    void deveContarVitoriasPorClube() {
        Long id = 3L;
        when(clubeService.contarVitorias(id)).thenReturn(new DadosDetalhamentoVitoriasClube(1L, 10));

        ResponseEntity<DadosDetalhamentoVitoriasClube> response = clubeController.contarVitoriasPorClube(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(12L, response.getBody());
    }
}