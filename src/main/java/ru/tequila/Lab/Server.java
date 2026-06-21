package ru.tequila.Lab;

import ru.tequila.Lab.repository.UserRepository;
import ru.tequila.Lab.service.UserService;
import ru.tequila.Lab.net.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class Server {
        private static final int PORT = 8080;
        private static final String DB_URL = "jdbc:sqlite:lab_storage.db";

        public static void main(String[] args) {
            System.out.println("ЗАПУСК ЛАБЫ 4113");

            try {
                Connection connection = DriverManager.getConnection(DB_URL);
                System.out.println("ура подключение к СУБД SQLite.");

                UserRepository userRepository = new UserRepository(connection);
                UserService userService = new UserService(userRepository);
                System.out.println("архитектура слой UserRepository и UserService связаны");

                try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                    System.out.println("сеть сервер запущен и ожидает подключений на порту " + PORT + "...");

                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("сеть новое подключение с адреса: " + clientSocket.getRemoteSocketAddress());
                        ClientHandler handler = new ClientHandler(clientSocket, userService);
                        Thread clientThread = new Thread(handler);
                        clientThread.start();
                    }
                }

            } catch (SQLException e) {
                System.err.println("ошибка бд: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("ошибка сети: " + e.getMessage());
            }
        }
    }
