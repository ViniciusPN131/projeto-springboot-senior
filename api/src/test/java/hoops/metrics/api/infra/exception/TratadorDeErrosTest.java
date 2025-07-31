package hoops.metrics.api.infra.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TratadorDeErrosTest {

    @InjectMocks
    private TratadorDeErros tratadorDeErros;

    @Mock
    private WebRequest webRequest;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        // Configuração removida daqui e movida para os testes individuais
    }

    @Test
    void handleResponseStatusException_DeveRetornarResponseEntityComErro() {
        // Configuração específica para este teste
        when(webRequest.getDescription(false)).thenReturn("test-path");

        String mensagem = "Erro específico";
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);

        ResponseEntity<TratadorDeErros.ErrorResponse> response =
                tratadorDeErros.handleResponseStatusException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(mensagem, response.getBody().error());
        assertEquals("test-path", response.getBody().path());
    }

    @Test
    void tratarErro404_DeveRetornarNotFound() {
        // Configuração específica para este teste
        when(webRequest.getDescription(false)).thenReturn("test-path");

        ResponseEntity<TratadorDeErros.ErrorResponse> response =
                tratadorDeErros.tratarErro404(webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Recurso não encontrado", response.getBody().error());
        assertEquals("test-path", response.getBody().path());
    }

    @Test
    void tratarErro400_DeveRetornarBadRequestComErrosDeValidacao() {
        // Configuração específica para este teste
        FieldError fieldError1 = new FieldError("objeto", "campo1", "Erro campo 1");
        FieldError fieldError2 = new FieldError("objeto", "campo2", "Erro campo 2");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<List<TratadorDeErros.DadosErroValidacao>> response =
                tratadorDeErros.tratarErro400(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("campo1", response.getBody().get(0).campo());
        assertEquals("Erro campo 1", response.getBody().get(0).mensagem());
    }

    @Test
    void tratarErroRegraDeNegocio_DeveRetornarBadRequest() {
        // Configuração específica para este teste
        when(webRequest.getDescription(false)).thenReturn("test-path");

        String mensagem = "Erro de regra de negócio";
        IllegalArgumentException ex = new IllegalArgumentException(mensagem);

        ResponseEntity<TratadorDeErros.ErrorResponse> response =
                tratadorDeErros.tratarErroRegraDeNegocio(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(mensagem, response.getBody().error());
        assertEquals("test-path", response.getBody().path());
    }

    @Test
    void tratarErro500_DeveRetornarInternalServerError() {
        // Configuração específica para este teste
        when(webRequest.getDescription(false)).thenReturn("test-path");

        Exception ex = new Exception("Erro genérico");

        ResponseEntity<TratadorDeErros.ErrorResponse> response =
                tratadorDeErros.tratarErro500(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno no servidor", response.getBody().error());
        assertEquals("test-path", response.getBody().path());
    }

    @Test
    void errorResponse_DeveConterTodosOsCampos() {
        LocalDateTime now = LocalDateTime.now();
        int status = 400;
        String error = "Bad Request";
        String path = "/test";

        TratadorDeErros.ErrorResponse response =
                new TratadorDeErros.ErrorResponse(now, status, error, path);

        assertEquals(now, response.timestamp());
        assertEquals(status, response.status());
        assertEquals(error, response.error());
        assertEquals(path, response.path());
    }

    @Test
    void dadosErroValidacao_DeveMapearFieldErrorCorretamente() {
        FieldError fieldError = new FieldError("objeto", "campo", "mensagem de erro");

        TratadorDeErros.DadosErroValidacao dados =
                new TratadorDeErros.DadosErroValidacao(fieldError);

        assertEquals("campo", dados.campo());
        assertEquals("mensagem de erro", dados.mensagem());
    }
}