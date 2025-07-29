package hoops.metrics.api.dto.estatistica;

public interface DadosGeraisEstatistica {
        Long getJogadorId();
        Integer getTotalPontos();
        Integer getTotalFaltas();
        Integer getRebotes_ofensivos();
        Integer getRebotes_defensivos();
        Integer getRoubos_de_bola();
        Integer getTurnovers();
}

