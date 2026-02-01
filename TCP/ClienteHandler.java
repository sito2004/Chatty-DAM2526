package TCP;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClienteHandler extends Thread {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private String nombre;
    private List<PrintWriter> clientesConectados;

    public ClienteHandler(Socket socket, List<PrintWriter> clientesConectados) throws IOException {
        this.socket = socket;
        this.clientesConectados = clientesConectados;

       
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salida = new PrintWriter(socket.getOutputStream(), true);

       
        nombre = entrada.readLine();

        
        synchronized (clientesConectados) {
            clientesConectados.add(salida);
        }
        enviarATodos("🔵 " + nombre + " se ha unido al chat");
    }

    @Override
    public void run() {
        String mensaje;
        try {
           
            while ((mensaje = entrada.readLine()) != null) {
                enviarATodos("[" + nombre + "]: " + mensaje);
            }
        } catch (IOException e) {
            
        } finally {
           
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
        synchronized (clientesConectados) {
            for (PrintWriter cliente : clientesConectados) {
                cliente.println(mensaje);
            }
        }
    }

}
