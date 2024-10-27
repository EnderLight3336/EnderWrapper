package me.enderlight3336.wrapper.command;

public final class StopCommand extends AbstractCommand {
    public StopCommand() {
        super("stop");
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 1) {
            System.exit(0);
        } else {
            int code;
            code = Integer.parseInt(args[1]);
            System.exit(code);
        }
    }
}
