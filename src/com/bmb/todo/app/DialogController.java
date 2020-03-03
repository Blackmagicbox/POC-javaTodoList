package com.bmb.todo.app;

import com.bmb.todo.app.datamodel.TodoData;
import com.bmb.todo.app.datamodel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {
  @FXML
  private TextField shortDescriptionField;
  @FXML
  private TextArea detailsArea;
  @FXML
  private DatePicker deadlinePicker;

  @FXML
  public void processResults () {
    String shortDescription = shortDescriptionField.getText().trim();
    String details = detailsArea.getText().trim();
    LocalDate deadline = deadlinePicker.getValue();

    if (!shortDescription.isEmpty() && !details.isEmpty() && deadline != null) {
      TodoData.getInstance().addTodoItem(new TodoItem(shortDescription, details, deadline));
    } else {
      return;
    }
  };

}
