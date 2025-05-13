package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.GameMove;
import il.cshaifasweng.OCSFMediatorExample.entities.RoleAssignment;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private final List<ConnectionToClient> pendingClients = new ArrayList<>();


	public SimpleServer(int port) {
		super(port);
		
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof GameMove move) {
			System.out.println("Received move: " + move);

			try {
				sendToAllClients(move);  // Broadcast the move
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void clientConnected(ConnectionToClient client) {
		// Reject 3rd or later client
		if (pendingClients.size() >= 2) {
			System.out.println("Rejected extra client: game already has 2 players.");
			try {
				client.sendToClient(new Warning("Game already has 2 players."));
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		// Add new client to list
		pendingClients.add(client);
		System.out.println("Client connected. Waiting for opponent...");

		// If two players are now connected, assign roles randomly
		if (pendingClients.size() == 2) {
			Collections.shuffle(pendingClients); // Randomize who gets X and O

			try {
				pendingClients.get(0).sendToClient(new RoleAssignment("X"));
				pendingClients.get(1).sendToClient(new RoleAssignment("O"));
				System.out.println("Roles assigned: X and O");
			} catch (IOException e) {
				e.printStackTrace();
			}

			pendingClients.clear(); // Reset for next game if you allow that
		}
	}

	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		pendingClients.remove(client);
		System.out.println("Client disconnected.");

		if (pendingClients.isEmpty()) {
			System.out.println("All clients disconnected. Closing server...");
			try {
				close(); // This shuts down the server
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public void sendToAllClients(String message) {
		try {
			for (SubscribedClient subscribedClient : SubscribersList) {
				subscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
