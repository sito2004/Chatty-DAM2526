package TCP;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

    private static List<PrintWriter> clientesConectados = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(5000);
        System.out.println("Servidor TCP iniciado en puerto 5000...");

        while (true) {

            Socket socketCliente = servidor.accept();

            ClienteHandler handler = new ClienteHandler(socketCliente, clientesConectados);
            handler.start();
        }
    }

}
