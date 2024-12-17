package il.cshaifasweng.OCSFMediatorExample.client;

import org.greenrobot.eventbus.EventBus;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

import java.io.IOException;

public class SimpleClient extends AbstractClient {
	public static String ip = "127.0.0.1";
	public static int port = 3000;
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
		Game.getGame();
	}

	@Override
	protected void handleMessageFromServer(Object msg) throws IOException {
		System.out.println(msg.toString());
		//test
		if (msg instanceof Warning) {
			EventBus.getDefault().post(msg); // Post the Warning directly
		} else if (msg.toString().startsWith("Start")) {
			SecondaryController.switchTogame();
			EventBus.getDefault().post("StartGame");
		} else if (msg.toString().startsWith("Player") || msg.toString().equals("X") || msg.toString().equals("O")) {
			EventBus.getDefault().post(msg); // Post status updates directly
		} else {
			int row = Character.getNumericValue(msg.toString().charAt(0));
			int col = Character.getNumericValue(msg.toString().charAt(1));
			String operation = msg.toString().substring(2);
			EventBus.getDefault().post(new Object[] { row, col, operation }); // Send board updates
		}
	}

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient(ip, port);
		}
		return client;
	}
}
