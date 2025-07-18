package hoops.metrics.api.controller;

import hoops.metrics.api.domain.clube.*;
import hoops.metrics.api.domain.clube.DadosAtualizacaoClube;
import hoops.metrics.api.domain.clube.DadosDetalhamentoClube;
import hoops.metrics.api.domain.clube.DadosListagemClube;
import hoops.metrics.api.domain.tecnico.Tecnico;
import hoops.metrics.api.domain.tecnico.TecnicoRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private ClubeRepository clubeRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarClube(@RequestBody @Valid DadosCadastroClube dados, UriComponentsBuilder uriBuilder){

        var tecnico = tecnicoRepository.findById(dados.tecnico_id())
                .orElseThrow(() -> new EntityNotFoundException("Técnico não encontrado"));

        var clube = new Clube(dados);
        clube.setTecnico(tecnico);

        clubeRepository.save(clube);

        var uri = uriBuilder.path("/clubes/{id}").buildAndExpand(clube.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoClube(clube));


    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemClube>> listarClubes(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){

        var page = clubeRepository.findAllByAtivoTrue(paginacao).map(DadosListagemClube::new);

        return  ResponseEntity.ok(page);

    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarClube(@RequestBody @Valid DadosAtualizacaoClube dados){

        var clube = clubeRepository.getReferenceById(dados.id());
        clube.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoClube(clube));

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarClube(@PathVariable Long id){

        var clube = clubeRepository.getReferenceById(id);
        clube.excluir();

        return ResponseEntity.noContent().build();

    }

    

}
