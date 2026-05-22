package ru.tequila.Lab.console;
import ru.tequila.Lab.console.commands.*;
import ru.tequila.Lab.repository.InMemoryContainerRepository;
import ru.tequila.Lab.service.ContainerService;
import java.util.*;

public class CommandProcessor {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandProcessor() {
        InMemoryContainerRepository repo = new InMemoryContainerRepository();
        ContainerService s = new ContainerService(repo);
        register(new ContainerAddCommand(s));
        register(new ContainerListCommand(s));
        register(new ContainerShowCommand(s));
        register(new ContainerUpdateCommand(s));
        register(new ContainerStatusCommand(s));
        register(new BoxAddCommand(s));
        register(new BoxListCommand(s));
        register(new BoxSearchCommand(s));
        register(new SamplePlaceCommand(s));
        register(new SampleFreeCommand(s));
        register(new DeleteCommand(s));
        register(new HelpCommand());
    }

    private void register(Command c) {
        commands.put(c.name(), c);
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String line = sc.nextLine();
            if (line.equals("exit")) break;
            String[] parts = line.split(" ");
            Command c = commands.get(parts[0]);
            if (c != null) c.execute(parts);
        }
    }
}