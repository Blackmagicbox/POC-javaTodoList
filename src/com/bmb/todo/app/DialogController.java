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
  public TodoItem processResults () throws Exception {
    String shortDescription = shortDescriptionField.getText().trim();
    String details = detailsArea.getText().trim();
    LocalDate deadline = deadlinePicker.getValue();

    if (!shortDescription.isEmpty() && !details.isEmpty() && deadline != null) {
      TodoItem todoItem =  new TodoItem(shortDescription, details, deadline);
      TodoData.getInstance().addTodoItem(todoItem);
      return todoItem;
    } else {
      throw new Exception("You need to feel every field to add a new Item");
    }
  };

}
