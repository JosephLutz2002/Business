#!/bin/bash
modulePath="javafx-sdk-21.0.1/lib"
javac --module-path "$modulePath" --add-modules javafx.controls,javafx.base,javafx.graphics,javafx.web unitStatus.java
java --module-path "$modulePath" --add-modules javafx.controls,javafx.base,javafx.graphics,javafx.web unitStatus
rm -f *.class
read -p "Press Enter to exit..."
