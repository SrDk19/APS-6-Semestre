import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

import java.io.File;
import java.util.*;

public class CadastroFacial {

    private static final String ARQUIVO_MODELO = "modelo.yml";
    private static final String ARQUIVO_NOMES  = "nomes.txt";
    private static final String PASTA_FOTOS    = "fotos";
    private static final int FOTOS_POR_PESSOA  = 20; // quantas fotos capturar

    public static void main(String[] args) throws Exception {
        // Pede o nome da pessoa (pode ser via terminal ou via JOptionPane)
        String nome = javax.swing.JOptionPane.showInputDialog(
                "Digite o nome da pessoa a ser cadastrada:");
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Cadastro cancelado.");
            return;
        }
        nome = nome.trim().replaceAll("[^a-zA-Z0-9_-]", "_");

        System.out.println("Cadastrando: " + nome);
        System.out.println("Capture " + FOTOS_POR_PESSOA + " fotos. Olhe para a câmera.");

        // 1. Cria a pasta da pessoa
        File pastaPessoa = new File(PASTA_FOTOS, nome);
        pastaPessoa.mkdirs();

        // 2. Abre câmera e detector
        CascadeClassifier detector = new CascadeClassifier("haarcascade_frontalface_default.xml");
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.start();

        CanvasFrame canvas = new CanvasFrame("Cadastro - " + nome);
        canvas.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        Mat gray = new Mat();
        RectVector faces = new RectVector();

        int capturadas = 0;
        int tentativas = 0;

        // 3. Loop de captura
        while (canvas.isVisible() && capturadas < FOTOS_POR_PESSOA && tentativas < 300) {
            Frame frame = grabber.grab();
            if (frame == null) break;

            Mat color = converter.convert(frame);
            cvtColor(color, gray, COLOR_BGR2GRAY);
            detector.detectMultiScale(gray, faces, 1.1, 3, 0,
                    new Size(80, 80), new Size());

            for (long i = 0; i < faces.size(); i++) {
                Rect r = faces.get(i);

                // Salva a foto recortada do rosto
                Mat rosto = new Mat(gray, r);
                Mat rostoRedim = new Mat();
                resize(rosto, rostoRedim, new Size(200, 200));

                File saida = new File(pastaPessoa,
                        (capturadas + 1) + ".jpg");
                opencv_imgcodecs.imwrite(saida.getAbsolutePath(), rostoRedim);
                capturadas++;
                System.out.println("Foto " + capturadas + "/" + FOTOS_POR_PESSOA);

                // Desenha retângulo e contador
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

            // Pequena pausa para não salvar a mesma pose 20x seguidas
            Thread.sleep(150);
        }

        grabber.stop();
        canvas.dispose();

        if (capturadas < FOTOS_POR_PESSOA) {
            System.out.println("Captura incompleta (" + capturadas + " fotos). Abortando treino.");
            return;
        }

        // 4. Treina o modelo com TODAS as pastas em fotos/
        System.out.println("Treinando modelo...");
        treinarModelo();

        javax.swing.JOptionPane.showMessageDialog(null,
                "Cadastro de '" + nome + "' concluído!\nModelo atualizado.",
                "Sucesso", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    // Treina usando todas as pastas existentes em fotos/
    private static void treinarModelo() {
        CascadeClassifier detector = new CascadeClassifier("haarcascade_frontalface_default.xml");
        LBPHFaceRecognizer recognizer = LBPHFaceRecognizer.create();

        Map<Integer, String> idParaNome = new LinkedHashMap<>();
        List<Mat> rostos = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();

        File raiz = new File(PASTA_FOTOS);
        File[] pastas = raiz.listFiles(File::isDirectory);
        if (pastas == null) return;

        int id = 0;
        for (File pasta : pastas) {
            idParaNome.put(id, pasta.getName());
            System.out.println("  " + pasta.getName() + " -> ID " + id);

            File[] fotos = pasta.listFiles((d, n) -> {
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

                // Já estão recortadas e redimensionadas (200x200), mas garante
                Mat padrao = new Mat();
                resize(gray, padrao, new Size(200, 200));
                rostos.add(padrao);
                labels.add(id);
            }
            id++;
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

        // Salva o mapeamento ID->nome
        try (java.io.PrintWriter pw = new java.io.PrintWriter(ARQUIVO_NOMES)) {
            for (Map.Entry<Integer, String> e : idParaNome.entrySet()) {
                pw.println(e.getKey() + ";" + e.getValue());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Modelo salvo em " + ARQUIVO_MODELO);
        System.out.println("Nomes salvos em " + ARQUIVO_NOMES);
    }
}