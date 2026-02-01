package TCP;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 5000;

        try {

            Socket socket = new Socket(host, puerto);
            System.out.println("Conectado al servidor de chat.");

            BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salidaServidor = new PrintWriter(socket.getOutputStream(), true);

            Scanner teclado = new Scanner(System.in);

            System.out.print("Introduce tu nombre de usuario: ");
            String nombre = teclado.nextLine();
            salidaServidor.println(nombre);


            Thread hiloEscucha = new Thread(() -> {
                String mensajeEntrante;
                try {
                    while ((mensajeEntrante = entradaServidor.readLine()) != null) {
                        System.out.println(mensajeEntrante);
                    }
                } catch (IOException e) {
                    System.out.println("Se ha perdido la conexión con el servidor.");
                }
            });
            hiloEscucha.start();


            while (true) {
                String mensajeUsuario = teclado.nextLine();

               
                if (mensajeUsuario.equalsIgnoreCase("/salir")) {
                    break;
                }

                salidaServidor.println(mensajeUsuario);
            }

            socket.close();
            teclado.close();

        } catch (IOException e) {
            System.out.println("Error en el cliente: " + e.getMessage());
        }
    }

}
