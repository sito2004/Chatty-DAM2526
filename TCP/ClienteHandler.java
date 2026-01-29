package TCP;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClienteHandler extends Thread {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private String nombre;
    // Referencia a la lista compartida del servidor
    private List<PrintWriter> clientesConectados;

    public ClienteHandler(Socket socket, List<PrintWriter> clientesConectados) throws IOException {
        this.socket = socket;
        this.clientesConectados = clientesConectados; // Guardamos referencia a la lista

        // Inicializar flujos
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salida = new PrintWriter(socket.getOutputStream(), true);

        // 1. El primer mensaje es el nombre
        nombre = entrada.readLine();

        // 2. Añadir este cliente a la lista compartida y avisar
        synchronized (clientesConectados) { // Sincronizamos para evitar errores de concurrencia
            clientesConectados.add(salida);
        }
        enviarATodos("🔵 " + nombre + " se ha unido al chat");
    }

    @Override
    public void run() {
        String mensaje;
        try {
            // Bucle principal de lectura
            while ((mensaje = entrada.readLine()) != null) {
                enviarATodos("[" + nombre + "]: " + mensaje);
            }
        } catch (IOException e) {
            // Se ignora excepción al cerrar conexión abruptamente
        } finally {
            // Limpieza al salir
            synchronized (clientesConectados) {
                clientesConectados.remove(salida);
            }
            enviarATodos("🔴 " + nombre + " ha salido del chat");
            try {
                socket.close();
            } catch (IOException e) {}
        }
    }

    private void enviarATodos(String mensaje) {
        synchronized (clientesConectados) { // Bloqueamos la lista mientras la recorremos
            for (PrintWriter cliente : clientesConectados) {
                cliente.println(mensaje);
            }
        }
    }
}