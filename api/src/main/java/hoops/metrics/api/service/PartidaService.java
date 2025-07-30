package hoops.metrics.api.service;

import hoops.metrics.api.domain.Partida;
import hoops.metrics.api.dto.partida.*;
import hoops.metrics.api.repository.ClubeRepository;
import hoops.metrics.api.repository.PartidaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartidaService {

    ClubeRepository clubeRepository;
    PartidaRepository partidaRepository;

    public PartidaService(ClubeRepository clubeRepository, PartidaRepository partidaRepository) {
        this.clubeRepository = clubeRepository;
        this.partidaRepository = partidaRepository;
    }

    @Transactional
    public DadosDetalhamentoPartida cadastrar(@Valid DadosCadastroPartida dados) {

        var clubeCasa = clubeRepository.getReferenceById(dados.timeCasaId());
        var clubeVisitante = clubeRepository.getReferenceById(dados.timeVisitanteId());

        Partida partida = new Partida(dados, clubeCasa, clubeVisitante);
        partidaRepository.save(partida);

        return new DadosDetalhamentoPartida(partida);

    }

    public Page<DadosListagemPartida> listar(Pageable paginacao) {

        return partidaRepository.findAllByAtivoTrue(paginacao).map(DadosListagemPartida::new);

    }

    @Transactional
    public DadosDetalhamentoPartida atualizar(@Valid DadosAtualizacaoPartida dados) {

        Partida partida = partidaRepository.getReferenceById(dados.id());
        partida.atualizarInformacoes(dados);

        return new DadosDetalhamentoPartida(partida);

    }

    @Transactional
    public boolean excluir(Long id) {
        if (!partidaRepository.existsById(id)) {
            return false;
        }
        Partida partida = partidaRepository.getReferenceById(id);
        partida.excluir();
        return true;
    }

    public DadosResultadoPartida buscarResultado(Long partidaId) {
        return partidaRepository.buscarResultadoDaPartida(partidaId);
    }
}
