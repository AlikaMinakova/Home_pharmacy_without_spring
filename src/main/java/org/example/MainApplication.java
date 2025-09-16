package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class MainApplication {
    public static final int PORT = 8080;

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);

        // Указываем папку с web.xml
        String webAppDir = "src/main/webapp"; // тут должен быть WEB-INF/web.xml
        File webApp = new File(webAppDir);

        // contextPath = "" → доступ по http://localhost:8080/
        Context ctx = tomcat.addWebapp("", webApp.getAbsolutePath());

        tomcat.getConnector();

        System.out.println("Starting Tomcat on http://localhost:" + PORT + "/symptoms");

        tomcat.start();
        tomcat.getServer().await();
    }
}