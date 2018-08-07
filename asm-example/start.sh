#!/usr/bin/env bash

mvn clean install -DskipTests
java -javaagent:target/asm-1.0.0-SNAPSHOT.jar com.brook.asm.UserDemo