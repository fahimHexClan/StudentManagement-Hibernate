package lk.Ijse.Controller;

import com.jfoenix.controls.JFXTextField;
import jakarta.transaction.SystemException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lk.Ijse.Bo.custom.StudentBo;
import lk.Ijse.Bo.custom.impl.StudentBoImpl;
import lk.Ijse.Dto.StudentDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StudentDetailsFormController {

    public TableView tblStudent;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colContactNum;
    public TableColumn colDelete;
    public TableColumn colUpdate;
    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtContactNumber;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    private final StudentBo studentBo = new StudentBoImpl();


    @FXML
    void SaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException, SystemException {
        String name = txtName.getText();
        String address = txtAddress.getText();
        String contactNum = txtContactNumber.getText();

        StudentDto studentDto = new StudentDto(name, address, contactNum);
        boolean isSuccess = studentBo.saveStudent(studentDto);
        if (isSuccess) {
            int generatedId = studentDto.getId(); // Get the auto-generated ID
            txtId.setText(String.valueOf(generatedId)); // Display the ID in the UI
            new Alert(Alert.AlertType.INFORMATION, "Data added").show();
            ClearAllOnAction();
        } else {
            new Alert(Alert.AlertType.ERROR, "Data not added").show();
        }
    }


    @FXML
    void DeleteOnAction(ActionEvent event) {
        String idText = txtId.getText();
        if (!idText.isEmpty()) {
            int id = Integer.parseInt(idText);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete record with ID " + id + "?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                boolean isDeleted = studentBo.deleteStudent(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Data Deleted Successfully").show();
                    ClearAllOnAction();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Data Not Deleted").show();
                }
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please enter ID to delete").show();
        }
    }

    @FXML
    void UpdateOnAction(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtId.getText());
            String name = txtName.getText();
            String address = txtAddress.getText();
            String contactNum = txtContactNumber.getText();

            StudentDto studentDto = new StudentDto(id, name, address, contactNum);
            boolean isSuccess = studentBo.updateStudent(studentDto);
            if (isSuccess) {
                new Alert(Alert.AlertType.INFORMATION, "Data updated").show();
                ClearAllOnAction();
            } else {
                new Alert(Alert.AlertType.ERROR, "Data not updated").show();
            }
        } catch (SQLException | ClassNotFoundException | SystemException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while updating data").show();
        }
    }

    @FXML
    void ClearAllOnAction() {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtContactNumber.clear();
    }

    @FXML
    void SearchOnAction(ActionEvent event) {
        String keyword = txtId.getText();
        try {
            List<StudentDto> studentDtos = studentBo.searchStudent(keyword);

            if (studentDtos.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No matching records found").show();
                ClearAllOnAction(); // Clear fields
            } else {
                StudentDto firstResult = studentDtos.get(0);
                txtId.setText(String.valueOf(firstResult.getId()));
                txtName.setText(firstResult.getName());
                txtAddress.setText(firstResult.getAddress());
                txtContactNumber.setText(firstResult.getContactNum());
            }
        } catch (SQLException | ClassNotFoundException | SystemException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while searching data").show();
        }
    }
}
