package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.GameMove;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import javafx.application.Platform;


public class SimpleClient extends AbstractClient {

	public SimpleClient(String host, int port) throws Exception {
		super(host, port);
		openConnection();
	}

	public static SimpleClient getClient() {
		return null;
	}


	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg instanceof GameMove move) {
			System.out.println("Got move from server: " + move);

			Platform.runLater(() -> {
				PrimaryController.updateBoard(move);
			});
		}
	}


	public void sendToServerSafely(Object msg) {
		try {
			sendToServer(msg);
		} catch (Exception e) {
			System.out.println("Failed to send message: " + e.getMessage());
		}
	}
}
