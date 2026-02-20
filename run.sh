#!/bin/bash

echo "Cleaning build..."
rm -rf build
mkdir build

echo "Compiling..."
javac -d build src/app/MainApp.java

echo "Starting server..."
java -cp build app.MainApp
