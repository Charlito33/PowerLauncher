package fr.charlito33.powerlauncher;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static String SERVICE_URL = "";

    public static String readFromURL(String url) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL connURL = new URL(url);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connURL.openStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append(";");
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stringBuilder.toString();
    }

    public static Map<String, String> parseFile(String file) {
        String[] lines = file.split(";");
        Map<String, String> result = new HashMap<>();

        for (String line : lines) {
            String[] lineData = line.split("=");

            result.put(lineData[0], lineData[1]);
        }

        return result;
    }

    public static void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static String getVersionFromRepoURL(String url) {
         String result = url.substring("https://github.com/".length());
         result = "https://raw.githubusercontent.com/" + result + "/main/infos.txt";
         return result;
    }

    public static void downloadFile(String fileURL, String filename, DownloadProgressCallback callback) {
        try {
            URL url = new URL(fileURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            long fileSize = httpURLConnection.getContentLength();

            BufferedInputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = inputStream.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                float progress = (float) ((double) downloadedFileSize / (double) fileSize); // Double bigger than float (64bit vs 32bit)
                callback.run(progress);
                bufferedOutputStream.write(data, 0, x);
            }
            bufferedOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showErrorMessage(Main.loadingWindow, "Download Error", "Unable to download file from : " + fileURL);
            Main.exit(1);
        }
    }

    public static interface DownloadProgressCallback {
        void run(float progress);
    }
}
