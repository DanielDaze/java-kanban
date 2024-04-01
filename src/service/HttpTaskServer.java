package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import exception.DateTimeConflict;
import model.task.Epic;
import model.task.SubTask;
import model.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class HttpTaskServer {
    HttpServer server;
    private static final TaskManager manager = Managers.getDefault();
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    private static class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
            jsonWriter.value(String.valueOf(localDateTime));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString());
        }
    }

    private static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
            jsonWriter.value(String.valueOf(duration));
        }

        @Override
        public Duration read(JsonReader jsonReader) throws IOException {
            return Duration.parse(jsonReader.nextString());
        }
    }

    private class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    sendResponse(exchange, 400, "Такой метод не предусмотрен сервером");
                    break;
            }
        }

        private void handleGet(HttpExchange exchange) throws IOException {
            System.out.println("Пришел запрос GET по пути /tasks");
            String uri = exchange.getRequestURI().toString();
            String[] uriSplit = uri.split("/");
            Map<Integer, Task> taskMap = manager.getTasks();
            if (taskMap.isEmpty()) {
                sendResponse(exchange, 200, "Список задач пуст!");
            } else if (uriSplit.length == 2) {
                List<Task> taskList = new ArrayList<>(taskMap.values());
                sendResponse(exchange, 200, gson.toJson(taskList));
            } else {
                try {
                    int id = Integer.parseInt(uriSplit[2]);
                    if (taskMap.containsKey(id)) {
                        Task task = manager.getTaskById(id);
                        sendResponse(exchange, 200, gson.toJson(task));
                    } else {
                        sendResponse(exchange, 404, "Такой задачи нет!");
                    }
                } catch (NumberFormatException e) {
                    sendResponse(exchange, 400, "id задачи был введен некорректно!");
                }
            }
        }

        private void handlePost(HttpExchange exchange) throws IOException {
            System.out.println("Пришел запрос POST по пути /tasks");
            String uri = exchange.getRequestURI().toString();
            String[] uriSplit = uri.split("/");
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);
            try {
                if (uriSplit.length == 2) {
                    manager.createTask(task);
                    sendResponse(exchange, 201, "Задача добавлена");
                } else {
                    try {
                        int id = Integer.parseInt(uriSplit[2]);
                        manager.updateTask(task, id);
                        sendResponse(exchange, 201, "Задача отредактирована");
                    } catch (NumberFormatException e) {
                        sendResponse(exchange, 400, "id задачи был введен некорректно!");
                    }
                }
            } catch (DateTimeConflict e) {
                sendResponse(exchange, 406, "Данная задача пересекается по времени с существующими");
            }
        }

        private void handleDelete(HttpExchange exchange) throws IOException {
            System.out.println("Пришел запрос DELETE по пути /tasks");
            String uri = exchange.getRequestURI().toString();
            String[] uriSplit = uri.split("/");
            try {
                int id = Integer.parseInt(uriSplit[2]);
                manager.removeTaskById(id);
                sendResponse(exchange, 200, "Задача удалена");
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "id задачи был введен некорректно!");
            }
        }

        private void sendResponse(HttpExchange exchange, int code, String body) throws IOException {
            exchange.sendResponseHeaders(code, 0);
            if (!body.isBlank()) {
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body.getBytes());
                }
            }
        }
    }

    private class SubTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    sendResponse(exchange, 400, "Такой метод не предусмотрен сервером");
                    break;
            }
        }

        private void handleGet(HttpExchange exchange) throws IOException {
            System.out.println("Пришел запрос GET по пути /subtasks");
            String uri = exchange.getRequestURI().toString();
            String[] uriSplit = uri.split("/");
            Map<Integer, SubTask> taskMap = manager.getSubTasks();
            if (taskMap.isEmpty()) {
                sendResponse(exchange, 200, "Список подзадач пуст!");
            } else if (uriSplit.length == 2) {
                List<SubTask> taskList = new ArrayList<>(taskMap.values());
                sendResponse(exchange, 200, gson.toJson(taskList));
            } else {
                try {
                    int id = Integer.parseInt(uriSplit[2]);
                    if (taskMap.containsKey(id)) {
                        SubTask subTask = manager.getSubTaskById(id);
                        sendResponse(exchange, 200, gson.toJson(subTask));
                    } else {
                        sendResponse(exchange, 404, "Такой подзадачи нет!");
                    }
                } catch (NumberFormatException e) {
                    sendResponse(exchange, 400, "id подзадачи был введен некорректно!");
                }
            }
        }

        private void handlePost(HttpExchange exchange) throws IOException {
            System.out.println("Пришел запрос POST по пути /subtasks");
            String uri = exchange.getRequestURI().toString();
            String[] uriSplit = uri.split("/");
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            SubTask task = gson.fromJson(body, SubTask.class);
            try {
                if (uriSplit.length == 2) {
                    manager.createSubTask(task);
                    sendResponse(exchange, 201, "Подзадача добавлена");
                } else {
                    try {
                        int id = Integer.parseInt(uriSplit[2]);
                        manager.updateSubTask(task, id);
                        sendResponse(exchange, 201, "Подзадача отредактирована");
                    } catch (NumberFormatException e) {
                        sendResponse(exchange, 400, "id подзадачи был введен некорректно!");
                    }
                }
            } catch (DateTimeConflict e) {
                sendResponse(exchange, 406, "Данная задача пересекается по времени с существующими");
            }
        }

        private void handleDelete(HttpExchange exchange) throws IOException {
            System.out.println("Пришел запрос DELETE по пути /subtasks");
            String uri = exchange.getRequestURI().toString();
            String[] uriSplit = uri.split("/");
            try {
                int id = Integer.parseInt(uriSplit[2]);
                manager.removeSubTaskById(id);
                sendResponse(exchange, 200, "Подзадача удалена");
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "id подзадачи был введен некорректно!");
            }
        }

        private void sendResponse(HttpExchange exchange, int code, String body) throws IOException {
            exchange.sendResponseHeaders(code, 0);
            if (!body.isBlank()) {
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body.getBytes());
                }
            }
        }
    }

    private class EpicsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    sendResponse(exchange, 400, "Такой метод не предусмотрен сервером");
                    break;
            }
        }

        private void handleGet(HttpExchange exchange) throws IOException {
            System.out.println("Пришел запрос GET по пути /epics");
            String uri = exchange.getRequestURI().toString();
            String[] uriSplit = uri.split("/");
            Map<Integer, Epic> taskMap = manager.getEpics();
            if (taskMap.isEmpty()) {
                sendResponse(exchange, 200, "Список эпиков пуст!");
            } else if (uriSplit.length == 2) {
                List<Epic> taskList = new ArrayList<>(taskMap.values());
                sendResponse(exchange, 200, gson.toJson(taskList));
            } else if (uriSplit.length == 3) {
                try {
                    int id = Integer.parseInt(uriSplit[2]);
                    if (taskMap.containsKey(id)) {
                        Epic epic = manager.getEpicById(id);
                        sendResponse(exchange, 200, gson.toJson(epic));
                    } else {
                        sendResponse(exchange, 404, "Такого эпика нет!");
                    }
                } catch (NumberFormatException e) {
                    sendResponse(exchange, 400, "id эпика был введен некорректно!");
                }
            } else {
                try {
                    int id = Integer.parseInt(uriSplit[2]);
                    if (taskMap.containsKey(id)) {
                        Epic epic = manager.getEpicById(id);
                        sendResponse(exchange, 200, gson.toJson(epic.getSubTasksIds()));
                    } else {
                        sendResponse(exchange, 404, "Такого эпика нет!");
                    }
                } catch (NumberFormatException e) {
                    sendResponse(exchange, 400, "id эпика был введен некорректно!");
                }
            }
        }

        private void handlePost(HttpExchange exchange) throws IOException {
            System.out.println("Пришел запрос POST по пути /epics");
            String uri = exchange.getRequestURI().toString();
            String[] uriSplit = uri.split("/");
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic task = gson.fromJson(body, Epic.class);
            try {
                if (uriSplit.length == 2) {
                    manager.createEpic(task);
                    sendResponse(exchange, 201, "Эпик добавлен");
                } else {
                    try {
                        int id = Integer.parseInt(uriSplit[2]);
                        manager.updateEpic(task, id);
                        sendResponse(exchange, 201, "Эпик отредактирован");
                    } catch (NumberFormatException e) {
                        sendResponse(exchange, 400, "id эпика был введен некорректно!");
                    }
                }
            } catch (DateTimeConflict e) {
                sendResponse(exchange, 406, "Данная задача пересекается по времени с существующими");
            }
        }

        private void handleDelete(HttpExchange exchange) throws IOException {
            System.out.println("Пришел запрос DELETE по пути /epics");
            String uri = exchange.getRequestURI().toString();
            String[] uriSplit = uri.split("/");
            try {
                int id = Integer.parseInt(uriSplit[2]);
                manager.removeEpicById(id);
                sendResponse(exchange, 200, "Задача удалена");
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "id эпика был введен некорректно!");
            }
        }

        private void sendResponse(HttpExchange exchange, int code, String body) throws IOException {
            exchange.sendResponseHeaders(code, 0);
            if (!body.isBlank()) {
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body.getBytes());
                }
            }
        }
    }

    private class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("GET")) {
                handleGet(exchange);
            } else {
                sendResponse(exchange, 400, "Такой метод не предусмотрен сервером");
            }
        }

        private void handleGet(HttpExchange exchange) throws IOException {
            System.out.println("Пришел запрос GET по пути /history");
            List<Task> history = manager.getHistory();
            if (history.isEmpty()) {
                sendResponse(exchange, 200, "История пуста!");
            } else {
                sendResponse(exchange, 200, gson.toJson(history));
            }
        }

        private void sendResponse(HttpExchange exchange, int code, String body) throws IOException {
            exchange.sendResponseHeaders(code, 0);
            if (!body.isBlank()) {
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body.getBytes());
                }
            }
        }
    }

    private class PrioritizedHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("GET")) {
                handleGet(exchange);
            } else {
                sendResponse(exchange, 400, "Такой метод не предусмотрен сервером");
            }
        }

        private void handleGet(HttpExchange exchange) throws IOException {
            System.out.println("Пришел запрос GET по пути /prioritized");
            TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();
            if (prioritizedTasks.isEmpty()) {
                sendResponse(exchange, 200, "Список задач пуст!");
            } else {
                sendResponse(exchange, 200, gson.toJson(prioritizedTasks));
            }
        }

        private void sendResponse(HttpExchange exchange, int code, String body) throws IOException {
            exchange.sendResponseHeaders(code, 0);
            if (!body.isBlank()) {
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body.getBytes());
                }
            }
        }
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(2);
    }

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TasksHandler());
        server.createContext("/subtasks", new SubTasksHandler());
        server.createContext("/epics", new EpicsHandler());
        server.createContext("/history", new HistoryHandler());
        server.createContext("/prioritized", new PrioritizedHandler());
    }
}