package hoops.metrics.api.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import hoops.metrics.api.domain.Estatistica;
import hoops.metrics.api.domain.Jogador;
import hoops.metrics.api.dto.jogador.DadosGeraisJogador;
import hoops.metrics.api.repository.EstatisticaRepository;
import hoops.metrics.api.repository.JogadorRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {
    private final JogadorRepository jogadorRepository;
    private final EstatisticaRepository estatisticaRepository;

    public PdfService(JogadorRepository jogadorRepository,
                      EstatisticaRepository estatisticaRepository) {
        this.jogadorRepository = jogadorRepository;
        this.estatisticaRepository = estatisticaRepository;
    }

    public ResponseEntity<byte[]> exportar(Long id) {
        Jogador jogador = jogadorRepository.findById(id).orElse(null);
        if (jogador == null) {
            return ResponseEntity.notFound().build();
        }

        DadosGeraisJogador dadosGeraisJogador = jogadorRepository.estatisticasGeraisPorJogador(id);
        if (dadosGeraisJogador == null){
            return ResponseEntity.internalServerError().build();
        }

        List<Estatistica> estatisticasMvp = estatisticaRepository.buscarEstatisticasOndeJogadorFoiMvp(id);
        Long quantidadeDeVitorias = jogadorRepository.quantidadeDeVitoriasDoJogador(id);

        try {
            byte[] pdf = gerarPdfJogador(jogador, estatisticasMvp, quantidadeDeVitorias, dadosGeraisJogador);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment().filename("relatorio_"+jogador.getNome()+".pdf").build());
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public static byte[] gerarPdfJogador(Jogador jogador, List<Estatistica> estatisticasMvp, Long quantidadeDeVitorias, DadosGeraisJogador dadosGerais) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // Configuração de fontes
        Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 17);
        Font subTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font texto = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font textoNegrito = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font textoDados = new Font(Font.HELVETICA, 12);

        // Cabeçalho
        document.add(new Paragraph("Relatório do Jogador", titulo));
        document.add(new Paragraph(" "));

        // Informações do jogadorNome em duas colunas
        float[] columnWidths = {1, 1};
        PdfPTable infoTable = new PdfPTable(columnWidths);
        infoTable.setWidthPercentage(100);

        addTableCell(infoTable, "Nome", textoNegrito, jogador.getNome(), textoDados);
        addTableCell(infoTable, "Data Nascimento", textoNegrito,
                jogador.getData_nascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), textoDados);
        addTableCell(infoTable, "Altura", textoNegrito, jogador.getAltura() + " cm", textoDados);
        addTableCell(infoTable, "Peso", textoNegrito, jogador.getPeso() + " kg", textoDados);
        addTableCell(infoTable, "Posicao", textoNegrito, jogador.getPosicao().toString(), textoDados);
        addTableCell(infoTable, "Time", textoNegrito, jogador.getClube().getNome(), textoDados);
        addTableCell(infoTable, "MVPs", textoNegrito, String.valueOf(estatisticasMvp.size()), textoDados);
        addTableCell(infoTable, "Vitórias", textoNegrito, String.valueOf(quantidadeDeVitorias), textoDados);

        document.add(infoTable);
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Estatisticas gerais", titulo));



        PdfPTable statsTable1 = new PdfPTable(7);
        statsTable1.setWidthPercentage(100);
        statsTable1.setSpacingBefore(5f);
        statsTable1.setSpacingAfter(5f);

        addStatCell(statsTable1, "Pontos", textoNegrito, dadosGerais.getTotalPontos(), textoDados);
        addStatCell(statsTable1, "Assistências", textoNegrito, dadosGerais.getAssistencias(), textoDados);
        addStatCell(statsTable1, "Faltas", textoNegrito, dadosGerais.getTotalFaltas(), textoDados);
        addStatCell(statsTable1, "Reb. Of.", textoNegrito, dadosGerais.getRebotes_ofensivos(), textoDados);
        addStatCell(statsTable1, "Reb. Def.", textoNegrito, dadosGerais.getRebotes_defensivos(), textoDados);
        addStatCell(statsTable1, "Steals", textoNegrito, dadosGerais.getRoubos_de_bola(), textoDados);
        addStatCell(statsTable1, "Turnovers", textoNegrito, dadosGerais.getTurnovers(), textoDados);

        document.add(statsTable1);
        document.add(new Paragraph(" "));

        // Estatísticas das partidas
        document.add(new Paragraph("Estatísticas das Partidas MVP:", titulo));

        int i = 1;

        for (Estatistica estat : estatisticasMvp) {
            // Título da partidaId
            document.add(new Paragraph("Partida " + i++, subTitulo));

            // Tabela de estatísticas com 4 colunas
            PdfPTable statsTable = new PdfPTable(7);
            statsTable.setWidthPercentage(100);
            statsTable.setSpacingBefore(5f);
            statsTable.setSpacingAfter(5f);

            // Adicionando estatísticas em grupos compactos
            addStatCell(statsTable, "Pontos", textoNegrito, estat.getTotalPontos(), textoDados);
            addStatCell(statsTable, "Assistências", textoNegrito, estat.getAssistencias(), textoDados);
            addStatCell(statsTable, "Faltas", textoNegrito, estat.getTotalFaltas(), textoDados);
            addStatCell(statsTable, "Reb. Of.", textoNegrito, estat.getRebotesOfensivos(), textoDados);
            addStatCell(statsTable, "Reb. Def.", textoNegrito, estat.getRebotesDefensivos(), textoDados);
            addStatCell(statsTable, "Steals", textoNegrito, estat.getRoubosDeBola(), textoDados);
            addStatCell(statsTable, "Turnovers", textoNegrito, estat.getTurnovers(), textoDados);

            document.add(statsTable);
            document.add(new Paragraph(" "));
        }

        document.close();
        return out.toByteArray();
    }

    private static void addTableCell(PdfPTable table, String label, Font labelFont, String value, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(valueCell);
    }

    private static void addStatCell(PdfPTable table, String label, Font labelFont, Object value, Font valueFont) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);

        Paragraph p = new Paragraph();
        p.add(new Chunk(label + "\n", labelFont));
        p.add(new Chunk(value.toString(), valueFont));
        p.setAlignment(Element.ALIGN_CENTER);

        cell.addElement(p);
        table.addCell(cell);
    }
}