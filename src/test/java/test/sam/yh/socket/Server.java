package test.sam.yh.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

public class Server {

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(50080);
            while (true) {
                Socket socket = server.accept();
                ServerHandel handel = new ServerHandel(socket);
                Thread thread = new Thread(handel);
                thread.start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(server);
        }

    }
}
