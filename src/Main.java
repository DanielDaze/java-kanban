import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("1 задача", "описание 1 задачи", Status.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("2 задача", "описание 2 задачи", Status.NEW);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("1 эпик", "описание 1 эпика", Status.NEW);
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("1 подзадача", "1 эпик", Status.NEW, 3);
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("2 подзадача", "1 эпик", Status.NEW, 3);
        taskManager.createSubTask(subTask2);
        epic1.getSubTasksIds().add(subTask1.getId());
        epic1.getSubTasksIds().add(subTask2.getId());

        Epic epic2 = new Epic("2 эпик", "описание 2 эпика", Status.NEW);
        taskManager.createEpic(epic2);
        SubTask subTask3 = new SubTask("1 подзадача", "2 эпик", Status.NEW, 4);
        taskManager.createSubTask(subTask3);
        epic2.getSubTasksIds().add(subTask3.getId());
        taskManager.getAll();
        taskManager.printSubTasksInEpic(3);
        taskManager.printSubTasksInEpic(6);

        SubTask newSubTask = new SubTask("новое название 1 подзадачи", "2 эпик", Status.DONE, 4);
        taskManager.updateSubTask(newSubTask, 7);

        taskManager.getAll();
    }
}
