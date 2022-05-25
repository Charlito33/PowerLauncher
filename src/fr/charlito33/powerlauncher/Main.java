package fr.charlito33.powerlauncher;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;


// Repo : https://github.com/Charlito33/PowerLauncherTestRepo
// Raw Version File : https://raw.githubusercontent.com/Charlito33/PowerLauncherTestRepo/main/infos.txt
public class Main {
    public static Properties settings;
    public static LoadingWindow loadingWindow;
    public static Map<String, String> versionData;
    public static Map<String, Object> license;

    public static void main(String[] args) {
        settings = new Properties();
        try {
            settings.load(Files.newInputStream(Paths.get("settings.properties")));
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showErrorMessage("File Error", "Unable to load settings.properties");
            exit(1);
        }

        loadingWindow = new LoadingWindow(settings.getProperty("launcherName"));

        if (!settings.containsKey("version") || !settings.containsKey("buildNumber") || !settings.containsKey("repo") || !settings.containsKey("file")) {
            Utils.showErrorMessage(loadingWindow, "Settings Error", "Missing entries in settings.properties");
            exit(1);
        }
        if (!settings.containsKey("launcherName")) {
            settings.setProperty("launcherName", "Power Launcher");
        }

        boolean isOnline = false;

        try {
            InetAddress inetAddress = InetAddress.getByName("1.1.1.1");
            isOnline = inetAddress.isReachable(2000);
        } catch (IOException e) {
            // Offline
        }

        if (!isOnline) {
            openApp();
        }
        String versionRawURL = Utils.getVersionFromRepoURL(settings.getProperty("repo"));
        String versionFile = Utils.readFromURL(versionRawURL);
        versionData = Utils.parseFile(versionFile);

        loadingWindow.setReady(true);
    }

    public static void checkForUpdates() {
        int currentBuild = Integer.parseInt(settings.getProperty("buildNumber"));
        int latestBuild = Integer.parseInt(versionData.get("buildNumber"));

        File appFile = new File(settings.getProperty("file"));
        if (latestBuild > currentBuild || !appFile.exists()) {
            loadingWindow.getProgressBar().setMinimum(0);
            loadingWindow.getProgressBar().setMaximum(100);
            loadingWindow.getProgressBar().setIndeterminate(false);

            Utils.downloadFile(settings.getProperty("repo") + "/releases/download/" + versionData.get("version") + "/" + settings.getProperty("file"), settings.getProperty("file"), new Utils.DownloadProgressCallback() {
                @Override
                public void run(float progress) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            loadingWindow.getProgressBar().setValue((int) (progress * 100f));
                        }
                    });
                }
            });

            loadingWindow.getProgressBar().setIndeterminate(true);

            settings.setProperty("version", versionData.get("version"));
            settings.setProperty("buildNumber", versionData.get("buildNumber"));

            openApp();
        } else {
            openApp();
        }
    }

    public static void openApp() {
        File file = new File(settings.getProperty("file"));

        if (file.exists()) {
            try {
                if (settings.getProperty("file").endsWith(".jar")) {
                    ProcessBuilder processBuilder = new ProcessBuilder();
                    processBuilder.command("cmd", "/c", "java -jar ./" + settings.getProperty("file"));
                    processBuilder.start();
                } else {
                    Desktop.getDesktop().open(file);
                }
            } catch (Exception e) {
                Utils.showErrorMessage(loadingWindow, "File Error", "Unable to start " + file.getName());
                exit(1);
            }
        } else {
            Utils.showErrorMessage(loadingWindow, "File Error", "Can't find " + file.getName());
            exit(1);
        }

        loadingWindow.setEnded(true);
        exit(0);
    }

    public static void exit(int statusCode) {
        try {
            settings.store(new FileWriter("settings.properties"), "Power Launcher Settings");
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showErrorMessage(loadingWindow, "File Error", "Unable to save settings.properties");
        }

        System.exit(statusCode);
    }
}
