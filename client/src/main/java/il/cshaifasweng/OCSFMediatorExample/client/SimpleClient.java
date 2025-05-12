package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.GameMove;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.RoleAssignment;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.scene.control.Alert;


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

		} else if (msg instanceof RoleAssignment role) {
			System.out.println("Got role assignment from server: " + role.getSymbol());

			Platform.runLater(() -> {
				PrimaryController.getInstance().setMySymbol(role.getSymbol());
			});

		} else if (msg instanceof Warning warning) {
			System.out.println("Got warning from server: " + warning.getMessage());

			Platform.runLater(() -> {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Game Full");
				alert.setHeaderText(null);
				alert.setContentText(warning.getMessage());
				alert.showAndWait();
				System.exit(0);  // Exit client if too many players
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
