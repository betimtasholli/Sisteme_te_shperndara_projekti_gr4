package sample;

import Connectivity.ConnectionClass;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class EditVeturaController implements Initializable {
    ConnectionClass connectionClass = new ConnectionClass();
    Connection connection = connectionClass.getConnection();
    RentACarController obj = new RentACarController();

    @FXML
    public TextField txtVetura;
    public TextField txtModeli;
    public TextField txtKm;
    public TextField txtTargat;
    public TextField txtNgjyra;
    public TextField txtCmimi;

    @FXML
    public Button btnEdito;

    @FXML
    public ComboBox cmbVetura;

    public void fillComboBox1() throws SQLException{
        cmbVetura.getItems().clear();
        String fillQuery = "Select * from vehicle";
        ResultSet rs = connection.createStatement().executeQuery(fillQuery);
        while (rs.next()) {
            cmbVetura.getItems().addAll(rs.getString("name") + " " + rs.getString("model") + " - " + rs.getString("plates"));
        }

    }

    public void clearForm(){
        txtVetura.clear();txtModeli.clear();txtKm.clear();txtNgjyra.clear();txtTargat.clear();
    }

    public void fillVeturen(){
        try{
            clearForm();
            String vetura = (String) cmbVetura.getValue();
            String targat = getTargat(vetura);
            String sql = "SELECT * FROM vehicle WHERE plates = '" + targat + "';";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                txtVetura.setText(rs.getString("name"));
                txtModeli.setText(rs.getString("model"));
                txtKm.setText(rs.getString("km"));
                txtTargat.setText(rs.getString("plates"));
                txtNgjyra.setText(rs.getString("color"));
            }
        }catch(SQLException ex){
            System.out.println("GABIM E KI");
        }
    }

    public String getTargat(String vetura){
        String targat = "";
        for(int i = vetura.length()-9; i < vetura.length(); i++){
            targat+=(vetura.charAt(i));
        }
        return targat;

    }

    public void fshijVeturen() throws SQLException{
        String vetura = cmbVetura.getValue().toString();
        String targat = getTargat(vetura);
        String sql = "DELETE FROM vehicle where plates='"+targat+"';";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        fillComboBox1();
        clearForm();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setContentText("Vetura u fshi nga databaza");
        alert.showAndWait();
    }

    public void updateVetura() {
        try{
            String vetura = cmbVetura.getValue().toString();
            String targat = getTargat(vetura);
            String sql = "UPDATE vehicle SET name='"+txtVetura.getText()+"', model='"+txtModeli.getText()+"', km='"+txtKm.getText()+
                    "', plates='"+txtTargat.getText()+"', color='"+txtNgjyra.getText()+"' WHERE plates='"+targat+"';";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            fillComboBox1();
            clearForm();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setContentText("Te dhenat u regjistruan ne databaze");
            alert.showAndWait();
        }catch(SQLException ex){
            System.out.println("GABIMMM");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            fillComboBox1();
        } catch (SQLException ex) {
            Logger.getLogger(EditVeturaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
