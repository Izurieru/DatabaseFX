package databasefx;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class LayoutController implements Initializable {
        
    private final String url = "jdbc:mysql://localhost:3306/learn?useTimezone=true&serverTimezone=UTC";
    private final String user = "root";
    private final String pass = "";
    
    ObservableList<String> searchResults = FXCollections.observableArrayList();
    ObservableList<String> list = FXCollections.observableArrayList();
    ArrayList<Student> students = new ArrayList<>();

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField searchField;

    @FXML
    private TextField textField;

    @FXML
    private TextField studentName;

    @FXML
    private TextField studentCourse;
    
     @FXML
    private Text errorMessage;
    
    @Override
    public void initialize(URL arg0, ResourceBundle rb) {      
        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            
            while(rs.next()) {
                int id = rs.getInt("stud_id");
                String name = rs.getString("stud_name");
                String course = rs.getString("stud_course");
                
                students.add(new Student(id, name, course));
                
                
            }
            
            for (Student s: students) {
                    list.add(s.getName());
            }
           
            listView.setItems(list);
            
            conn.close();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addStudent(ActionEvent event) {
        if(studentName.getText().isEmpty() || studentCourse.getText().isEmpty()) {
            errorMessage.setText("Fields cannot be empty!");
        } else {
            errorMessage.setText("");
            String name = studentName.getText();
            String course = studentCourse.getText();
            
            students.add(new Student(name, course));
            list.add(students.get(students.size()-1).getName());
            
            listView.setItems(list);
            
            studentName.clear();
            studentCourse.clear();
            
            try {
                Connection conn = DriverManager.getConnection(url, user, pass);
                Statement stmt = conn.createStatement();

                StringBuilder sb = new StringBuilder();
                Formatter fm = new Formatter(sb);

                fm.format("INSERT INTO students (stud_name, stud_course) VALUES ('%s', '%s')", name, course);
                stmt.executeUpdate(sb.toString());

                conn.close();
    
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void deleteStudent(ActionEvent event) {
       if (listView.getSelectionModel().getSelectedItem() == null) {
           return;
       }
       
       String name = listView.getSelectionModel().getSelectedItem();
       int id = 0;
       
       for (int i=0; i<students.size(); i++) {
           if (students.get(i).getName().equals(name)) {
               id = students.get(i).getId();
               students.remove(students.get(i));
               list.remove(name);
           }
       }

       listView.setItems(list);
       
       try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();

            StringBuilder sb = new StringBuilder();
            Formatter fm = new Formatter(sb);

            fm.format("DELETE FROM students WHERE stud_id = %d", id);
            stmt.executeUpdate(sb.toString());

            conn.close();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void displaySearchResults(KeyEvent event) {
        if (searchField.getText().isEmpty() && event.getCode() == KeyCode.ENTER) {
            listView.setItems(list);
        } else if (!searchField.getText().isEmpty() && event.getCode() == KeyCode.ENTER) {
            String searchText = searchField.getText();
            
            for(String s: list) {
                if (s.contains(searchText)) {
                    searchResults.add(s);
                }
            }
            
            if(searchResults.size() == 0) {
                searchResults.add("No results");
            } else {
                listView.setItems(searchResults);
            }
        }
        
    }

    @FXML
    void editStudent(ActionEvent event) {
        if (listView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        textField.setText(listView.getSelectionModel().getSelectedItem());
    }

    @FXML
    void updateStudent(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER  && !textField.getText().isEmpty()) {
            String name = listView.getSelectionModel().getSelectedItem();
            String newName = textField.getText();
            int id = 0;
            
            for (Student s: students) {
                if (s.getName().equals(name)) {
                    id = s.getId();
                    s.setName(newName);
                }
            }
            
            list.clear();
            
            for (Student s: students) {
                list.add(s.getName());
            }
            
            listView.setItems(list);
            textField.clear();
            
            try {
                Connection conn = DriverManager.getConnection(url, user, pass);
                Statement stmt = conn.createStatement();

                StringBuilder sb = new StringBuilder();
                Formatter fm = new Formatter(sb);

                fm.format("UPDATE students SET stud_name = '%s' WHERE stud_id = %d", newName, id);
                stmt.executeUpdate(sb.toString());

                conn.close();
    
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            
        }
    }

}
