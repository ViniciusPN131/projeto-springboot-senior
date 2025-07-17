package hoops.metrics.api.controller;

import hoops.metrics.api.domain.clube.*;
import hoops.metrics.api.domain.jogador.DadosListagemJogador;
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

        var page = repository.findAll(paginacao).map(DadosListagemClube::new);

        return  ResponseEntity.ok(page);

    }

}
