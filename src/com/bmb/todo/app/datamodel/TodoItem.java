package com.bmb.todo.app.datamodel;

import java.time.LocalDate;

public class TodoItem {
  private String shortDescription;
  private String details;
  private LocalDate deadLine;

  public TodoItem(String shortDescription, String details, LocalDate deadLine) {
    this.shortDescription = shortDescription;
    this.details = details;
    this.deadLine = deadLine;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public String getDetails() {
    return details;
  }

  public LocalDate getDeadLine() {
    return deadLine;
  }
}
