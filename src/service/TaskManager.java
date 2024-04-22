package service;

import exception.DateTimeConflict;
import model.task.Epic;
import model.task.SubTask;
import model.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    List<Task> getHistory();

    void getAll();

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, SubTask> getSubTasks();

    TreeSet<Task> getPrioritizedTasks();

    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicById(int id);

    void createTask(Task task) throws DateTimeConflict;

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void updateTask(Task newTask, int id);

    void updateEpic(Epic newEpic, int id);

    void updateSubTask(SubTask newSubTask, int id);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    void printSubTasksInEpic(int epicId);
}
