package hoops.metrics.api.controller;

import hoops.metrics.api.dto.clube.DadosAtualizacaoClube;
import hoops.metrics.api.dto.clube.DadosCadastroClube;
import hoops.metrics.api.dto.clube.DadosDetalhamentoClube;
import hoops.metrics.api.dto.clube.DadosListagemClube;
import hoops.metrics.api.service.ClubeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/clubes")
public class ClubeController {

    private final ClubeService clubeService;

    @Autowired
    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarClube(@RequestBody @Valid DadosCadastroClube dados, UriComponentsBuilder uriBuilder) {
        DadosDetalhamentoClube clubeCriado = clubeService.cadastrar(dados);
        var uri = uriBuilder.path("/clubes/{id}").buildAndExpand(clubeCriado.id()).toUri();
        return ResponseEntity.created(uri).body(clubeCriado);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemClube>> listarClubes(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        Page<DadosListagemClube> page = clubeService.listarAtivos(paginacao);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarClube(@RequestBody @Valid DadosAtualizacaoClube dados) {
        DadosDetalhamentoClube clubeAtualizado = clubeService.atualizar(dados);
        return ResponseEntity.ok(clubeAtualizado);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarClube(@PathVariable Long id) {
        clubeService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clube/vitorias/{clubeId}")
    public ResponseEntity<Long> contarVitoriasPorClube(@PathVariable Long clubeId) {
        Long total = clubeService.contarVitorias(clubeId);
        return ResponseEntity.ok(total);
    }
}