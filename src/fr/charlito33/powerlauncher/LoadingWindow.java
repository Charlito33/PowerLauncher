package fr.charlito33.powerlauncher;


import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingWindow extends JFrame {
    private JPanel panel;
    private JProgressBar progressBar;

    private JLabel name;

    private boolean ready = false;
    private boolean waited = false;
    private boolean ended = false;

    public LoadingWindow(String name) {
        this.name.setText(name);

        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        Color background = new Color(43, 43, 47);

        panel.setBackground(background);
        panel.setForeground(Color.WHITE);

        progressBar.setBackground(background);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setIndeterminate(true);

        add(panel);
        pack();
        setLocationRelativeTo(null);

        setVisible(true);

        Timer timer = new Timer();
        timer.schedule(new Loading(), 2500);
    }

    public void setReady(boolean ready) {
        this.ready = ready;
        updateStatus();
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
        updateStatus();
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    private void updateStatus() {
        if (ready && waited) {
            if (ended) {
                dispose();
            } else {
                Main.checkForUpdates();
            }
        }
    }

    class Loading extends TimerTask {
        @Override
        public void run() {
            waited = true;
            updateStatus();
        }
    }
}
