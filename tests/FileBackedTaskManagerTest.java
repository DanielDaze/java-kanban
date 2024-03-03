import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;

public class FileBackedTaskManagerTest {

    @Test
    void test() {
        FileBackedTaskManager taskManager = new FileBackedTaskManager();
        Task task1 = new Task("1 задача", "описание 1 задачи", Status.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("2 задача", "описание 2 задачи", Status.NEW);
        taskManager.createTask(task2);
        Task task3 = new Task("проверочная задача", "описание проверочной задачи", Status.NEW);
        taskManager.createTask(task3);
        Epic epic1 = new Epic("1 эпик", "описание 1 эпика", Status.NEW);
        epic1.getSubTasksIds().add(5);
        taskManager.createEpic(epic1);
        taskManager.createSubTask(new SubTask("сабтаск", "описание", Status.DONE, 4));
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
    }

    @Test
    void test2() {
        FileBackedTaskManager taskManager = new FileBackedTaskManager();
        taskManager.loadFromFile();
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
