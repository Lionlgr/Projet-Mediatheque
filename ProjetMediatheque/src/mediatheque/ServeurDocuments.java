package mediatheque;
import java.lang.reflect.InvocationTargetException;

import java.io.*;
import java.net.*;

public class ServeurDocuments implements Runnable {
	private ServerSocket listen_socket;
	private Class<? extends Service> Service;

	// Cree un serveur TCP - objet de la classe ServerSocket
	public ServeurDocuments(Class<? extends Service> Service, int port) throws IOException {
		this.Service = Service;
		listen_socket = new ServerSocket(port);
	}

	// Le serveur ecoute et accepte les connexions.
	// pour chaque connexion, il cree le service demandé,
	// qui va la traiter, et le lance
	public void run() {
        try {
            while(true) {
                ((Service)this.Service.getConstructor(Socket.class).newInstance(this.listen_socket.accept())).lancer();
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | IOException var4) {
            try {
                this.listen_socket.close();
            } catch (IOException var3) {
            }

            System.err.println("Pb sur le port d'�coute :" + var4);
        }
    }

    protected void finalize() throws Throwable {
        try {
            this.listen_socket.close();
        } catch (IOException var2) {
        }

    }
}
