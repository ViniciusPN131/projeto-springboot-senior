package hoops.metrics.api.controller;

import hoops.metrics.api.domain.clube.*;
import hoops.metrics.api.domain.clube.DadosAtualizacaoClube;
import hoops.metrics.api.domain.clube.DadosDetalhamentoClube;
import hoops.metrics.api.domain.clube.DadosListagemClube;
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
@RequestMapping("clubes")
public class ClubeController {

    @Autowired
    private ClubeRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarClube(@RequestBody @Valid DadosCadastroClube dados, UriComponentsBuilder uriBuilder){

        var clube = new Clube(dados);
        repository.save(clube);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(clube.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoClube(clube));

    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemClube>> listarClubes(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){

        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemClube::new);

        return  ResponseEntity.ok(page);

    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarClube(@RequestBody @Valid DadosAtualizacaoClube dados){

        var clube = repository.getReferenceById(dados.id());
        clube.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoClube(clube));

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarClube(@PathVariable Long id){

        var clube = repository.getReferenceById(id);
        clube.excluir();

        return ResponseEntity.noContent().build();

    }

    

}
