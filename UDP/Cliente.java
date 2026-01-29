package UDP;

import java.net.*;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress ipServidor = InetAddress.getByName("localhost");
        int puertoServidor = 5000;
        Scanner teclado = new Scanner(System.in);

        Thread hiloEscucha = new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket recibido = new DatagramPacket(buffer, buffer.length);
                    socket.receive(recibido);
                    String mensaje = new String(recibido.getData(), 0, recibido.getLength());
                    System.out.println(mensaje);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        hiloEscucha.start();

        System.out.println("Escribe tus mensajes (pulsa Enter para enviar):");
        while (true) {
            String texto = teclado.nextLine();
            byte[] datos = texto.getBytes();
            DatagramPacket paquete = new DatagramPacket(datos, datos.length, ipServidor, puertoServidor);
            socket.send(paquete);
        }
    }
}