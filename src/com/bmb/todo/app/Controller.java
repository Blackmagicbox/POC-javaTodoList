package com.bmb.todo.app;

import com.bmb.todo.app.datamodel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<TodoItem> todoitems;

    @FXML
    private ListView<TodoItem> todoListView;
    @FXML
    private TextArea todoListItemDetailsView;

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

        todoListView.getItems().setAll(todoitems);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    public void handleClickListView(){
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        System.out.println(item.getDetails());
        todoListItemDetailsView.setText(buildDescriptionString(item));
    };

    private String buildDescriptionString(TodoItem i) {
        StringBuilder sb = new StringBuilder(i.getShortDescription());
        sb.append("\n\n");
        sb.append(i.getDetails());
        sb.append("\n\n");
        sb.append("Due: ");
        sb.append(i.getDeadLine().toString());
        return sb.toString();
    };
}
