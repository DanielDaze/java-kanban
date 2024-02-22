package service;

import model.Task;

import java.util.List;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> viewedTasks = new LinkedList<>();

    private static class Node {

    }

    @Override
    public List<Task> getHistory() {
        if (!viewedTasks.isEmpty()) {
            return viewedTasks;
        } else {
            return null;
        }
    }

    @Override
    public void add(Task task) {
        checkIfViewedTasksIsOver10();
        viewedTasks.add(task);
    }

    @Override
    public void remove(int id) {
        // TODO
    }

    private void checkIfViewedTasksIsOver10() {
        if (viewedTasks.size() == 10) {
            viewedTasks.removeFirst();
        }
    }
}