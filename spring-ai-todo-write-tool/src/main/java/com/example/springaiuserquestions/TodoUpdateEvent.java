package com.example.springaiuserquestions;

import org.springaicommunity.agent.tools.TodoWriteTool;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class TodoUpdateEvent extends ApplicationEvent {
  private final List<TodoWriteTool.Todos.TodoItem> todos;
  public TodoUpdateEvent(Object source, List<TodoWriteTool.Todos.TodoItem> todos) {
    super(source);
    this.todos = todos;
  }
  public List<TodoWriteTool.Todos.TodoItem> getTodos() { return todos; }
}
