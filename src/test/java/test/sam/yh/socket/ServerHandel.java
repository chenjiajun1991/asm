package test.sam.yh.socket;

import java.io.BufferedInputStream;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

public class ServerHandel implements Runnable {

    private Socket socket;

    public ServerHandel(Socket socket) {
        super();
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(socket.getInputStream());
            while (true) {

            }
        } catch (Exception e) {

        } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(socket);
        }
    }

}
