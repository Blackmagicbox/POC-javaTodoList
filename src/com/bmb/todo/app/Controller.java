package com.bmb.todo.app;

import com.bmb.todo.app.datamodel.TodoData;
import com.bmb.todo.app.datamodel.TodoItem;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class Controller {

  @FXML
  private ListView<TodoItem> todoListView;
  @FXML
  private TextArea todoListItemDetailsView;
  @FXML
  private Label deadlineLabel;

  @FXML
  private BorderPane mainBorderPane;

  /**
   * Initialize method.
   */
  public void initialize() {
    todoListView.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, oldValue, newValue) -> {
          if (newValue != null) {
            TodoItem item = todoListView.getSelectionModel().getSelectedItem();
            updateTodoItemText(item);
          }
        });
    todoListView.setItems(TodoData.getInstance().getTodoItems());
    todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    todoListView.getSelectionModel().selectFirst();
    todoListView.setCellFactory(new Callback<>() {
      @Override
      public ListCell<TodoItem> call(ListView<TodoItem> todoItemListView) {
        ListCell<TodoItem> cell = new ListCell<>() {
          @Override
          protected void updateItem(TodoItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              setText(null);
            } else {
              setText(item.getShortDescription());
              if (item.getDeadLine().isBefore(LocalDate.now().plusDays(1))) {
                setTextFill(Color.RED);
              } else if (item.getDeadLine().equals(LocalDate.now().plusDays(1))) {
                setTextFill(Color.ORANGE);
              }
            }
          }
        };
        return cell;
      }
    });
  }

  @FXML
  public void showNewItemDialog() throws Exception {
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.initOwner(mainBorderPane.getScene().getWindow());
    dialog.setTitle("New Item");
    dialog.setHeaderText("Add new items to the todo list");
    FXMLLoader fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));
    try {
      dialog.getDialogPane().setContent(fxmlLoader.load());
    } catch (IOException e) {
      System.out.println("Could not load the Dialog");
      e.printStackTrace();
      return;
    }
    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
    Optional<ButtonType> result = dialog.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      DialogController controller = fxmlLoader.getController();
      TodoItem newItem = controller.processResults();
      todoListView.getSelectionModel().select(newItem);
    }
  }

  @FXML
  public void updateTodoItemText(TodoItem item) {
    todoListItemDetailsView.setText(item.getDetails());
    deadlineLabel.setText(item.getDeadLine().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
  }
}
