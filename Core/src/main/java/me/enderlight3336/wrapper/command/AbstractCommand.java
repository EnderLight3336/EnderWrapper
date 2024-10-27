package me.enderlight3336.wrapper.command;

public abstract class AbstractCommand {
    public final String name;
    public AbstractCommand(String name) {
        this.name = name;
    }

    /**
     * Execute command
     *
     * @param args User input
     */
    public abstract void execute(String[] args) throws Exception;
}
