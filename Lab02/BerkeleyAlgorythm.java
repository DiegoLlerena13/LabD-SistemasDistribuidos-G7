package Lab02;

import java.util.Random;

class Node extends Thread {
    private int id;
    private int clock; 
    private int offset; 
    private Coordinator coordinator;

    public Node(int id, int clock, Coordinator coordinator) {
        this.id = id;
        this.clock = clock;
        this.coordinator = coordinator;
    }

    public int getClock() {
        return clock;
    }

    public void adjustClock(int offset) {
        this.offset = offset;
        this.clock += offset;
        System.out.println("Nodo " + id + " ajustó su reloj en " + offset + " unidades. Nuevo reloj: " + clock);
    }

    @Override
    public void run() {
        try {
            coordinator.receiveClock(id, clock, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Coordinator {
    private int[] clocks;
    private Node[] nodes; 
    private int totalNodes;
    private int clocksReceived = 0;

    public Coordinator(int totalNodes) {
        this.totalNodes = totalNodes;
        this.clocks = new int[totalNodes];
        this.nodes = new Node[totalNodes];
    }

    public synchronized void receiveClock(int id, int clock, Node node) {
        clocks[id] = clock;
        nodes[id] = node;
        clocksReceived++;
        if (clocksReceived == totalNodes) {
            synchronizeClocks();
        }
    }
    private void synchronizeClocks() {
        int sum = 0;
        for (int clock : clocks) {
            sum += clock;
        }
        int average = sum / totalNodes;
        System.out.println("\nCoordinador calcula hora promedio: " + average + "\n");

        for (int i = 0; i < totalNodes; i++) {
            int offset = average - clocks[i];
            System.out.println("Coordinador envía ajuste de " + offset + " al nodo " + i);
            nodes[i].adjustClock(offset); 
        }
    }
}


public class BerkeleyAlgorythm {
    public static void main(String[] args) {
        int totalNodes = 5;
        Coordinator coordinator = new Coordinator(totalNodes);
        Random random = new Random();

        Node[] nodes = new Node[totalNodes];
        for (int i = 0; i < totalNodes; i++) {
            int clock = 100 + random.nextInt(50); // Relojes entre 100 y 149
            nodes[i] = new Node(i, clock, coordinator);
            System.out.println("Nodo " + i + " tiene reloj inicial: " + clock);
        }

        System.out.println("\n--- Iniciando sincronización ---\n");

        // Iniciar todos los nodos
        for (Node node : nodes) {
            node.start();
        }
    }
}
