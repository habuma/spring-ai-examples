package com.example.springaiuserquestions;

import org.springaicommunity.agent.tools.TodoWriteTool;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TodoProgressListener {

  @EventListener
  public void onTodoUpdate(TodoUpdateEvent event) {
    System.err.println(" -- ");
    int completed = (int) event.getTodos()
        .stream()
        .filter(t -> t.status() == TodoWriteTool.Todos.Status.completed).count();
    int total = event.getTodos().size();

    System.out.printf("\nProgress: %d/%d tasks completed (%.0f%%)\n", completed, total,
        (completed * 100.0 / total));
  }
}