package service;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, SubTask> subTasks;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int generateId() {
        return ++seq;
    }

    @Override
    public void getAll() {
        calculateStatusForEpics();
        calculateScheduleForEpics();
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
        calculateScheduleForEpics();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        calculateStatusForEpics();
        calculateScheduleForEpics();
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        if (checkTasksOverlapping(task)) {
            System.out.println("Данная задача совпадает по времени с одной из ранее созданных");
        } else {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        if (checkTasksOverlapping(subTask)) {
            System.out.println("Данная задача совпадает по времени с одной из ранее созданных");
        } else {
            subTasks.put(subTask.getId(), subTask);
        }
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
            calculateScheduleForEpics();
        } else {
            System.out.println("Задачи с таким id еще нет");
        }
    }

    @Override
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
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
                historyManager.remove(subTaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Задачи с таким id еще нет");
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            subTasks.remove(id);
            calculateStatusForEpics();
            calculateScheduleForEpics();
            historyManager.remove(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Задачи с таким id еще нет");
        }
    }

    @Override
    public void printSubTasksInEpic(int epicId) {
        System.out.println(epics.get(epicId).getSubTasksIds());
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        Comparator<Task> byStartTime = Comparator.comparing(Task::getStartTime);
        TreeSet<Task> prioritizedTasks = new TreeSet<>(byStartTime);
        if (!tasks.isEmpty()) {
            prioritizedTasks.addAll(tasks.values());
        }

        if (!epics.isEmpty()) {
            for (Epic epic : epics.values()) {
                if (epic.getStartTime() != null) {
                    prioritizedTasks.add(epic);
                }
            }
        }

        if (!subTasks.isEmpty()) {
            prioritizedTasks.addAll(subTasks.values());
        }
        return prioritizedTasks;
    }

    private boolean checkTasksOverlapping(Task task) {
        boolean overlapping = false;
        TreeSet<Task> prioritizedTasks = getPrioritizedTasks();
        if (!prioritizedTasks.isEmpty()) {
            for (Task prioritizedTask : prioritizedTasks) {
                if (task.getStartTime().isBefore(prioritizedTask.getEndTime()) && task.getEndTime().isAfter(prioritizedTask.getStartTime())) {
                    overlapping = true;
                    break;
                }
            }
        }
        return overlapping;
    }

    private void calculateStatusForEpics() {
        for (Epic epic : epics.values()) {
            int checkNumber = 0;
            boolean checkIfAllNEW = false;
            boolean checkIfAllDONE = false;
            if (!epic.getSubTasksIds().isEmpty()) {
                for (int i = 0; i < epic.getSubTasksIds().size(); i++) {
                    if (subTasks.get(epic.getSubTasksIds().get(i)).getStatus() == Status.NEW) {
                        checkNumber++;
                        if (checkNumber == epic.getSubTasksIds().size()) {
                            checkIfAllNEW = true;
                        }
                    }
                }
                for (int i = 0; i < epic.getSubTasksIds().size(); i++) {
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
    }

    private void calculateScheduleForEpics() {
        for (Epic epic : epics.values()) {
            Duration epicDuration = Duration.ofMinutes(0);
            LocalDateTime epicStartTime = LocalDateTime.of(2999, 12, 31, 0, 0, 0);
            for (int i = 0; i < epic.getSubTasksIds().size(); i++) {
                SubTask curSubTask = getSubTaskById(epic.getSubTasksIds().get(i));
                epicDuration = epicDuration.plus(curSubTask.getDuration());

                if (epicStartTime.isAfter(curSubTask.getStartTime())) {
                    epicStartTime = curSubTask.getStartTime();
                }
            }
            epic.setDuration(epicDuration);
            epic.setStartTime(epicStartTime);
        }
    }

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }
}
