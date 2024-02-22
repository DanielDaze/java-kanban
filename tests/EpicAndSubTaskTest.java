import model.Epic;
import model.Status;
import model.SubTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

public class EpicAndSubTaskTest {
    TaskManager taskManager = Managers.getDefault();

    @BeforeEach
    void beforeEach() {
        Epic epic1 = new Epic("1 эпик", "описание 1 эпика", Status.NEW);
        taskManager.create(epic1);
        SubTask subTask1 = new SubTask("1 подзадача", "1 эпик", Status.NEW, epic1.getId());
        taskManager.create(subTask1);
        SubTask subTask2 = new SubTask("2 подзадача", "1 эпик", Status.NEW, epic1.getId());
        taskManager.create(subTask2);
        epic1.getSubTasksIds().add(subTask1.getId());
        epic1.getSubTasksIds().add(subTask2.getId());
    }

    @Test
    void shouldBecomeString() {
        System.out.println(taskManager.getSubTaskById(2).toString());
    }

    @Test
    void shouldGetEpicIdFromSubTask() {
        Assertions.assertEquals(1, taskManager.getSubTaskById(2).getEpicId());
    }
}
