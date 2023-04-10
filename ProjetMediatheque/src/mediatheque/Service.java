package mediatheque;

import java.net.Socket;

public abstract class Service implements Runnable {
    private final Socket client;

    public Service(Socket socket) {
        this.client = socket;
    }

    public void lancer() {
        (new Thread(this)).start();
    }

    protected void finalize() throws Throwable {
        this.client.close();
    }

    public Socket getClient() {
        return this.client;
    }
}
