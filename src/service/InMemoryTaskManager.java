package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int seq = 0;

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public int generateId() {
        return ++seq;
    }
    @Override
    public void getAll() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
        for (SubTask subTask : subTasks.values()) {
            System.out.println(subTask);
        }
    }


    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        for (Epic epic : epics.values()) {
            for (int subTaskId : epic.getSubTasksIds()) {
                subTasks.remove(subTaskId);
            }
            epics.remove(epic.getId());
        }
    }

    @Override
    public void clearSubTasks() {
        subTasks.clear();
        calculateStatusForEpics();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.checkIfViewedTasksIsOver10();
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.checkIfViewedTasksIsOver10();
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.checkIfViewedTasksIsOver10();
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
            subTask.setId(generateId());
            subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public void updateTask(Task newTask, int id) {
        if (tasks.containsKey(id)) {
            tasks.get(id).setId(newTask.getId());
            tasks.get(id).setTitle(newTask.getTitle());
            tasks.get(id).setDescription(newTask.getDescription());
            tasks.get(id).setStatus(newTask.getStatus());
        } else {
            System.out.println("Задачи с таким id еще нет");
        }
    }

    @Override
    public void updateEpic(Epic newEpic, int id) {
        if (epics.containsKey(id)) {
            epics.get(id).setId(newEpic.getId());
            epics.get(id).setTitle(newEpic.getTitle());
            epics.get(id).setDescription(newEpic.getDescription());
            epics.get(id).setStatus(newEpic.getStatus());
        } else {
            System.out.println("Задачи с таким id еще нет");
        }
    }

    @Override
    public void updateSubTask(SubTask newSubTask, int id) {
        if (subTasks.containsKey(id)) {
            subTasks.get(id).setId(newSubTask.getId());
            subTasks.get(id).setTitle(newSubTask.getTitle());
            subTasks.get(id).setDescription(newSubTask.getDescription());
            subTasks.get(id).setStatus(newSubTask.getStatus());
            calculateStatusForEpics();
        } else {
            System.out.println("Задачи с таким id еще нет");
        }
    }

    @Override
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Задачи с таким id еще нет");
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            for (int subTaskId : getEpicById(id).getSubTasksIds()) {
                subTasks.remove(subTaskId);
            }
            epics.remove(id);
        } else {
            System.out.println("Задачи с таким id еще нет");
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            subTasks.remove(id);
            calculateStatusForEpics();
        } else {
            System.out.println("Задачи с таким id еще нет");
        }
    }

    @Override
    public void printSubTasksInEpic(int epicId) {
        System.out.println(epics.get(epicId).getSubTasksIds());
    }

    @Override
    public void calculateStatusForEpics() {
        for (Epic epic : epics.values()) {
            int checkNumber = 0;
            boolean checkIfAllNEW = false;
            boolean checkIfAllDONE = false;
            for (int i = 0; i < epic.getSubTasksIds().size() - 1; i++) {
                if (subTasks.get(epic.getSubTasksIds().get(i)).getStatus() == Status.NEW) {
                    checkNumber++;
                    if (checkNumber == epic.getSubTasksIds().size()) {
                        checkIfAllNEW = true;
                    }
                }
            }
            for (int i = 0; i < epic.getSubTasksIds().size() - 1; i++) {
                if (subTasks.get(epic.getSubTasksIds().get(i)).getStatus() == Status.DONE) {
                    checkNumber++;
                    if (checkNumber == epic.getSubTasksIds().size()) {
                        checkIfAllDONE = true;
                    }
                }
            }
            if (checkIfAllNEW) {
                epic.setStatus(Status.NEW);
            } else if (checkIfAllDONE) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public InMemoryTaskManager() { // Дефолтный конструктор
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }
}
