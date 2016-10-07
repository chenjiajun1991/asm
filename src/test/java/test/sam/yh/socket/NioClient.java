package test.sam.yh.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class NioClient {

	private final static int MAX_BUF_SIZE = 1024;
	private InetSocketAddress serverAddr;
	private int clientCount;

	private static int loopcount = 0;
	
	Set<Integer> linkedSet = new HashSet<Integer>();

	public NioClient(String ip, int port, int clientCount) {
		this.clientCount = clientCount;
		this.serverAddr = new InetSocketAddress(ip, port);
	}

	private void sendMessageToSrv(SocketChannel sockChnl, int clientNo,
			int index) {
		// send data to server...
		/*
		 * ByteBuffer sendBuf = ByteBuffer.allocate(MAX_BUF_SIZE); String
		 * sendText = "Client " + clientNo + " say " + index + "\r\n";
		 * sendBuf.put(sendText.getBytes()); sendBuf.flip();
		 * sockChnl.write(sendBuf); System.out.println(sendText);
		 */

		// ByteBuffer sendBuf = ByteBuffer.allocate(512);
		try {
		long imei = 8000000000000000L + clientNo;
		String content = "imsi=9460066071037714&phonenumber=0&longitude=121.000440&latitude=29.005500&temperature=379&voltage=655.0&imei="
				+ String.valueOf(imei) + "\r\n";
		ByteBuffer sendBuf = ByteBuffer.allocate(MAX_BUF_SIZE);

		sendBuf.put(content.getBytes());

		sendBuf.flip();
		sockChnl.write(sendBuf);

		// imsi=9460066071037714&phonenumber=0&longitude=0.000000&latitude=0.000000&temperature=379&voltage=655.0&imei=511041001002900
		// String out =
		// String.format("client: %d send message, index: %d, a: %d, b: %d",
		// clientNo, index, clientNo, index);
		System.out.println(content);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void recvMessage(SocketChannel sockChnl, int clientNo) {
		/*
		 * ByteBuffer recvBuf = ByteBuffer.allocate(MAX_BUF_SIZE); int bytesRead
		 * = sockChnl.read(recvBuf); while (bytesRead > 0) { recvBuf.flip(); //
		 * write mode to read mode, position to 0, // limit to position String
		 * recvText = new String(recvBuf.array(), 0, bytesRead);
		 * recvBuf.clear(); // clear buffer content, read mode to write mode,
		 * position to 0, limit to capacity System.out.println("Client " +
		 * clientNo + " receive: " + recvText); bytesRead =
		 * sockChnl.read(recvBuf); }
		 */
		try {
			ByteBuffer recvBuf = ByteBuffer.allocate(MAX_BUF_SIZE);
			int bytesRead = sockChnl.read(recvBuf);
			while (bytesRead > 0) {
				recvBuf.flip(); // write mode to read mode, position to 0, //
								// limit
								// to position

				String s = getString(recvBuf);
				recvBuf.clear(); // clear buffer content, read mode to write
									// mode,
									// position to 0, limit to capacity
				System.out.println(s);
				bytesRead = sockChnl.read(recvBuf);

			}
		} catch (Exception e) {
			linkedSet.remove(clientNo);
			System.out.println(linkedSet.size());
			e.printStackTrace();
		}
	}

	public void startNioClient() throws Exception {
		Selector selector = Selector.open();

		for (int i = 0; i < clientCount; i++) {
			estConn(selector, i);
		}

		while (true) {

			int readyChannels = selector.select();
			if (0 == readyChannels) {
				continue;
			}

			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			for (SelectionKey sk : selectionKeys) {
				Map clientInfo = (Map) sk.attachment();
				int clientNo = (Integer) clientInfo.get("no");
				SocketChannel socketchnl = (SocketChannel) sk.channel();

				try {
				if (sk.isConnectable()) {
					while (!socketchnl.finishConnect()) {
						Thread.sleep(5);
					}
					if (socketchnl.isConnected()) {
						System.out.println("connect is finish...");
						System.out.println("xxxxxxxxxx" + loopcount);
						// send data to server...
						sendMessageToSrv(socketchnl, clientNo, -1);
						sk.interestOps(SelectionKey.OP_READ);

					}
				} else if (sk.isReadable()) {
					// read data from server...
					recvMessage(socketchnl, clientNo);

					// send data to server...
					TimeUnit.MILLISECONDS.sleep(2);
					int index = (Integer) clientInfo.get("index");
					index += 1;
					sendMessageToSrv(socketchnl, clientNo, index);

					loopcount++;
					clientInfo.put("index", index);
				}
				
				}catch(Exception e) {
					linkedSet.remove(clientNo);
					System.out.println(linkedSet.size());
					e.printStackTrace();
				}
			}
			selectionKeys.clear();
		}
	}

	private void estConn(Selector selector, int i) throws IOException,
			ClosedChannelException {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		Map<String, Integer> clientInfo = new HashMap<String, Integer>();
		clientInfo.put("no", i);
		clientInfo.put("index", 0);
		linkedSet.add(i);
		socketChannel.register(selector, SelectionKey.OP_CONNECT,
				clientInfo);
		socketChannel.connect(this.serverAddr);
	}

	public int getClientCount() {
		return clientCount;
	}

	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}

	public static String getString(ByteBuffer buffer) {
		Charset charset = null;
		CharsetDecoder decoder = null;
		CharBuffer charBuffer = null;
		try {
			charset = Charset.forName("UTF-8");
			decoder = charset.newDecoder();
			// charBuffer = decoder.decode(buffer);//用这个的话，只能输出来一次结果，第二次显示为空
			charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
			return charBuffer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

}
