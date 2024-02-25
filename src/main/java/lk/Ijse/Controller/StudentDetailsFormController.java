package lk.Ijse.Controller;

import com.jfoenix.controls.JFXTextField;
import jakarta.transaction.SystemException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private ObservableList<StudentDto> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadAllStudents();
    }

    private void loadAllStudents() {
        List<StudentDto> studentDtos = studentBo.getAllStudents();
        ObservableList<StudentDto> observableList = FXCollections.observableArrayList(studentDtos);
        tblStudent.setItems(observableList);
        tblStudent.setItems(observableList);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContactNum.setCellValueFactory(new PropertyValueFactory<>("contactNum"));

        // Add delete and update buttons to the table
        colDelete.setCellFactory(column -> {
            return new TableCell<StudentDto, Void>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction((ActionEvent event) -> {
                        StudentDto student = getTableView().getItems().get(getIndex());
                        studentBo.deleteStudent(student.getId());
                        loadAllStudents();
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
        });

        colUpdate.setCellFactory(column -> {
            return new TableCell<StudentDto, Void>() {
                private final Button updateButton = new Button("Update");

                {
                    updateButton.setOnAction((ActionEvent event) -> {
                        StudentDto student = getTableView().getItems().get(getIndex());
                        try {
                            studentBo.updateStudent(student);
                            loadAllStudents();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (SystemException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(updateButton);
                    }
                }
            };
        });

    }


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
            loadAllStudents();
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
                    loadAllStudents();
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
                loadAllStudents();
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
