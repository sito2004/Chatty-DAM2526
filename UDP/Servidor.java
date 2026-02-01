package UDP;

import java.net.*;
import java.util.*;

public class Servidor {

    static class Cliente {
        InetAddress direccion;
        int puerto;

        public Cliente(InetAddress direccion, int puerto) {
            this.direccion = direccion;
            this.puerto = puerto;
        }
    }

    public static void main(String[] args) throws Exception {
        DatagramSocket socketServidor = new DatagramSocket(5000);
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        byte[] buffer = new byte[1024];

        System.out.println("Servidor escuchando en puerto 5000...");

        while (true) {
            DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
            socketServidor.receive(paqueteRecibido);

            String mensaje = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
            InetAddress ipRecibida = paqueteRecibido.getAddress();
            int puertoRecibido = paqueteRecibido.getPort();


            boolean existe = false;
            for (Cliente c : listaClientes) {

                if (c.direccion.equals(ipRecibida) && c.puerto == puertoRecibido) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                Cliente nuevo = new Cliente(ipRecibida, puertoRecibido);
                listaClientes.add(nuevo);
                System.out.println("Nuevo cliente conectado desde: " + puertoRecibido);
            }

            String mensajeFinal = "Cliente " + puertoRecibido + ": " + mensaje;
            byte[] datosEnvia = mensajeFinal.getBytes();

            for (Cliente c : listaClientes) {
                DatagramPacket paqueteEnvia = new DatagramPacket(
                        datosEnvia, datosEnvia.length, c.direccion, c.puerto
                );
                socketServidor.send(paqueteEnvia);
            }
        }
    }

}
