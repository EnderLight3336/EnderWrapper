package me.enderlight3336.wrapper.command;

import me.enderlight3336.wrapper.extension.Extension;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

public final class CommandManager {
    @ApiStatus.Internal
    public static final AbstractCommand[] defCommand = new AbstractCommand[] {
            new StopCommand(), new HelpCommand()
    };
    @ApiStatus.Internal
    public static final Reference2ObjectArrayMap<Extension, List<AbstractCommand>> extCommand = new Reference2ObjectArrayMap<>();
    @SuppressWarnings("unused")
    public static void regCommand(Extension extension, AbstractCommand... command) {
        List<AbstractCommand> list = extCommand.computeIfAbsent(extension, k -> new ArrayList<>());
        Collections.addAll(list, command);
    }
}
