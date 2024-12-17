package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class Game {
    private static Game game = null;

    @FXML
    private Button button00, button01, button02, button10, button11, button12, button20, button21, button22;

    @FXML
    private Label statusLabel;

    private String[][] board = new String[3][3];

    public Game() {
        EventBus.getDefault().register(this);
    }

    @FXML
    private void handleButtonClick(javafx.event.ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();

        try {
            String msg = buttonId.substring(6, 8);
            SimpleClient.getClient().sendToServer(msg);
            System.out.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void handleMessage(Object msg) {
        Platform.runLater(() -> {
            if (msg instanceof String) {
                String message = msg.toString();

                if (message.equals("StartGame")) {
                    statusLabel.setText("Game Started!");
                } else if (message.equals("X") || message.equals("O")) {
                    statusLabel.setText("Player " + message + "'s Turn");
                } else if (message.startsWith("Player") || message.contains("Draw")) {
                    statusLabel.setText(message);
                }
            } else if (msg instanceof Object[]) { // Board update
                Object[] update = (Object[]) msg;
                int row = (int) update[0];
                int col = (int) update[1];
                String operation = (String) update[2];
                setGame(row, col, operation);
            }
        });
    }

    public static Game getGame() {
        if (game == null) {
            game = new Game();
        }
        return game;
    }

    private void setGame(int row, int col, String operation) {
        board[row][col] = operation;
        if (row == 0 && col == 0) button00.setText(operation);
        else if (row == 0 && col == 1) button01.setText(operation);
        else if (row == 0 && col == 2) button02.setText(operation);
        else if (row == 1 && col == 0) button10.setText(operation);
        else if (row == 1 && col == 1) button11.setText(operation);
        else if (row == 1 && col == 2) button12.setText(operation);
        else if (row == 2 && col == 0) button20.setText(operation);
        else if (row == 2 && col == 1) button21.setText(operation);
        else if (row == 2 && col == 2) button22.setText(operation);
    }
}
