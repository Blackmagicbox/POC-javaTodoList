package com.bmb.todo.app;

import static java.util.Comparator.comparing;

import com.bmb.todo.app.datamodel.TodoData;
import com.bmb.todo.app.datamodel.TodoItem;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
  @FXML
  private ContextMenu listContextMenu;
  @FXML
  private ToggleButton filterToggleBtn;

  private FilteredList<TodoItem> filteredList;
  private Predicate<TodoItem> showTodaysItems;
  private Predicate<TodoItem> showAllItems;

  /**
   * Initialize method.
   */
  public void initialize() {
    listContextMenu = new ContextMenu();
    MenuItem deleteMenuItem = new MenuItem("Delete");
    deleteMenuItem.setOnAction(actionEvent -> {
      TodoItem item = todoListView.getSelectionModel().getSelectedItem();
      deleteItem(item);
    });
    listContextMenu.getItems().addAll(deleteMenuItem);
    todoListView.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, oldValue, newValue) -> {
          if (newValue != null) {
            TodoItem item = todoListView.getSelectionModel().getSelectedItem();
            updateTodoItemText(item);
          }
        });
    showTodaysItems = todoItem -> (todoItem.getDeadLine().isEqual(LocalDate.now()));

    showAllItems = item -> true;

    filteredList = new FilteredList<>(TodoData.getInstance().getTodoItems(),
        item -> true);
    SortedList<TodoItem> sortedList = new SortedList<>(filteredList, comparing(TodoItem::getDeadLine));
    todoListView.setItems(sortedList);
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
        cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
          if (isNowEmpty) {
            cell.setContextMenu(null);
          } else {
            cell.setContextMenu(listContextMenu);
          }
        });
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

  public void deleteItem(TodoItem item) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Delete Todo Item");
    alert.setHeaderText("Delete items from todo list item");
    alert.setContentText("Do you really want to delete: " + item.getShortDescription()
        + " press Ok to confirm or Cancel to keep it");
    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && (result.get() == ButtonType.OK)) {
      TodoData.getInstance().deleteTodoItem(item);
    }
  }

  @FXML
  public void handleKeyPress(KeyEvent keyEvent) {
    TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
    Boolean canDelete =
        keyEvent.getCode().equals(KeyCode.DELETE) || keyEvent.getCode().equals(KeyCode.BACK_SPACE);
    if (selectedItem != null && canDelete) {
      deleteItem(selectedItem);
    }
  }

  @FXML
  public void handleFilterButton() {
    TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
    if (filterToggleBtn.isSelected()) {
      filteredList.setPredicate(showTodaysItems);
      if (filteredList.isEmpty()) {
        todoListItemDetailsView.clear();
        deadlineLabel.setText("");
      } else if (filteredList.contains(selectedItem)) {
        todoListView.getSelectionModel().select(selectedItem);
      } else {
        todoListView.getSelectionModel().selectFirst();
      }
    } else {
      filteredList.setPredicate(showAllItems);
      if (filteredList.contains(selectedItem)) {
        todoListView.getSelectionModel().select(selectedItem);
      } else {
        todoListView.getSelectionModel().selectFirst();
      }
    }
  }

  @FXML
  public void handleExit() {
    Platform.exit();
  }

  @FXML
  public void editItem() {
    System.out.println("TBI");
  }
}
