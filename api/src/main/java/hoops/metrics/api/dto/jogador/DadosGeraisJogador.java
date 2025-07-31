package hoops.metrics.api.dto.jogador;

public interface DadosGeraisJogador {
        Long getJogadorId();
        Integer getTotalPontos();
        Integer getAssistencias();
        Integer getTotalFaltas();
        Integer getRebotes_ofensivos();
        Integer getRebotes_defensivos();
        Integer getRoubos_de_bola();
        Integer getTurnovers();
}

