#!/bin/bash
# Clean and compile
echo "Cleaning build..."
rm -rf build
mkdir build

echo "Compiling SaaSProduct1..."
javac -d build -cp "$HOME/UniversalFormula/dist/core-formula.jar:$HOME/UniversalFormula/dist/modules-formula.jar" src/app/MainApp.java

# Run server (foreground, blocking)
echo "Starting SaaS server..."
JAVA_CP="build:$HOME/UniversalFormula/dist/core-formula.jar:$HOME/UniversalFormula/dist/modules-formula.jar"
java -cp "$JAVA_CP" app.MainApp
echo "Server stopped."
