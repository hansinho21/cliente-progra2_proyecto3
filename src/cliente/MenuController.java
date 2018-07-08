/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
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
import sun.misc.IOUtils;

/**
 * FXML Controller class
 *
 * @author hvill
 */
public class MenuController implements Initializable {

    private Socket cliente;
    private BufferedReader entrada, teclado;
    private PrintStream salida;

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
    @FXML
    private TextField textFieldIp;
    @FXML
    private TextField textFieldPuerto;

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
                            updateMessage("Estableciendo conexión");

//                            String dirWeb = textFieldIp.getText();
//                            int puerto = Integer.parseInt(textFieldPuerto.getText());
                            String dirWeb = "localhost";
                            int puerto = 2004;

                            try {
                                cliente = new Socket(dirWeb, puerto);
                                if (cliente.isConnected()) {
                                    System.out.println("Conexión establecida con la dirección: " + dirWeb + " a travéz del puerto: " + puerto);
                                    updateMessage("Conectado!");
                                    anchorPaneGame.setVisible(true);
                                    break;
                                }
                            } catch (Exception e) {
                                System.err.println("No se pudo establecer conexión con: " + dirWeb + " a travez del puerto: " + puerto);
                                if (cont == 1) {
                                    updateMessage("Estableciendo conexión.");
                                    cont++;
                                    Thread.sleep(750);
                                }
                                if (cont == 2) {
                                    updateMessage("Estableciendo conexión..");
                                    cont++;
                                    Thread.sleep(750);
                                }
                                if (cont == 3) {
                                    updateMessage("Estableciendo conexión...");
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

    public void enviar(Socket socket, String mensaje) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write(mensaje);
        bufferedWriter.flush();
    }

    @FXML
    private void sendMessage(ActionEvent event) throws IOException {
        if (!textField.getText().equals("")) {
            String message = "";
            message += textField.getText();
            enviar(cliente, textField.getText());

            textField.clear();

            textArea.appendText(message + "\n");
        }
    }

}
