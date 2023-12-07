//package threads;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ping implements Runnable {

    private static final String ARDUINO_LIST_FILE = "list.txt";
    private static String LOG_FILE = "data/logs.txt";

    private List<ArduinoUnit> arduinoUnits;
    private final Consumer<String> logAppender;

    public ping(Consumer<String> logAppender) {
     this.logAppender = logAppender;
    }

    @Override
    public void run() {
        try {
            loadArduinoUnits();
            while (true) {
                System.out.println("CLient emergency listening");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                LOG_FILE = dateFormat.format(new Date()) + ".log";
                for (ArduinoUnit arduinoUnit : arduinoUnits) {
                    System.out.println(arduinoUnit.getIpAddress());
                    boolean isReachable = ping(arduinoUnit.getIpAddress());
                    if (!isReachable) {
                        System.out.println(arduinoUnit.getArduinoId());
                        logFailure(arduinoUnit.getArduinoId(), arduinoUnit.getIpAddress());
                        logAppender.accept(arduinoUnit.getArduinoId());
                    }else{
                    }
                }
                Thread.sleep(3600000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadArduinoUnits() {
        arduinoUnits = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileReader(ARDUINO_LIST_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                    String arduinoId = parts[0].trim();
                    String ipAddress = parts[1].trim();
                    arduinoUnits.add(new ArduinoUnit(arduinoId, ipAddress));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean ping(String ipAddress) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            return address.isReachable(2000);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void logFailure(String arduinoId, String ipAddress) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String logMessage = "Arduino unit " + arduinoId + " at " + ipAddress + " did not respond.\n";
            writer.write(logMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

