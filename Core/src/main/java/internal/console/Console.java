package internal.console;

import me.enderlight3336.wrapper.command.AbstractCommand;
import me.enderlight3336.wrapper.command.CommandManager;
import internal.log.LogUtil;
import me.enderlight3336.wrapper.log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public final class Console {
    public static final boolean ENABLE;
    public static final JFrame frame;
    public static final JPanel textPanel;
    public static final JPanel sendPanel;
    public static final JTextArea textArea;
    public static final JTextField inputArea;
    public static final JScrollPane scrollPane;
    static boolean loaded = false;
    static Map<String, AbstractCommand> commandMap;
    static final AbstractCommand command = new AbstractCommand(null) {
        @Override
        public void execute(String[] args) {
            print("Cannot find command, please run help command!");
        }
    };
    static {
        if (Boolean.parseBoolean(System.getProperty("internal.nogui")) || GraphicsEnvironment.isHeadless()) {
            ENABLE = false;
            textPanel = sendPanel = null;frame = null;textArea = null;inputArea = null;scrollPane = null;
        } else {
            ENABLE = true;
            frame = new JFrame("EnderWrapper Console");
            textPanel = new JPanel();
            sendPanel = new JPanel();
            textArea = new JTextArea();
            inputArea = new JTextField();
            scrollPane = new JScrollPane(textArea);

            frame.setLayout(new BorderLayout());
            scrollPane.setBounds(0, 0, 768, 400);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            int height=10;
            Point p = new Point();
            p.setLocation(0,textArea.getLineCount() * height);
            scrollPane.getViewport().setViewPosition(p);

            textPanel.setLayout(new BorderLayout());
            textPanel.add(scrollPane, BorderLayout.CENTER);
            inputArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        handleInput(inputArea.getText());
                    }
                }
            });
            sendPanel.add(inputArea);
            frame.setSize(384, 216);
            frame.add(textPanel, BorderLayout.CENTER);
            frame.add(inputArea, BorderLayout.SOUTH);
            frame.setAlwaysOnTop(false);
        }
    }

    public static void init() {
        if (ENABLE)
            frame.setVisible(true);
        else
            LogUtil.MAIN.info("UI Console is disabled!");
    }

    public static void print(String str) {
        printer.accept(str);
    }
    public static Consumer<String> printer = str -> {
        textArea.append(str);
        System.out.print(str);
    };

    public static void handleInput(String str) {
        if (!loaded)
            return;
        String[] string = str.split(" ");
        try {
            commandMap.getOrDefault(string[0].toLowerCase(Locale.ROOT), command).execute(string);
        } catch (Exception e) {
            LogUtil.MAIN.catchThrow(e, Logger.Level.WARN, "Error while execute command!");
        }
    }
    public static void setLoaded() {
        commandMap = new HashMap<>(CommandManager.defCommand.length + CommandManager.extCommand.size());
        for (AbstractCommand command : CommandManager.defCommand) {
            commandMap.put(command.name, command);
        }
        CommandManager.extCommand.reference2ObjectEntrySet().fastForEach(ext -> ext.getValue().forEach(c -> commandMap.put(c.name, c)));
        loaded = true;
        printer = textArea::append;
    }
    public static void shutdown() {
        if (ENABLE)
            frame.dispose();
    }
}
