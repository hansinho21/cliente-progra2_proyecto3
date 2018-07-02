/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hvill
 */
public class MenuController implements Initializable {

    private boolean isServer = true;
//    private NetworkConnection connection = isServer ? createSetver() : createClient();

    private Service<Void> backgroundThread;

    @FXML
    private Button button;
    @FXML
    private Label label;
    @FXML
    private TextArea textArea;
    @FXML
    private TextField textField;
    @FXML
    private AnchorPane anchorPaneGame;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.textArea.setEditable(false);
        this.anchorPaneGame.setVisible(false);
    }

    @FXML
    private void x(ActionEvent event) {
        this.button.setDisable(true);
        backgroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        int cont = 1;
                        while (true) {
                            updateMessage("Establishing connection");
                            
                            String dirWeb = "localhost";
                            int puerto = 2004;

                            try {
                                Socket s = new Socket(dirWeb, puerto);
                                if (s.isConnected()) {
                                    System.out.println("Conexión establecida con la dirección: " + dirWeb + " a travéz del puerto: " + puerto);
                                    updateMessage("Connected!");
                                    anchorPaneGame.setVisible(true);
                                    break;
                                }
                            } catch (Exception e) {
                                System.err.println("No se pudo establecer conexión con: " + dirWeb + " a travez del puerto: " + puerto);
                                if (cont == 1) {
                                    updateMessage("Establishing connection.");
                                    cont++;
                                    Thread.sleep(750);
                                }
                                if (cont == 2) {
                                    updateMessage("Establishing connection..");
                                    cont++;
                                    Thread.sleep(750);
                                }
                                if (cont == 3) {
                                    updateMessage("Establishing connection...");
                                    cont = 1;
                                    Thread.sleep(750);
                                }
                            }

                        }
                        return null;
                    }
                };
            }
        };

        backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                label.textProperty().unbind();
            }
        });

        this.label.textProperty().bind(backgroundThread.messageProperty());

        backgroundThread.restart();

    }

    private void connection() {
        try {
            Socket soc = new Socket("localhost", 2004);
            DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
            dout.writeUTF("Hello");
            dout.flush();
            dout.close();
            soc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sendMessage(ActionEvent event) {
        String message = "";
        message += textField.getText();
        textField.clear();

        textArea.appendText(message + "\n");
    }

}
