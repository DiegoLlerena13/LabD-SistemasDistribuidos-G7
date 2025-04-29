package Lab02;

import java.util.Random;
import java.text.DecimalFormat;

public class CristianAlgo {
    static class TimeServer {
        public long getTime() {
            return System.currentTimeMillis();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TimeServer server = new TimeServer();
        Random rand = new Random();
        DecimalFormat df = new DecimalFormat("#0.00");

        System.out.println("Cliente sincronizando reloj con el servidor...");

        long t0 = System.currentTimeMillis();
        Thread.sleep(rand.nextInt(100) + 50); // Simula retardo de ida
        long serverTime = server.getTime();   // Hora del servidor
        Thread.sleep(rand.nextInt(100) + 50); // Simula retardo de vuelta
        long t1 = System.currentTimeMillis();

        long rtt = t1 - t0;
        long adjustedTime = serverTime + rtt / 2;

        double t0Sec = t0 / 1000.0;
        double serverTimeSec = serverTime / 1000.0;
        double t1Sec = t1 / 1000.0;
        double rttSec = rtt / 1000.0;
        double adjustedTimeSec = adjustedTime / 1000.0;

        double base = t0Sec;

        System.out.println("Hora local antes de sincronizar: " + df.format(t0Sec - base) + " s");
        System.out.println("Hora del servidor: " + df.format(serverTimeSec - base) + " s");
        System.out.println("Hora local al recibir respuesta: " + df.format(t1Sec - base) + " s");
        System.out.println("RTT estimado: " + df.format(rttSec) + " s");
        System.out.println("Hora ajustada del cliente: " + df.format(adjustedTimeSec - base) + " s");
    }
}
