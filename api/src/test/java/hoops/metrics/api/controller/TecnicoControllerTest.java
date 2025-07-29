package hoops.metrics.api.controller;

import hoops.metrics.api.domain.Tecnico;
import hoops.metrics.api.dto.tecnico.DadosAtualizacaoTecnico;
import hoops.metrics.api.dto.tecnico.DadosCadastroTecnico;
import hoops.metrics.api.dto.tecnico.DadosDetalhamentoTecnico;
import hoops.metrics.api.dto.tecnico.DadosListagemTecnico;
import hoops.metrics.api.repository.TecnicoRepository;
import hoops.metrics.api.service.TecnicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TecnicoControllerTest {

    @InjectMocks
    private TecnicoController tecnicoController;

    @Mock
    private TecnicoRepository tecnicoRepository;

    @Mock
    private TecnicoService tecnicoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tecnicoController).build();
    }

    @Test
    void deveDeletarTecnico() {
        Long id = 1L;
        when(tecnicoService.excluir(id)).thenReturn(true);

        ResponseEntity<?> response = tecnicoController.deletarTecnico(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tecnicoService, times(1)).excluir(id);
    }

    @Test
    void deveLancarExcecaoSeCrefJaExistir() {
        DadosCadastroTecnico dados = new DadosCadastroTecnico("Técnico 2", "123456");

        when(tecnicoService.cadastrar(dados))
                .thenThrow(new IllegalArgumentException("CREF já cadastrado"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> tecnicoController.cadastrarTecnico(
                        dados,
                        UriComponentsBuilder.newInstance()
                )
        );

        assertEquals("CREF já cadastrado", exception.getMessage());
        verify(tecnicoService, times(1)).cadastrar(dados);
    }

    @Test
    void deveCadastrarTecnicoComSucesso() {
        DadosCadastroTecnico dados = new DadosCadastroTecnico("Técnico Teste", "CREF123");

        // Mock do DTO que será retornado pelo serviço
        DadosDetalhamentoTecnico dtoRetorno = new DadosDetalhamentoTecnico(1L, "Técnico Teste", "CREF123");

        when(tecnicoService.cadastrar(any(DadosCadastroTecnico.class)))
                .thenReturn(dtoRetorno);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");
        ResponseEntity<?> response = tecnicoController.cadastrarTecnico(dados, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof DadosDetalhamentoTecnico);

        DadosDetalhamentoTecnico body = (DadosDetalhamentoTecnico) response.getBody();
        assertEquals(1L, body.id());
        assertEquals("Técnico Teste", body.nome());
        assertEquals("CREF123", body.cref());

        verify(tecnicoService, times(1)).cadastrar(dados);
    }

    @Test
    void deveAtualizarTecnico() {
        Long id = 1L;
        DadosAtualizacaoTecnico dados = new DadosAtualizacaoTecnico(id, "Novo Nome", "CREF456");

        // Mock do DTO que será retornado pelo serviço
        DadosDetalhamentoTecnico dtoRetorno = new DadosDetalhamentoTecnico(id, "Novo Nome", "CREF456");

        when(tecnicoService.atualizar(any(DadosAtualizacaoTecnico.class)))
                .thenReturn(dtoRetorno);

        ResponseEntity<?> response = tecnicoController.atualizarTecnico(dados);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof DadosDetalhamentoTecnico);

        DadosDetalhamentoTecnico body = (DadosDetalhamentoTecnico) response.getBody();
        assertEquals(id, body.id());
        assertEquals("Novo Nome", body.nome());
        assertEquals("CREF456", body.cref());

        verify(tecnicoService, times(1)).atualizar(dados);
    }

    @Test
    void deveListarTecnicos() {
        // Criar o DTO diretamente (não precisa mockar a entidade)
        DadosListagemTecnico dto = new DadosListagemTecnico(1L, "Técnico 1", "CREF123");

        Page<DadosListagemTecnico> page = new PageImpl<>(List.of(dto));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome"));

        when(tecnicoService.listarAtivos(pageable)).thenReturn(page);

        ResponseEntity<Page<DadosListagemTecnico>> response = tecnicoController.listarTecnicoes(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());

        DadosListagemTecnico primeiroItem = response.getBody().getContent().get(0);
        assertEquals(1L, primeiroItem.id());
        assertEquals("Técnico 1", primeiroItem.nome());
        assertEquals("CREF123", primeiroItem.cref());
    }

    @Test
    void deveContarVitoriasPorTecnico() {
        when(tecnicoService.contarVitorias(5L)).thenReturn(7);

        ResponseEntity<Long> response = tecnicoController.contarVitoriasPorTecnico(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(7L, response.getBody().longValue()); // Usar longValue() para evitar autoboxing
    }

    @Test
    void deveRetornarNotFoundAoDeletarTecnicoInvalido() {
        Long idInvalido = 999L;

        when(tecnicoRepository.existsById(idInvalido)).thenReturn(false);

        ResponseEntity<?> response = tecnicoController.deletarTecnico(idInvalido);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverTecnicos() {
        Page<DadosListagemTecnico> page = new PageImpl<>(List.of());
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome"));

        when(tecnicoService.listarAtivos(pageable)).thenReturn(page);

        ResponseEntity<Page<DadosListagemTecnico>> response = tecnicoController.listarTecnicoes(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody()); // Agora deve passar
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void deveRetornarBadRequestAoCadastrarTecnicoComNomeVazio() throws Exception {
        String requestBody = "{\"nome\": \"\", \"cref\": \"CREF123\"}"; // Adicionar cref obrigatório

        mockMvc.perform(post("/tecnicos")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarBadRequestAoCadastrarTecnicoComCorpoVazio() throws Exception {
        String requestBody = "{\"nome\": \"\", \"cref\": \"\"}"; // Campos vazios mas presentes

        mockMvc.perform(post("/tecnicos")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}