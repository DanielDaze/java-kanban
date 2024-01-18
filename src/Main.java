import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("1-я задача", "описание 1-й задачи", Status.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("2-я задача", "описание 2-й задачи", Status.NEW);
        taskManager.createTask(task2);
        Epic epic1 = new Epic("1-й эпик", "описание 1-го эпика", Status.NEW);
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("1-я подзадача 1-го эпика", "описание 1-й подзадачи 1-го эпика",
                Status.NEW, epic1);
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("2-я подзадача 1-го эпика", "описание 2-й подзадачи 1-го эпика",
                Status.NEW, epic1);
        taskManager.createSubTask(subTask2);
        ArrayList<SubTask> subTasks1 = new ArrayList<>();
        epic1.setSubTasks(subTasks1);
        Epic epic2 = new Epic("2-й эпик", "описание 2-го эпика", Status.NEW);
        taskManager.createEpic(epic2);
        SubTask subTask3 = new SubTask("1-я подзадача 2-го эпика", "описание 1-й подзадачи 2-го эпика",
                Status.NEW, epic2);
        taskManager.createSubTask(subTask3);
        ArrayList<SubTask> subTasks2 = new ArrayList<>();
        epic2.setSubTasks(subTasks2);

        taskManager.printTasks();
        taskManager.printEpics();
        taskManager.printSubTasks();
        taskManager.printSubTasksInEpic(3);
        taskManager.printSubTasksInEpic(6);

        taskManager.getTaskById(1).setStatus(Status.IN_PROGRESS);
        taskManager.getSubTaskById(4).setStatus(Status.DONE);
        taskManager.getSubTaskById(5).setStatus(Status.DONE);
        epic1.calculateStatus();
        taskManager.removeTaskById(2);
        taskManager.removeEpicById(6);

        taskManager.printTasks();
        taskManager.printEpics();
        taskManager.printSubTasks();
        taskManager.printSubTasksInEpic(3);
    }
}
