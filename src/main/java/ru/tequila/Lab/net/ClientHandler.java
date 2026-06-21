package ru.tequila.Lab.net;

import ru.tequila.Lab.service.UserService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final UserService userService;
    private String currentSessionUser = null;

    public ClientHandler(Socket socket, UserService userService) {
        this.socket = socket;
        this.userService = userService;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        )
        {
            out.println("соединение установлено. доступны: register, login, add_box, exit");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] tokens = inputLine.trim().split("\\s+");
                if (tokens.length == 0 || tokens[0].isEmpty()) continue;

                String command = tokens[0].toLowerCase();

                switch (command) {
                    case "register":
                        if (tokens.length < 3) {
                            out.println("используйте format: register логин> <пароль");
                        } else {
                            boolean success = userService.register(tokens[1], tokens[2]);
                            if (success) out.println("регистрация завершена");
                            else out.println("не удалось зарегистрировать (возможно, имя занято)");
                        }
                        break;

                    case "login":
                        if (tokens.length < 3) {
                            out.println("используйте format login логин пароль");
                        } else {

                            boolean loggedIn = userService.login(tokens[1], tokens[2]);
                            if (loggedIn) {
                                currentSessionUser = tokens[1];
                                out.println("добро пожаловать, " + currentSessionUser);
                            } else {
                                out.println("неверные данные");
                            }
                        }
                        break;

                    case "add_box":
                        if (currentSessionUser == null) {
                            out.println("действие заблокировано. сначала выполните login");
                        } else {
                            if (tokens.length < 2) {
                                out.println("укажите имя бокса");
                            } else {
                                out.println("бокс '" + tokens[1] + "' создан пользователем " + currentSessionUser);
                            }
                        }
                        break;

                    case "exit":
                        out.println("сессия закрыта");
                        return;

                    default:
                        out.println("еизвестная команда");
                }
            }
        } catch (Exception e) {
            System.err.println("ошибка обработки клиента: " + e.getMessage());
        }
    }
}