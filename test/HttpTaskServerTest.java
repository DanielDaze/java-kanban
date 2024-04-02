import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpTaskServerTest {

    private HttpTaskServer server;
    private HttpClient client;

    @BeforeEach
    void beforeEach() throws IOException {
        server = new HttpTaskServer();
        server.start();
        client = HttpClient.newHttpClient();
    }

    @Test
    void successfulNewTask() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/tasks/");
        URI uriGet = URI.create("http://localhost:8080/tasks/1");
        String body = "{\"id\":1,\"title\":\"Задание\",\"description\":\"Описание\",\"status\":\"NEW\",\"duration\":\"PT30M\",\"startTime\":\"2024-12-12T12:30\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uriGet)
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, postResponse.statusCode());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(body, response.body());
    }

    @Test
    void nonExistingTask() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/tasks/");
        URI uriGet = URI.create("http://localhost:8080/tasks/2");
        String body = "{\"id\":1,\"title\":\"Задание\",\"description\":\"Описание\",\"status\":\"NEW\",\"duration\":\"PT30M\",\"startTime\":\"2024-12-12T12:30\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uriGet)
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    void overlappingTask() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/tasks/");
        URI uriPostAnother = URI.create("http://localhost:8080/tasks/");
        String body = "{\"id\":1,\"title\":\"Задание\",\"description\":\"Описание\",\"status\":\"NEW\",\"duration\":\"PT30M\",\"startTime\":\"2024-12-12T12:30\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(uriPostAnother)
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(406, response.statusCode());
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/tasks/");
        URI uriUpdate = URI.create("http://localhost:8080/tasks/1");
        String body = "{\"id\":1,\"title\":\"Задание\",\"description\":\"Описание\",\"status\":\"NEW\",\"duration\":\"PT30M\",\"startTime\":\"2024-12-12T12:30\"}";
        String updBody = "{\"id\":1,\"title\":\"Обновленное_задание\",\"description\":\"Описание\",\"status\":\"NEW\",\"duration\":\"PT30M\",\"startTime\":\"2024-12-12T12:30\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest updateRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(updBody))
                .uri(uriUpdate)
                .build();
        HttpResponse<String> response = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uriUpdate)
                .build();

        HttpResponse<String> newResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals(updBody, newResponse.body());
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/tasks/");
        URI uriDelete = URI.create("http://localhost:8080/tasks/1");
        String body = "{\"id\":1,\"title\":\"Задание\",\"description\":\"Описание\",\"status\":\"NEW\",\"duration\":\"PT30M\",\"startTime\":\"2024-12-12T12:30\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uriDelete)
                .build();
        HttpResponse<String> newResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, newResponse.statusCode());
        Assertions.assertEquals("Задача удалена", newResponse.body());
    }

    @Test
    void successfulNewSubTask() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/subtasks/");
        URI uriGet = URI.create("http://localhost:8080/subtasks/1");
        String body = "{\"epicId\":2,\"id\":1,\"title\":\"сабтаск\",\"description\":\"описание саба\",\"status\":\"IN_PROGRESS\",\"duration\":\"PT45M\",\"startTime\":\"2024-05-10T19:50\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uriGet)
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, postResponse.statusCode());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(body, response.body());
    }

    @Test
    void nonExistingSubTask() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/subtasks/");
        URI uriGet = URI.create("http://localhost:8080/subtasks/2");
        String body = "{\"epicId\":2,\"id\":1,\"title\":\"сабтаск\",\"description\":\"описание саба\",\"status\":\"IN_PROGRESS\",\"duration\":\"PT45M\",\"startTime\":\"2024-05-10T19:50\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uriGet)
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    void overlappingSubTask() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/subtasks/");
        URI uriPostAnother = URI.create("http://localhost:8080/subtasks/");
        String body = "{\"epicId\":2,\"id\":1,\"title\":\"сабтаск\",\"description\":\"описание саба\",\"status\":\"IN_PROGRESS\",\"duration\":\"PT45M\",\"startTime\":\"2024-05-10T19:50\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(uriPostAnother)
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(406, response.statusCode());
    }

    @Test
    void updateSubTask() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/subtasks/");
        URI uriUpdate = URI.create("http://localhost:8080/subtasks/1");
        String body = "{\"epicId\":2,\"id\":1,\"title\":\"сабтаск\",\"description\":\"описание саба\",\"status\":\"IN_PROGRESS\",\"duration\":\"PT45M\",\"startTime\":\"2024-05-10T19:50\"}";
        String updBody = "{\"epicId\":2,\"id\":1,\"title\":\"новый_сабтаск\",\"description\":\"описание саба\",\"status\":\"IN_PROGRESS\",\"duration\":\"PT45M\",\"startTime\":\"2024-05-10T19:50\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest updateRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(updBody))
                .uri(uriUpdate)
                .build();
        HttpResponse<String> response = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uriUpdate)
                .build();

        HttpResponse<String> newResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals(updBody, newResponse.body());
    }

    @Test
    void deleteSubTask() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/tasks/");
        URI uriDelete = URI.create("http://localhost:8080/tasks/1");
        String body = "{\"epicId\":2,\"id\":1,\"title\":\"сабтаск\",\"description\":\"описание саба\",\"status\":\"IN_PROGRESS\",\"duration\":\"PT45M\",\"startTime\":\"2024-05-10T19:50\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uriDelete)
                .build();
        HttpResponse<String> newResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, newResponse.statusCode());
        Assertions.assertEquals("Задача удалена", newResponse.body());
    }

    @Test
    void successfulNewEpic() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/epics/");
        URI uriPostSubtask = URI.create("http://localhost:8080/subtasks/");
        URI uriGet = URI.create("http://localhost:8080/epics/1");
        String body = "{\"subTasksIds\":[2],\"id\":1,\"title\":\"эпик\",\"description\":\"описание эпика\",\"status\":\"IN_PROGRESS\"}";
        String subtask = "{\"epicId\":1,\"id\": 2,\"title\":\"сабтаск\",\"description\":\"описание саба\",\"status\":\"IN_PROGRESS\",\"duration\":\"PT45M\",\"startTime\":\"2024-05-10T19:50\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest postSubTaskRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtask))
                .setHeader("Content-Type", "application/json")
                .uri(uriPostSubtask)
                .build();
        client.send(postSubTaskRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uriGet)
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, postResponse.statusCode());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void nonExistingEpic() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/epics/");
        URI uriPostSubtask = URI.create("http://localhost:8080/subtasks/");
        URI uriGet = URI.create("http://localhost:8080/epics/2");
        String body = "{\"subTasksIds\":[2],\"id\":1,\"title\":\"эпик\",\"description\":\"описание эпика\",\"status\":\"IN_PROGRESS\"}";
        String subtask = "{\"epicId\":1,\"id\": 2,\"title\":\"сабтаск\",\"description\":\"описание саба\",\"status\":\"IN_PROGRESS\",\"duration\":\"PT45M\",\"startTime\":\"2024-05-10T19:50\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest postSubTaskRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtask))
                .setHeader("Content-Type", "application/json")
                .uri(uriPostSubtask)
                .build();
        client.send(postSubTaskRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uriGet)
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    void getEpicSubtasks() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/epics/");
        URI uriPostSubtask = URI.create("http://localhost:8080/subtasks/");
        URI uriGetSubtasks = URI.create("http://localhost:8080/epics/1/subtasks");
        String body = "{\"subTasksIds\":[2],\"id\":1,\"title\":\"эпик\",\"description\":\"описание эпика\",\"status\":\"IN_PROGRESS\"}";
        String subtask = "{\"epicId\":1,\"id\": 2,\"title\":\"сабтаск\",\"description\":\"описание саба\",\"status\":\"IN_PROGRESS\",\"duration\":\"PT45M\",\"startTime\":\"2024-05-10T19:50\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest postSubTaskRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtask))
                .setHeader("Content-Type", "application/json")
                .uri(uriPostSubtask)
                .build();
        client.send(postSubTaskRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uriGetSubtasks)
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("[2]", response.body());
    }

    @Test
    void deleteEpic() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/epics/");
        URI uriDelete = URI.create("http://localhost:8080/epics/1");
        URI uriPostSubtask = URI.create("http://localhost:8080/subtasks/");
        String body = "{\"subTasksIds\":[2],\"id\":1,\"title\":\"эпик\",\"description\":\"описание эпика\",\"status\":\"IN_PROGRESS\"}";
        String subtask = "{\"epicId\":1,\"id\": 2,\"title\":\"сабтаск\",\"description\":\"описание саба\",\"status\":\"IN_PROGRESS\",\"duration\":\"PT45M\",\"startTime\":\"2024-05-10T19:50\"}";

        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest postSubTaskRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtask))
                .setHeader("Content-Type", "application/json")
                .uri(uriPostSubtask)
                .build();
        client.send(postSubTaskRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uriDelete)
                .build();
        HttpResponse<String> newResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, newResponse.statusCode());
        Assertions.assertEquals("Задача удалена", newResponse.body());
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/tasks/");
        URI uriGet = URI.create("http://localhost:8080/tasks/1");
        URI uriGet2 = URI.create("http://localhost:8080/tasks/2");
        URI uriHistory = URI.create("http://localhost:8080/history/");
        String body = "{\"id\":1,\"title\":\"Задание\",\"description\":\"Описание\",\"status\":\"NEW\",\"duration\":\"PT30M\",\"startTime\":\"2024-12-12T12:30\"}";
        String body2 = "{\"id\":2,\"title\":\"Задание2\",\"description\":\"Описание\",\"status\":\"NEW\",\"duration\":\"PT30M\",\"startTime\":\"2025-12-12T12:30\"}";


        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uriGet)
                .build();
        client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest postRequest_2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body2))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest_2, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest_2 = HttpRequest.newBuilder()
                .GET()
                .uri(uriGet2)
                .build();
        client.send(getRequest_2, HttpResponse.BodyHandlers.ofString());

        HttpRequest getHistoryRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uriHistory)
                .build();
        HttpResponse<String> historySuccessful = client.send(getHistoryRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, historySuccessful.statusCode());
        Assertions.assertEquals("[" + body + "," + body2 + "]", historySuccessful.body());
    }

    @Test
    void prioritizedTasks() throws IOException, InterruptedException {
        URI uriPost = URI.create("http://localhost:8080/tasks/");
        URI uriPrioritized = URI.create("http://localhost:8080/prioritized/");
        String body = "{\"id\":1,\"title\":\"Задание\",\"description\":\"Описание\",\"status\":\"NEW\",\"duration\":\"PT30M\",\"startTime\":\"2025-12-12T12:30\"}";
        String body2 = "{\"id\":2,\"title\":\"Задание2\",\"description\":\"Описание\",\"status\":\"NEW\",\"duration\":\"PT30M\",\"startTime\":\"2024-12-12T12:30\"}";


        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest postSubRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body2))
                .setHeader("Content-Type", "application/json")
                .uri(uriPost)
                .build();
        client.send(postSubRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getHistoryRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uriPrioritized)
                .build();
        HttpResponse<String> historySuccessful = client.send(getHistoryRequest, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, historySuccessful.statusCode());
        Assertions.assertEquals("[" + body2 + "," + body + "]", historySuccessful.body());
    }

    @AfterEach
    void afterEach() {
        server.stop();
        client.close();
    }
}