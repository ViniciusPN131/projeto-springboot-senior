package hoops.metrics.api.dto.estatistica;

import hoops.metrics.api.domain.Estatistica;

public record DadosListagemEstatistica(
        Long id,
        Long jogadorId,
        String jogadorNome,
        Long partidaId,
        Integer totalPontos,
        Integer assistencias,
        Integer totalFaltas,
        Integer rebotesOfensivos,
        Integer rebotesDefensivos,
        Integer roubosDeBola,
        Integer turnovers
) {

    public DadosListagemEstatistica(Estatistica estatistica) {
        this(
                estatistica.getId(),
                estatistica.getJogador().getId(),
                estatistica.getJogador().getNome(),
                estatistica.getPartida().getId(),
                estatistica.getTotalPontos(),
                estatistica.getAssistencias(),
                estatistica.getTotalFaltas(),
                estatistica.getRebotesOfensivos(),
                estatistica.getRebotesDefensivos(),
                estatistica.getRoubosDeBola(),
                estatistica.getTurnovers()
        );
    }
}
