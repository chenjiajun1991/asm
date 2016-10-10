package test.sam.yh.socket;


public class NettyClientTest {

	public static void main(String[] args) {
		System.out.println("clients start..............");
		NioClient client = new NioClient("114.55.33.1", 60080, 10000);
		try {
			client.startNioClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
