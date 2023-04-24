package org.trino.study;

import io.trino.server.TrinoServer;

public class TrinoServerMain {

    public static void main(String[] args) {
//        -Dnode.environment=production
        System.setProperty("node.environment", "production");
        System.setProperty("discovery.uri", "http://localhost:8080");
        System.setProperty("plugin.dir", "target/classes/plugin");

        TrinoServer.main(args);
    }
}
