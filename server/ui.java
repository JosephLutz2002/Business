import org.w3c.dom.Text;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class ui extends Application {

    class DisplayArea extends HBox {
        public DisplayArea(List<Unit> units) {
            setSpacing(10);
            setPadding(new Insets(10));
    
            // Iterate through the list of units and create circles for each unit
            for (Unit unit : units) {
                Circle circle = createUnitCircle(unit.isActive());
                getChildren().add(circle);
            }
        }
    
        private Circle createUnitCircle(boolean isActive) {
            Circle circle = new Circle(10);
            circle.setFill(isActive ? Color.GREEN : Color.RED);
            circle.setStroke(Color.BLACK);
            return circle;
        }
    }
    
    class Unit {
        private String name;
        private boolean isActive;
    
        public Unit(String name, boolean isActive) {
            this.name = name;
            this.isActive = isActive;
        }
    
        public String getName() {
            return name;
        }
    
        public boolean isActive() {
            return isActive;
        }
    }

    private TextArea logTextArea;
    private TextArea deviceStatusTextArea;
    private TextFlow deviceStatusTextFlow;
    List<Unit> units = List.of(
                new Unit("Unit 1", true),
                new Unit("Unit 2", false),
                new Unit("Unit 3", true)
        );

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Log and Status Viewer");

        // Create UI components
        logTextArea = new TextArea();
        deviceStatusTextArea = new TextArea();
        deviceStatusTextArea.setEditable(false); // Make it read-only
        deviceStatusTextFlow = new TextFlow();

        // Create tabs
        Tab logTab = new Tab("Logs", logTextArea);
        Tab statusTab = new Tab("Device Status", deviceStatusTextArea);

        // Create a TabPane to hold the tabs
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(logTab, statusTab);

        // Create the root layout
        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);

        // Set up some initial log and status entries for demonstration purposes
        log("Application started");
        updateDeviceStatus("Device 1", true); // Online
        updateDeviceStatus("Device 2", false); // Offline

        // Show the stage
        primaryStage.show();
        startServerThread(8080);
    }

    // Method to log messages to the logTextArea
    private void log(String message) {
        logTextArea.appendText(message + "\n");
    }

    // Method to update device status in the deviceStatusTextArea
    private void updateDeviceStatus(String deviceName, boolean connected) {
        String status = connected ? "Online" : "Offline";
        Color color = connected ? Color.GREEN : Color.RED;
    
        Text statusText = new Text(deviceName + ": " + status + "\n");
        statusText.setFont(Font.font("Arial", 12)); // Set the font
        statusText.setFill(color);
    
        deviceStatusTextFlow.getChildren().add(statusText);
    }

     private void startServerThread(int port) {
        serverThread = new ServerThread(port, this::log);
        new Thread(serverThread).start();
    }

    private void stopServerThread() {
        if (serverThread != null) {
            serverThread.setIsRunning(false);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
