/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brainf_ck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SHIVA
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button newfile;
    @FXML
    private Button openfile;
    @FXML
    private Button savefile;
    @FXML
    private Button runfile;
    @FXML
    private TabPane filetab;
    @FXML
    private TextArea myfile;
    @FXML
    private TextField inputline;
    @FXML
    private TextArea terminal;
    @FXML
    private VBox mainsc;
    @FXML
    private Tab fileName;
    
    private boolean isSave = false;
    private File file;
    
    private int data[] = new int[1024];
    private int ptr = 0, limit,i=0;
    private String ip="";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        terminal.setEditable(false);

        terminal.textProperty().addListener((obs, oldText, newText) -> {
            if (oldText.length() < 3 && newText.length() >= 3) {
                terminal.end();
                terminal.requestFocus();

            }
        });
        
        inputline.setOnKeyPressed((e)->{
                if(!e.getCode().equals(KeyCode.ENTER))
                    return;
                if("\n".equals(inputline.getText()))
                    return;
                if(i<ip.length()){
                    String out ="";
                    data[ptr] = inputline.getText().charAt(0);
                    System.out.print(ip+"  ");
                    out = complile(ip);
                    System.out.println(out);
                    terminal.setText(terminal.getText()+out);
                }
                
                if(i>=ip.length()){
                    terminal.setText(terminal.getText()+"\nProgram Ended.\n");
                    i=0;
                    ptr=0;
                    for(int x=0;x<1024;x++)
                        data[x]=0;
                }
                inputline.setText("");
                
        });
        
        
        // CSS Styles
    }

    @FXML
    private void createNewFile(MouseEvent event) {
        fileName.setText("Unknown.bf");
        myfile.setText("");
        isSave = false;
    }

    @FXML
    private void openFIle(MouseEvent event) {
        Stage stage = (Stage) mainsc.getScene().getWindow();
        FileChooser file_c = new FileChooser();
        file_c.setTitle("Open");
        file = file_c.showOpenDialog(stage);
        if (file != null) {
            try {
                isSave = true;
                Scanner ip = new Scanner(file);
                String data = "";
                while (ip.hasNextLine()) {
                    data += ip.nextLine() + "\n";
                }
                fileName.setText(file.getName());
                myfile.setText(data);
                ip.close();
            } catch (FileNotFoundException ex) {
            }
        }
    }

    @FXML
    private void saveFile(MouseEvent event) {
        if(isSave){
            SaveFile(myfile.getText(), file);
            return;
        }
        Stage stage = (Stage) mainsc.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("BrainF_ck files (*.bf)", "*.bf");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            isSave = true;
            fileName.setText(file.getName());
            SaveFile(myfile.getText(), file);
        }
        

    }
    
    private void SaveFile(String content, File file){
        try {
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex){   }
         
    }

    @FXML
    private void RunFile(MouseEvent event) {
        terminal.setText(terminal.getText()+"\nProgram Ended.\n");
        ptr=0;
        i = 0;
        for(int x=0;x<1024;x++)
            data[x]=0;
        ip = myfile.getText();
        String out = "";
        String txt = "";
        txt += "Currently Running File:" + fileName.getText() + "\n";
        out = complile(ip);
        if(i!=ip.length()){
            txt += out;
        }else{
            txt += out + "\nProgram Ended.\n";
        }
        terminal.setText(txt);
        
        
    }
    


    private String complile(String ip) {
        String out = "";
        while (i < ip.length()) {
            switch (ip.charAt(i)) {
                case '>':
                    ptr = (ptr + 1) % 1024;
                    break;
                case '<':
                    ptr = (ptr - 1) % 1024;
                    break;
                case '+':
                    limit = data[ptr] + 1;
                    if (limit > 255) {
                        return "Error Infinity Loop Detected";
                    } else {
                        data[ptr] = limit;
                    }
                    break;
                case '-':
                    limit = data[ptr] - 1;
                    if (limit < 0) {
                        return "Error Infinity Loop Detected";
                    } else {
                        data[ptr] = limit;
                    }
                    break;
                case '.':
                    out += (char) data[ptr];
                    break;
                case '[':
                    if (data[ptr] == 0) {
                        int balance = 1;
                        i++;
                        while (balance!=0) {
                            if(ip.charAt(i)=='[')
                                balance++;
                            if(ip.charAt(i)==']')
                                balance--;
                            i++;
                            if(i>=1024){
                                return "Error Infinity Loop";
                            }
                        }
                    }
                    break;
                case ']':
                    if (data[ptr] != 0) {
                        int balance = -1;
                        i--;
                        while (balance!=0) {
                            if(ip.charAt(i)=='[')
                                balance++;
                            if(ip.charAt(i)==']')
                                balance--;
                            i--;
                            if(i<0){
                                return "Error Infinity Loop";
                            }
                        }
                    }
                    break;
                case ',':
                    i++;
                    return out;
                default:
            }
            i++;
        }
        return out;
    }
}
