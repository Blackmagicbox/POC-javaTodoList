package com.bmb.todo.app;

import com.bmb.todo.app.datamodel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<TodoItem> todoitems;

    @FXML
    private ListView todoListView;

    public void initialize() {
        TodoItem item1 = new TodoItem("Mail birthday card",
                "Buy 30th birthday card for John", LocalDate.of(2020, Month.AUGUST, 26));

        TodoItem item2 = new TodoItem("Doctors appointment",
                "Go to doctor Marlon for the checkup", LocalDate.of(2020, Month.MAY, 3));

        TodoItem item3 = new TodoItem("Working meeting",
                "Meeting to discuss the begining of the sprint", LocalDate.of(2020, Month.JULY, 24));

        todoitems = new ArrayList<TodoItem>();
        todoitems.add(item1);
        todoitems.add(item2);
        todoitems.add(item3);

        todoListView.getItems().setAll(todoitems);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
}
