import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class MainTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    void test() {
        Task task1 = new Task("1 задача", "описание 1 задачи", Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2021, 4, 21, 19, 30));
        taskManager.createTask(task1);
        Task task2 = new Task("2 задача", "описание 2 задачи", Status.NEW, Duration.ofMinutes(7), LocalDateTime.of(2012, 12, 21, 12, 0));
        taskManager.createTask(task2);

        Epic epic1 = new Epic("1 эпик", "описание 1 эпика", Status.NEW);
        taskManager.createEpic(epic1);
        taskManager.createSubTask(new SubTask("1 подзадача", "1 эпик", Status.NEW, Duration.ofMinutes(28), LocalDateTime.of(2022, 10, 22, 22, 30), epic1.getId()));
        epic1.getSubTasksIds().add(taskManager.getSubTaskById(4).getId());
        taskManager.createSubTask(new SubTask("2 подзадача", "1 эпик", Status.NEW, Duration.ofMinutes(48), LocalDateTime.of(2023, 11, 23, 8, 31), epic1.getId()));
        epic1.getSubTasksIds().add(taskManager.getSubTaskById(5).getId());
        taskManager.createSubTask(new SubTask("3 подзадача", "1 эпик", Status.NEW, Duration.ofMinutes(35), LocalDateTime.of(2021, 8, 10, 21, 32), epic1.getId()));
        epic1.getSubTasksIds().add(taskManager.getSubTaskById(6).getId());
        Epic epic2 = new Epic("2 эпик", "описание 2 эпика", Status.NEW);
        taskManager.createEpic(epic2);
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        System.out.println(taskManager.getHistory());

        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistory());

        taskManager.removeEpicById(3);
        System.out.println(taskManager.getHistory());
    }
}
