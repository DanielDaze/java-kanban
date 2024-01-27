import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

public class TaskTest {
    TaskManager taskManager = Managers.getDefault();

    @BeforeEach
    void beforeEach() {
        taskManager.createTask(new Task("название", "описание", Status.IN_PROGRESS));
    }

    @Test
    void shouldGetAllTaskFields() {
        Assertions.assertEquals(1, taskManager.getTaskById(1).getId());
        Assertions.assertEquals("название", taskManager.getTaskById(1).getTitle());
        Assertions.assertEquals("описание", taskManager.getTaskById(1).getDescription());
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getTaskById(1).getStatus());
    }

    @Test
    void shouldSetAllTaskFields() {
        taskManager.getTaskById(1).setTitle("другое_название");
        taskManager.getTaskById(1).setDescription("другое_описание");
        taskManager.getTaskById(1).setStatus(Status.DONE);
        Assertions.assertEquals("другое_название", taskManager.getTaskById(1).getTitle());
        Assertions.assertEquals("другое_описание", taskManager.getTaskById(1).getDescription());
        Assertions.assertEquals(Status.DONE, taskManager.getTaskById(1).getStatus());
    }

    @Test
    void shouldBecomeString() {
        System.out.println(taskManager.getTaskById(1).toString());
    }


}

