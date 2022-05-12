
package it.polito.tdp.borders;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	
	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private ComboBox<Country> cmbStato;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno;

    @FXML // fx:id="txtResult"
    private TextArea txtResult;

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	int anno;
    	
    	try{ 
    		anno= Integer.parseInt(txtAnno.getText());
    		if (anno<1816 || anno>2016) {
    			txtResult.setText("L'anno deve essere compreso tra 1816 e 2016");
    			return; 
    		}
    		
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserisci un anno");
    		return; //deve finire non può andare avanti
    	}
    	
    	model.creaGrafo(anno);
    	txtResult.clear();
    	txtResult.appendText("Vertici: "+ model.nVertici());
    	txtResult.appendText("\nArchi: "+ model.nArchi());
    	txtResult.appendText("\nNumero componenti connesse: "+ model.getComponenteConnessa());
    	
    	Map<Country,Integer> confini = model.getNumConfini(); 
    	for(Country c: confini.keySet()) {
    	txtResult.appendText("\n"+ c.getNome() + " " + confini.get(c));
    }
    	
    
    }

    @FXML
    void statiRaggiungibili(ActionEvent event) {
    	txtResult.clear();
    	Set<Country> confini; 
    	
    	Country stato= cmbStato.getValue();
    	confini= model.getRaggiungibili(stato);
    	txtResult.appendText("Paesi raggiungibili a partire da: " +stato+"\n");
    	
    	//stampa stati
    	for (Country c: confini) {
    		txtResult.appendText(c.toString());
    	}
    	
    }

    @FXML
    void initialize() {
        assert cmbStato != null : "fx:id=\"cmbStato\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    public void setModel(Model model) {
    	this.model = model;
    	cmbStato.getItems().addAll(model.getCountry().values());
    }

}

