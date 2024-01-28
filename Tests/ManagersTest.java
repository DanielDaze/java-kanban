import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

public class ManagersTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    void shouldCreateWorkingTaskManager() {
        Task testTask = new Task("описание", "название", Status.NEW);
        taskManager.createTask(testTask);
        testTask.setId(1);
        Assertions.assertEquals(testTask, taskManager.getTaskById(1));
    }
}
