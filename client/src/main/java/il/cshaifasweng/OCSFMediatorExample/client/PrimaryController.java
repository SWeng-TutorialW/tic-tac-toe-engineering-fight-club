/**
 * Sample Skeleton for 'primary.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import java.net.URL;
import il.cshaifasweng.OCSFMediatorExample.entities.GameMove;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class PrimaryController {
    private SimpleClient client;
    private String currentPlayer;
    private String playerX;
    private String playerO;
    private boolean gameStarted = false;
    private static PrimaryController instance;
    private String[][] board = new String[3][3];
    private String mySymbol;  // This client's assigned symbol: "X" or "O"



    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="BtnCell00"
    private Button BtnCell00; // Value injected by FXMLLoader

    @FXML // fx:id="BtnCell0020"
    private Button BtnCell20; // Value injected by FXMLLoader

    @FXML // fx:id="BtnCell01"
    private Button BtnCell01; // Value injected by FXMLLoader

    @FXML // fx:id="BtnCell02"
    private Button BtnCell02; // Value injected by FXMLLoader

    @FXML // fx:id="BtnCell10"
    private Button BtnCell10; // Value injected by FXMLLoader

    @FXML // fx:id="BtnCell11"
    private Button BtnCell11; // Value injected by FXMLLoader

    @FXML // fx:id="BtnCell12"
    private Button BtnCell12; // Value injected by FXMLLoader

    @FXML // fx:id="BtnCell21"
    private Button BtnCell21; // Value injected by FXMLLoader

    @FXML // fx:id="BtnCell22"
    private Button BtnCell22; // Value injected by FXMLLoader

    @FXML // fx:id="HeaderLabel"
    private Label HeaderLabel; // Value injected by FXMLLoader

    @FXML // fx:id="restartBtn"
    private Button restartBtn; // Value injected by FXMLLoader

    @FXML // fx:id="startBtn"
    private Button startBtn; // Value injected by FXMLLoader

    @FXML
    void pressCell00(ActionEvent event) {
        handleCellPress(BtnCell00, 0, 0);
    }

    @FXML
    void pressCell01(ActionEvent event) {
        handleCellPress(BtnCell01, 0, 1);
    }

    @FXML
    void pressCell02(ActionEvent event) {
        handleCellPress(BtnCell02, 0, 2);
    }

    @FXML
    void pressCell10(ActionEvent event) {
        handleCellPress(BtnCell10, 1, 0);
    }

    @FXML
    void pressCell11(ActionEvent event) {
        handleCellPress(BtnCell11, 1, 1);
    }

    @FXML
    void pressCell12(ActionEvent event) {
        handleCellPress(BtnCell12, 1, 2);
    }


    @FXML
    void pressCell20(ActionEvent event) {
        handleCellPress(BtnCell20, 2, 0);
    }

    @FXML
    void pressCell21(ActionEvent event) {
        handleCellPress(BtnCell21, 2, 1);
    }

    @FXML
    void pressCell22(ActionEvent event) {
        handleCellPress(BtnCell22, 2, 2);
    }

    @FXML
    void pressRestartBtn(ActionEvent event) {
        BtnCell00.setText("");
        BtnCell01.setText("");
        BtnCell02.setText("");
        BtnCell10.setText("");
        BtnCell11.setText("");
        BtnCell12.setText("");
        BtnCell20.setText("");
        BtnCell21.setText("");
        BtnCell22.setText("");

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = "";

        gameStarted = false;
        HeaderLabel.setText("Game reset. Press Start to play.");
    }



    private void handleCellPress(Button cell, int row, int col) {
        if (!gameStarted) {
            HeaderLabel.setText("Please press Start first!");
            return;
        }

        // Already filled cell
        if (!cell.getText().isEmpty()) return;

        // Check if it's the player's turn
        if (!mySymbol.equals(currentPlayer)) {
            HeaderLabel.setText("Wait for your turn!");
            return;
        }

        // If it's your turn, send move to server
        GameMove move = new GameMove(row, col, mySymbol);
        client.sendToServerSafely(move);
    }



    @FXML
    void pressStartBtn(ActionEvent event) {
        try {
            client = new SimpleClient("localhost", 3025); // or use IP if needed
        } catch (Exception e) {
            HeaderLabel.setText("Could not connect to server.");
            e.printStackTrace();
            return;
        }

        Random random = new Random();

        if (random.nextBoolean()) {
            playerX = "Player 1";
            playerO = "Player 2";
            currentPlayer = "X";
            mySymbol = "X";  // This client is X
        } else {
            playerX = "Player 2";
            playerO = "Player 1";
            currentPlayer = "O";
            mySymbol = "O";  // This client is O
        }

        gameStarted = true;
        HeaderLabel.setText(playerX + " is X, " + playerO + " is O. " + currentPlayer + "'s turn.");
    }


    private Button getButtonByCoordinates(int row, int col) {
        if (row == 0 && col == 0) return BtnCell00;
        if (row == 0 && col == 1) return BtnCell01;
        if (row == 0 && col == 2) return BtnCell02;
        if (row == 1 && col == 0) return BtnCell10;
        if (row == 1 && col == 1) return BtnCell11;
        if (row == 1 && col == 2) return BtnCell12;
        if (row == 2 && col == 0) return BtnCell20;
        if (row == 2 && col == 1) return BtnCell21;
        if (row == 2 && col == 2) return BtnCell22;
        return null;
    }

    private boolean checkWin(String player) {
        // Rows and Columns
        for (int i = 0; i < 3; i++) {
            if ((player.equals(board[i][0]) && player.equals(board[i][1]) && player.equals(board[i][2])) ||
                    (player.equals(board[0][i]) && player.equals(board[1][i]) && player.equals(board[2][i]))) {
                return true;
            }
        }

        // Diagonals
        return (player.equals(board[0][0]) && player.equals(board[1][1]) && player.equals(board[2][2])) ||
                (player.equals(board[0][2]) && player.equals(board[1][1]) && player.equals(board[2][0]));
    }

    private boolean isDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == null || board[i][j].isEmpty()) return false;
            }
        }
        return true;
    }


    public static void updateBoard(GameMove move) {
        if (instance == null) return;

        Button target = instance.getButtonByCoordinates(move.getRow(), move.getCol());
        if (target != null && target.getText().isEmpty()) {
            target.setText(move.getPlayer());
            instance.board[move.getRow()][move.getCol()] = move.getPlayer();  // board update

            // Check winner
            if (instance.checkWin(move.getPlayer())) {
                instance.HeaderLabel.setText("Player " + move.getPlayer() + " wins!");
                instance.gameStarted = false;
                return;
            }

            // Check draw
            if (instance.isDraw()) {
                instance.HeaderLabel.setText("It's a draw!");
                instance.gameStarted = false;
                return;
            }

            // Continue
            instance.currentPlayer = move.getPlayer().equals("X") ? "O" : "X";
            instance.HeaderLabel.setText(instance.currentPlayer + "'s turn.");
        }
    }



    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        instance = this;
        assert BtnCell00 != null : "fx:id=\"BtnCell00\" was not injected: check your FXML file 'primary.fxml'.";
        assert BtnCell20 != null : "fx:id=\"BtnCell0020\" was not injected: check your FXML file 'primary.fxml'.";
        assert BtnCell01 != null : "fx:id=\"BtnCell01\" was not injected: check your FXML file 'primary.fxml'.";
        assert BtnCell02 != null : "fx:id=\"BtnCell02\" was not injected: check your FXML file 'primary.fxml'.";
        assert BtnCell10 != null : "fx:id=\"BtnCell10\" was not injected: check your FXML file 'primary.fxml'.";
        assert BtnCell11 != null : "fx:id=\"BtnCell11\" was not injected: check your FXML file 'primary.fxml'.";
        assert BtnCell12 != null : "fx:id=\"BtnCell12\" was not injected: check your FXML file 'primary.fxml'.";
        assert BtnCell21 != null : "fx:id=\"BtnCell21\" was not injected: check your FXML file 'primary.fxml'.";
        assert BtnCell22 != null : "fx:id=\"BtnCell22\" was not injected: check your FXML file 'primary.fxml'.";
        assert HeaderLabel != null : "fx:id=\"HeaderLabel\" was not injected: check your FXML file 'primary.fxml'.";
        assert restartBtn != null : "fx:id=\"restartBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert startBtn != null : "fx:id=\"startBtn\" was not injected: check your FXML file 'primary.fxml'.";

    }

}
