package hoops.metrics.api.controller;

import hoops.metrics.api.dto.tecnico.*;
import hoops.metrics.api.service.TecnicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("tecnicos")
public class TecnicoController {

    @Autowired
    private TecnicoService tecnicoService;

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrarTecnico(@RequestBody @Valid DadosCadastroTecnico dados, UriComponentsBuilder uriBuilder) {
        DadosDetalhamentoTecnico tecnicoCriado = tecnicoService.cadastrar(dados);
        var uri = uriBuilder.path("/tecnicos/{id}").buildAndExpand(tecnicoCriado.id()).toUri();
        return ResponseEntity.created(uri).body(tecnicoCriado);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTecnico>> listarTecnicoes(@PageableDefault(size = 10, sort = {"id"}) Pageable paginacao) {

        Page<DadosListagemTecnico> page = tecnicoService.listarAtivos(paginacao);
        return ResponseEntity.ok(page);

    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarTecnico(@RequestBody @Valid DadosAtualizacaoTecnico dados) {

        DadosDetalhamentoTecnico tecncioAtualizado = tecnicoService.atualizar(dados);
        if (tecncioAtualizado != null) {
            return ResponseEntity.ok(tecncioAtualizado);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Técnico não encontrado");
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarTecnico(@PathVariable Long id) {
        if (tecnicoService.excluir(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/tecnico/vitorias/{tecnicoId}")
    public ResponseEntity<DadosDetalhamentoVitoriasTecnico> contarVitoriasPorTecnico(@PathVariable Long tecnicoId) {
        DadosDetalhamentoVitoriasTecnico total = tecnicoService.contarVitorias(tecnicoId);
        return ResponseEntity.ok(total);
    }

}