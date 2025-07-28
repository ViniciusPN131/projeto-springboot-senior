package hoops.metrics.api.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import hoops.metrics.api.domain.estatisticas.Estatistica;
import hoops.metrics.api.domain.jogador.Jogador;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {

    public byte[] gerarPdfJogador(Jogador jogador, List<Estatistica> estatisticasMvp, int quantidadeDeVitorias) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font texto = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph("Relatório do Jogador - MVP", titulo));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Nome: " + jogador.getNome(), texto));
        document.add(new Paragraph("Data de Nascimento: " + jogador.getData_nascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), texto));
        document.add(new Paragraph("Altura: " + jogador.getAltura() + " cm", texto));
        document.add(new Paragraph("Peso: " + jogador.getPeso() + " kg", texto));
        document.add(new Paragraph("Time: " + jogador.getClube().getNome(), texto));
        document.add(new Paragraph("Quantidade de MVPs: " + estatisticasMvp.size(), texto));
        document.add(new Paragraph("Quantidade de Vitórias: " + quantidadeDeVitorias, texto));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Estatísticas das Partidas MVP:", titulo));
        int i = 1;
        for (Estatistica estat : estatisticasMvp) {
            document.add(new Paragraph("Partida " + i++, texto));
            document.add(new Paragraph("Pontos: " + estat.getTotalPontos() +
                    ", Faltas: " + estat.getTotalFaltas() +
                    ", Reb. Ofensivos: " + estat.getRebotesOfensivos() +
                    ", Reb. Defensivos: " + estat.getRebotesDefensivos(), texto));
            document.add(new Paragraph("Assistências: N/A, Roubos: " + estat.getRoubosDeBola() +
                    ", Turnovers: " + estat.getTurnovers(), texto));
            document.add(new Paragraph(" "));
        }

        document.close();
        return out.toByteArray();
    }
}
