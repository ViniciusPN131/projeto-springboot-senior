package hoops.metrics.api.service;

import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.domain.Jogador;
import hoops.metrics.api.dto.clube.DadosDetalhamentoClube;
import hoops.metrics.api.dto.jogador.*;
import hoops.metrics.api.repository.ClubeRepository;
import hoops.metrics.api.repository.JogadorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JogadorService {

    @Autowired
    private ClubeRepository clubeRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    @Transactional
    public DadosDetalhamentoJogador cadastrar(@Valid DadosCadastroJogador dados) {

        if (jogadorRepository.existsByCpf(dados.cpf())){
            throw new IllegalArgumentException("CPF já cadastrado!");
        }
        if (dados==null){
            return null;
        }
        if (!clubeRepository.existsById(dados.clube_id())){
            throw new EntityNotFoundException("Tecnico não encontrado");
        }

        Clube clube = clubeRepository.findById(dados.clube_id()).get();
        Jogador jogador = new Jogador(dados);
        jogador.setClube(clube);

        jogadorRepository.save(jogador);

        return new DadosDetalhamentoJogador(jogador);

    }

    public Page<DadosListagemJogador> listarAtivos(Pageable paginacao) {

        return jogadorRepository.findAllByAtivoTrue(paginacao).map(DadosListagemJogador::new);

    }

    @Transactional
    public DadosDetalhamentoJogador atualizar(@Valid DadosAtualizacaoJogador dados) {

        var jogador = jogadorRepository.getReferenceById(dados.id());
        jogador.atualizarInformacoes(dados);
        return new DadosDetalhamentoJogador(jogador);

    }

    public boolean excluir(Long id) {

        if (jogadorRepository.existsById(id)) {

            var jogador = jogadorRepository.getReferenceById(id);

            jogador.excluir();
            return true;
        }

        return false;

    }
}
