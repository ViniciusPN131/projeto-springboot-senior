package hoops.metrics.api.controller;

import hoops.metrics.api.dto.estatistica.DadosAtualizacaoEstatistica;
import hoops.metrics.api.dto.estatistica.DadosCadastroEstatistica;
import hoops.metrics.api.dto.estatistica.DadosDetalhamentoEstatistica;
import hoops.metrics.api.dto.estatistica.DadosListagemEstatistica;
import hoops.metrics.api.repository.EstatisticaRepository;
import hoops.metrics.api.repository.JogadorRepository;
import hoops.metrics.api.repository.PartidaRepository;
import hoops.metrics.api.service.EstatisticaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/estatisticas")
public class EstatisticaController {

    @Autowired
    private EstatisticaRepository estatisticaRepository;
    @Autowired
    private JogadorRepository jogadorRepository;
    @Autowired
    private PartidaRepository partidaRepository;

    private final EstatisticaService estatisticaService;

    @Autowired
    public EstatisticaController(EstatisticaService estatisticaService) {
        this.estatisticaService = estatisticaService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoEstatistica> cadastrar(@RequestBody @Valid DadosCadastroEstatistica dados, UriComponentsBuilder uriBuilder) {
        DadosDetalhamentoEstatistica estatisticaCriada = estatisticaService.cadastrar(dados);
        if (estatisticaCriada == null) return ResponseEntity.badRequest().build();
        var uri = uriBuilder.path("/estatisticas/{id}").buildAndExpand(estatisticaCriada.id()).toUri();
        return ResponseEntity.created(uri).body(estatisticaCriada);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemEstatistica>> listar(@PageableDefault(size = 10, sort = {"totalPontos"},direction = Sort.Direction.DESC) Pageable paginacao) {

        Page<DadosListagemEstatistica> page = estatisticaService.listar(paginacao);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoEstatistica> atualizar(@RequestBody @Valid DadosAtualizacaoEstatistica dados) {

        DadosDetalhamentoEstatistica estatisticaAtualizada = estatisticaService.atualizar(dados);
        if (estatisticaAtualizada == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estatisticaAtualizada);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean validar = estatisticaService.excluir(id);
        if (validar)
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }


}
