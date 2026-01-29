package TCP;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

    // Lista compartida para guardar los flujos de salida de todos los clientes
    // Usamos Collections.synchronizedList para seguridad básica en hilos
    private static List<PrintWriter> clientesConectados = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(5000);
        System.out.println("Servidor TCP iniciado en puerto 5000...");

        while (true) {
            // 1. Esperar conexión
            Socket socketCliente = servidor.accept();

            // 2. Crear y arrancar el Handler pasando el socket y la lista compartida
            ClienteHandler handler = new ClienteHandler(socketCliente, clientesConectados);
            handler.start();
        }
    }
}