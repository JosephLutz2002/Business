import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import threads.ArduinoUnit;
import threads.ping;
public class server{
    private List<ArduinoUnit> units = new ArrayList<>();
    private ping pinger;

    public server() throws FileNotFoundException{
        loadUnits();
        pinger = new ping(units);
        pinger.start();
    }

    public void loadUnits() throws FileNotFoundException{
        Scanner scFile = new Scanner(new File("list.txt"));
        while(scFile.hasNextLine()){
            String  []line = scFile.nextLine().split(" ");
            units.add(new ArduinoUnit(line[0], line[1]));
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        server t = new server();
    }
}