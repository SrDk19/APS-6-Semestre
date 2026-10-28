import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import java.io.File;
import java.util.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

public class CadastroFacial {

    private static final String ARQUIVO_MODELO = "modelo.yml";
    private static final String ARQUIVO_NOMES  = "nomes.txt";
    private static final String PASTA_FOTOS    = "fotos";
    private static final int FOTOS_POR_PESSOA  = 4;

    /**
     * Executa o cadastro. Aceita qualquer variação de nível:
     * "Nível 2", "nivel2", "NIVEL 2", "nivel 2", etc.
     * Internamente converte para "nivel2" ou "nivel3".
     */
    public static void executar(String nivel) {
        String nivelCanonico = normalizarNivel(nivel);

        if (nivelCanonico == null) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Nível inválido: '" + nivel + "'\n" +
                    "Esperado: 'Nível 2' ou 'Nível 3'.",
                    "Erro", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nome = javax.swing.JOptionPane.showInputDialog(
                "Digite o nome da pessoa a ser cadastrada no " + nivelCanonico + ":");
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Cadastro cancelado.");
            return;
        }
        final String nomeFinal = nome.trim().replaceAll("[^a-zA-Z0-9_-]", "_");

        new Thread(() -> {
            try {
                capturarETreinar(nomeFinal, nivelCanonico);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Normaliza o nível recebido para "nivel2" ou "nivel3".
     * Retorna null se não for reconhecido.
     */
    private static String normalizarNivel(String nivel) {
        if (nivel == null) return null;

        // Minúsculo, sem espaços, sem acentos comuns
        String s = nivel.trim().toLowerCase()
                .replace("í", "i")
                .replace("é", "e")
                .replace(" ", "");

        // Remove o "nível"/"nivel" do início, se houver
        s = s.replace("nivel", "");  // "nivel2" -> "2"; "nivel 2" -> "2" (já removeu espaço)

        if (s.equals("2")) return "Nível 2";
        if (s.equals("3")) return "Nível 3";
        return null;
    }

    // Mantém compatibilidade
    public static void executar() {
        executar("Nivel_2");
    }

    public static void main(String[] args) {
        executar("Nivel_2");
    }

    private static void capturarETreinar(String nome, String nivel) throws Exception {
        System.out.println("Cadastrando '" + nome + "' no " + nivel);

        File pastaPessoa = new File(PASTA_FOTOS + File.separator + nivel, nome);
        pastaPessoa.mkdirs();

        File xml = new File("haarcascade_frontalface_default.xml");
        if (!xml.exists()) {
        javax.swing.SwingUtilities.invokeLater(() ->
        javax.swing.JOptionPane.showMessageDialog(null,
            "Arquivo não encontrado:\n" + xml.getAbsolutePath(),
            "Erro", javax.swing.JOptionPane.ERROR_MESSAGE));
        return;
}
        CascadeClassifier detector = new CascadeClassifier(xml.getAbsolutePath());
        if (detector.empty()) {
        System.err.println("Erro: modelo Haar não pôde ser carregado mesmo existindo.");
        return;
}

        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(1);

        grabber.setFormat("dshow");   // importante no Windows

        grabber.start();


        CanvasFrame canvas = new CanvasFrame("Cadastro " + nivel + " - " + nome);
        canvas.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        Mat gray = new Mat();
        RectVector faces = new RectVector();

        int capturadas = 0;
        int tentativas = 0;

        while (canvas.isVisible() && capturadas < FOTOS_POR_PESSOA && tentativas < 300) {
            Frame frame = grabber.grab();
            if (frame == null) break;

            Mat color = converter.convert(frame);
            cvtColor(color, gray, COLOR_BGR2GRAY);
            detector.detectMultiScale(gray, faces, 1.1, 3, 0,
                    new Size(80, 80), new Size());

            for (long i = 0; i < faces.size(); i++) {
                Rect r = faces.get(i);
                Mat rosto = new Mat(gray, r);
                Mat rostoRedim = new Mat();
                resize(rosto, rostoRedim, new Size(200, 200));

                File saida = new File(pastaPessoa, (capturadas + 1) + ".jpg");
                opencv_imgcodecs.imwrite(saida.getAbsolutePath(), rostoRedim);
                capturadas++;
                System.out.println("Foto " + capturadas + "/" + FOTOS_POR_PESSOA);

                rectangle(color,
                        new Point(r.x(), r.y()),
                        new Point(r.x() + r.width(), r.y() + r.height()),
                        new Scalar(0, 255, 0, 0));
                putText(color, capturadas + "/" + FOTOS_POR_PESSOA,
                        new Point(20, 40),
                        FONT_HERSHEY_SIMPLEX, 1.0,
                        new Scalar(0, 255, 0, 0), 2, 0, false);
            }
            canvas.showImage(converter.convert(color));
            tentativas++;
            Thread.sleep(150);
        }

        grabber.stop();
        canvas.dispose();

        if (capturadas < FOTOS_POR_PESSOA) {
            System.out.println("Captura incompleta (" + capturadas + " fotos). Abortando treino.");
            return;
        }

        System.out.println("Treinando modelo...");
        treinarModelo();

        javax.swing.SwingUtilities.invokeLater(() ->
            javax.swing.JOptionPane.showMessageDialog(null,
                "Cadastro de '" + nome + "' (" + nivel + ") concluído!\nModelo atualizado.",
                "Sucesso", javax.swing.JOptionPane.INFORMATION_MESSAGE));
    }

    private static void treinarModelo() {
        LBPHFaceRecognizer recognizer = LBPHFaceRecognizer.create();

        Map<Integer, String[]> idParaInfo = new LinkedHashMap<>();
        List<Mat> rostos = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();

        File raiz = new File(PASTA_FOTOS);
        File[] pastasNivel = raiz.listFiles(File::isDirectory);
        if (pastasNivel == null) {
            System.err.println("Pasta 'fotos' inexistente ou vazia.");
            return;
        }

        int id = 0;
        for (File pastaNivel : pastasNivel) {
            String nivel = pastaNivel.getName();
            File[] pastasPessoas = pastaNivel.listFiles(File::isDirectory);
            if (pastasPessoas == null) continue;

            for (File pastaPessoa : pastasPessoas) {
                String nome = pastaPessoa.getName();
                idParaInfo.put(id, new String[]{nome, nivel});
                System.out.println("  " + nivel + "/" + nome + " -> ID " + id);

                File[] fotos = pastaPessoa.listFiles((d, n) -> {
                    String s = n.toLowerCase();
                    return s.endsWith(".jpg") || s.endsWith(".png") || s.endsWith(".jpeg");
                });
                if (fotos == null) { id++; continue; }

                for (File foto : fotos) {
                    Mat img = opencv_imgcodecs.imread(foto.getAbsolutePath());
                    if (img.empty()) continue;

                    Mat gray = new Mat();
                    if (img.channels() > 1) cvtColor(img, gray, COLOR_BGR2GRAY);
                    else gray = img;

                    Mat padrao = new Mat();
                    resize(gray, padrao, new Size(200, 200));
                    rostos.add(padrao);
                    labels.add(id);
                }
                id++;
            }
        }

        if (rostos.isEmpty()) {
            System.err.println("Nenhuma foto encontrada.");
            return;
        }

        MatVector rostosVector = new MatVector(rostos.toArray(new Mat[0]));
        Mat labelsMat = new Mat(labels.size(), 1,
                org.bytedeco.opencv.global.opencv_core.CV_32SC1);
        for (int i = 0; i < labels.size(); i++) {
            labelsMat.ptr(i).putInt(labels.get(i));
        }

        recognizer.train(rostosVector, labelsMat);
        recognizer.save(ARQUIVO_MODELO);

        try (java.io.PrintWriter pw = new java.io.PrintWriter(ARQUIVO_NOMES)) {
            for (Map.Entry<Integer, String[]> e : idParaInfo.entrySet()) {
                pw.println(e.getKey() + ";" + e.getValue()[0] + ";" + e.getValue()[1]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Modelo salvo em " + ARQUIVO_MODELO);
        System.out.println("Nomes salvos em " + ARQUIVO_NOMES);
    }
}