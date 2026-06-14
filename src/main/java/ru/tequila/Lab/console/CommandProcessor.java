package ru.tequila.Lab.console;

import ru.tequila.Lab.console.commands.*;
import ru.tequila.Lab.service.ContainerService;
import java.util.HashMap;
import java.util.Map;

public class CommandProcessor {
    private final Map<String, Object> commands = new HashMap<>();

    public CommandProcessor(ContainerService containerService) {
        ContainerUpdateCommand cuc = new ContainerUpdateCommand(containerService);
        BoxSearchCommand bsc = new BoxSearchCommand(containerService);
        DeleteCommand dc = new DeleteCommand(containerService);
        ContainerStatusCommand csc = new ContainerStatusCommand(containerService);
        ContainerListCommand clc = new ContainerListCommand(containerService);
        ContainerAddCommand cac = new ContainerAddCommand(containerService);
        SampleFreeCommand sfc = new SampleFreeCommand(containerService);
        ContainerShowCommand csc2 = new ContainerShowCommand(containerService);
        BoxAddCommand bac = new BoxAddCommand(containerService);
        SamplePlaceCommand spc = new SamplePlaceCommand(containerService);
        BoxListCommand blc = new BoxListCommand(containerService);

        commands.put(cuc.name(), cuc);
        commands.put(bsc.name(), bsc);
        commands.put(dc.name(), dc);
        commands.put(csc.name(), csc);
        commands.put(clc.name(), clc);
        commands.put(cac.name(), cac);
        commands.put(sfc.name(), sfc);
        commands.put(csc2.name(), csc2);
        commands.put(bac.name(), bac);
        commands.put(spc.name(), spc);
        commands.put(blc.name(), blc);
    }

    public void processCommand(String line) {
        if (line == null || line.trim().isEmpty()) return;
        String[] args = line.split("\\s+");
        String commandName = args[0];

        Object cmd = commands.get(commandName);
        if (cmd == null) {
            System.out.println("Неизвестная команда: " + commandName);
            return;
        }

        if (cmd instanceof ContainerUpdateCommand) {
            ((ContainerUpdateCommand) cmd).execute(args);
        } else if (cmd instanceof BoxSearchCommand) {
            ((BoxSearchCommand) cmd).execute(args);
        } else if (cmd instanceof DeleteCommand) {
            ((DeleteCommand) cmd).execute(args);
        } else if (cmd instanceof ContainerStatusCommand) {
            ((ContainerStatusCommand) cmd).execute(args);
        } else if (cmd instanceof ContainerListCommand) {
            ((ContainerListCommand) cmd).execute(args);
        } else if (cmd instanceof ContainerAddCommand) {
            ((ContainerAddCommand) cmd).execute(args);
        } else if (cmd instanceof SampleFreeCommand) {
            ((SampleFreeCommand) cmd).execute(args);
        } else if (cmd instanceof ContainerShowCommand) {
            ((ContainerShowCommand) cmd).execute(args);
        } else if (cmd instanceof BoxAddCommand) {
            ((BoxAddCommand) cmd).execute(args);
        } else if (cmd instanceof SamplePlaceCommand) {
            ((SamplePlaceCommand) cmd).execute(args);
        } else if (cmd instanceof BoxListCommand) {
            ((BoxListCommand) cmd).execute(args);
        }
    }
}