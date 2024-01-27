package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> viewedTasks = new ArrayList<>();

    @Override
    public void checkIfViewedTasksIsOver10() {
        if (viewedTasks.size() == 10) {
            viewedTasks.removeFirst();
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        if (!viewedTasks.isEmpty()) {
            return viewedTasks;
        } else {
            return null;
        }
    }

    @Override
    public void add(Task task) {
        viewedTasks.add(task);
    }

}