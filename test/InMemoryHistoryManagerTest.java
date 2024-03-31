import model.task.Task;
import model.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistory();
    private final Task task = new Task("Задача", "описание", Status.NEW);

    @BeforeEach
    void beforeEach() {
        historyManager.add(task);
    }

    @Test
    void historySizeShouldBeEqual() {
        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void historyShouldBeEmpty() {
        historyManager.remove(0);
        Assertions.assertEquals(0, historyManager.getHistory().size());
    }
}
