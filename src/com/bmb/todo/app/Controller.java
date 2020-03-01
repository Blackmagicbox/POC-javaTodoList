package com.bmb.todo.app;

import com.bmb.todo.app.datamodel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<TodoItem> todoitems;

    @FXML
    private ListView<TodoItem> todoListView;
    @FXML
    private TextArea todoListItemDetailsView;
    @FXML
    private Label deadlineLabel;

    public void initialize() {
        TodoItem item1 = new TodoItem("Mail birthday card",
                "Buy 30th birthday card for John", LocalDate.of(2020, Month.AUGUST, 26));

        TodoItem item2 = new TodoItem("Doctors appointment",
                "Go to doctor Marlon for the checkup", LocalDate.of(2020, Month.MAY, 3));

        TodoItem item3 = new TodoItem("Working meeting",
                "Meeting to discuss the begining of the sprint", LocalDate.of(2020, Month.JULY, 24));

        todoitems = new ArrayList<>();
        todoitems.add(item1);
        todoitems.add(item2);
        todoitems.add(item3);

        todoListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                updateTodoItemText(item);
            }
        });

        todoListView.getItems().setAll(todoitems);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();
    }

    @FXML
    public void updateTodoItemText(TodoItem item){
        todoListItemDetailsView.setText(item.getDetails());
        deadlineLabel.setText(item.getDeadLine().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    };
}
