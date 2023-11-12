package es.florida.multifil.avaluable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Manufacture {
    private List<String> llistaPeces = new ArrayList<>();

    /**
     * Inicia la producció de peces segons la comanda.
     *
     * @param comanda Mapa que conté els tipus de peces com a clau i la quantitat com a valor.
     */
    public void iniciarProduccio(Map<String, Integer> comanda) {
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        for (Map.Entry<String, Integer> entrada : comanda.entrySet()) {
            String tipus = entrada.getKey();
            int quantitat = entrada.getValue();

            for (int i = 0; i < quantitat; i++) {
                executorService.submit(() -> {
                    processarFabricacio(obtenirTempsFabricacio(tipus));
                    String peca = tipus + "_" + obtenirTimestampActual();
                    llistaPeces.add(peca);
                    System.out.println(peca);
                });
            }
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            escriureAfitxer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int obtenirTempsFabricacio(String tipus) {
        switch (tipus) {
            case "I":
                return 1000;
            case "O":
                return 2000;
            case "T":
                return 3000;
            case "J":
            case "L":
                return 4000;
            case "S":
            case "Z":
                return 500;
            default:
                return 0;
        }
    }

    private void processarFabricacio(int tempsFabricacio) {
        long tempsInici = System.currentTimeMillis();
        long tempsFinal = tempsInici + tempsFabricacio;

        while (System.currentTimeMillis() < tempsFinal) {
            // Realitzar iteracions addicionals per a consumir processador (simula ocupació de màquina)
        }
    }

    private String obtenirTimestampActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date());
    }

    private void escriureAfitxer() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("LOG_" + obtenirTimestampActual() + ".txt"))) {
            for (String peca : llistaPeces) {
                writer.write(peca);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
