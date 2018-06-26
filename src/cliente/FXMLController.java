/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.DataOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author hvill
 */
public class FXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void x(ActionEvent event) {
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

}
