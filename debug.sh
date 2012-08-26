#!/bin/sh  
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000 -cp src/resources:target/Ortelium-2.0-SNAPSHOT-jar-with-dependencies.jar com.stackframe.ortelium.Ortelium
