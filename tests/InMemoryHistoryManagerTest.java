import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

public class InMemoryHistoryManagerTest {

    static TaskManager taskManager0 = Managers.getDefault();
    static TaskManager taskManager3 = Managers.getDefault();
    static TaskManager taskManager11 = Managers.getDefault();

    @BeforeAll
    static void beforeAll() {
        Task task1 = new Task("1 задача", "описание 1 задачи", Status.NEW);
        taskManager0.createTask(task1);
        taskManager3.createTask(task1);
        taskManager11.createTask(task1);
        Task task2 = new Task("2 задача", "описание 2 задачи", Status.NEW);
        taskManager3.createTask(task2);
        taskManager0.createTask(task2);
        taskManager11.createTask(task2);

        Epic epic1 = new Epic("1 эпик", "описание 1 эпика", Status.NEW);
        taskManager3.createEpic(epic1);
        taskManager0.createEpic(epic1);
        SubTask subTask1 = new SubTask("1 подзадача", "1 эпик", Status.NEW, epic1.getId());
        taskManager3.createSubTask(subTask1);
        taskManager0.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("2 подзадача", "1 эпик", Status.NEW, epic1.getId());
        taskManager3.createSubTask(subTask2);
        taskManager0.createSubTask(subTask2);
        epic1.getSubTasksIds().add(subTask1.getId());
        epic1.getSubTasksIds().add(subTask2.getId());


        taskManager3.getTaskById(1);
        taskManager3.getEpicById(5);
        taskManager3.getSubTaskById(3);

        taskManager11.getTaskById(1);
        taskManager11.getTaskById(2);
        taskManager11.getTaskById(1);
        taskManager11.getTaskById(1);
        taskManager11.getTaskById(1);
        taskManager11.getTaskById(1);
        taskManager11.getTaskById(1);
        taskManager11.getTaskById(1);
        taskManager11.getTaskById(1);
        taskManager11.getTaskById(1);
        taskManager11.getTaskById(1);
    }

    @Test
    void shouldReturn3Tasks() {
        Assertions.assertEquals(3, taskManager3.getHistory().size());
    }

    @Test
    void shouldReturnNull() {
        Assertions.assertNull(taskManager0.getHistory());
    }

    @Test
    void shouldReturnPositiveIfOver11() {
        Assertions.assertSame(taskManager11.getHistory().getFirst(), taskManager11.getTaskById(2));
    }
}
