import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LeitorFacial {

    private static final String ARQUIVO_MODELO   = "modelo.yml";
    private static final String ARQUIVO_NOMES    = "nomes.txt";
    private static final double LIMIAR_CONFIANCA = 70.0;

    public static void executar(String nivelEsperado, java.util.function.Consumer<String> onSucesso) {
        new Thread(() -> {
            try {
                if (!new File(ARQUIVO_MODELO).exists()) {
                    javax.swing.SwingUtilities.invokeLater(() ->
                        javax.swing.JOptionPane.showMessageDialog(null,
                            "Nenhum modelo encontrado.\nRode o CadastroFacial primeiro.",
                            "Erro", javax.swing.JOptionPane.ERROR_MESSAGE));
                    return;
                }

                // Mapas LOCAIS (evita condição de corrida entre janelas)
                Map<Integer, String> idParaNome  = new HashMap<>();
                Map<Integer, String> idParaNivel = new HashMap<>();
                carregarNomes(idParaNome, idParaNivel);

                LBPHFaceRecognizer recognizer = LBPHFaceRecognizer.create();
                recognizer.read(ARQUIVO_MODELO);

                CascadeClassifier detector = new CascadeClassifier("haarcascade_frontalface_default.xml");
                if (detector.empty()) {
                    System.err.println("Erro: modelo Haar não carregado.");
                    return;
                }

                OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
                grabber.start();

                CanvasFrame canvas = new CanvasFrame("Identificação - " + nivelEsperado);
                canvas.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

                OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
                Mat gray = new Mat();
                RectVector faces = new RectVector();

                String nomeReconhecido = null;

                while (canvas.isVisible() && nomeReconhecido == null) {
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

                        int[] label = new int[1];
                        double[] confidence = new double[1];
                        recognizer.predict(rostoRedim, label, confidence);

                        String texto;
                        if (confidence[0] < LIMIAR_CONFIANCA) {
                            String nome  = idParaNome.getOrDefault(label[0], "ID " + label[0]);
                            String nivel = idParaNivel.getOrDefault(label[0], "?");

                            if (nivel.equals(nivelEsperado)) {
                                texto = nome;
                                nomeReconhecido = nome;
                            } else {
                                texto = nome + " (" + nivel + ") - nível errado";
                            }
                        } else {
                            texto = "Desconhecido (" + (int) confidence[0] + ")";
                        }

                        rectangle(color,
                                new Point(r.x(), r.y()),
                                new Point(r.x() + r.width(), r.y() + r.height()),
                                new Scalar(0, 255, 0, 0));
                        putText(color, texto,
                                new Point(r.x(), r.y() - 10),
                                FONT_HERSHEY_SIMPLEX, 0.9,
                                new Scalar(0, 255, 0, 0), 2, 0, false);
                    }
                    canvas.showImage(converter.convert(color));
                }

                grabber.stop();
                canvas.dispose();

                if (nomeReconhecido != null) {
                    System.out.println("Reconhecido (" + nivelEsperado + "): " + nomeReconhecido);
                    final String nf = nomeReconhecido;
                    javax.swing.SwingUtilities.invokeLater(() -> onSucesso.accept(nf));
                } else {
                    System.out.println("Reconhecimento cancelado.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void carregarNomes(Map<Integer, String> idParaNome,
                                      Map<Integer, String> idParaNivel) {
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.FileReader(ARQUIVO_NOMES))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] p = linha.split(";");
                if (p.length >= 2) {
                    int id = Integer.parseInt(p[0]);
                    idParaNome.put(id, p[1]);
                    if (p.length >= 3) idParaNivel.put(id, p[2]);
                }
            }
        } catch (Exception ex) {
            System.err.println("Aviso: não foi possível carregar nomes.txt");
        }
    }
}