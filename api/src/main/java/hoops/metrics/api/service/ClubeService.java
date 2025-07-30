package hoops.metrics.api.service;

import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.dto.clube.DadosAtualizacaoClube;
import hoops.metrics.api.dto.clube.DadosCadastroClube;
import hoops.metrics.api.dto.clube.DadosDetalhamentoClube;
import hoops.metrics.api.dto.clube.DadosListagemClube;
import hoops.metrics.api.repository.ClubeRepository;
import hoops.metrics.api.repository.TecnicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;
    private final TecnicoRepository tecnicoRepository;

    public ClubeService(ClubeRepository clubeRepository, TecnicoRepository tecnicoRepository) {
        this.clubeRepository = clubeRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    @Transactional
    public DadosDetalhamentoClube cadastrar(DadosCadastroClube dados) {
        var tecnico = tecnicoRepository.findById(dados.tecnico_id())
                .orElseThrow(() -> new EntityNotFoundException("Técnico não encontrado"));
        var clube = new Clube(dados);
        clube.setTecnico(tecnico);
        if (clubeRepository.verificarSeTecnicoEstaDisponivel(dados.tecnico_id())) {
            clubeRepository.save(clube);
            return new DadosDetalhamentoClube(clube);
        }
        throw new IllegalArgumentException("Tecnico ja em uso");
    }

    public Page<DadosListagemClube> listarAtivos(Pageable paginacao) {
        return clubeRepository.findAllByAtivoTrue(paginacao).map(DadosListagemClube::new);
    }

    @Transactional
    public DadosDetalhamentoClube atualizar(DadosAtualizacaoClube dados) {
        var clube = clubeRepository.getReferenceById(dados.id());
        clube.atualizarInformacoes(dados);
        return new DadosDetalhamentoClube(clube);
    }

    @Transactional
    public boolean excluir(Long id) {
        if (clubeRepository.existsById(id)) {
            var clube = clubeRepository.getReferenceById(id);
            clube.excluir();
            return true;
        }
        return false;
    }

    public Long contarVitorias(Long clubeId) {

        return clubeRepository.contarVitoriasPorClube(clubeId);

    }
}