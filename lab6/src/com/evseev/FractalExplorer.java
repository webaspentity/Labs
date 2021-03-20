package com.evseev;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

/**
 * Класс для создания и отображения фрактала
 * */
public class FractalExplorer {
    private int displaySize;
    private JImageDisplay image;
    private Rectangle2D.Double range;
    private JLabel coordLabel;
    private JButton resetButton;
    private JButton saveButton;
    private JComboBox comboBox;
    private JLabel comboLabel;
    private JFileChooser fileChooser;
    private JFrame frame;
    private FractalGenerator fractal;
    private int rowsRemaining;
    private DefaultComboBoxModel<String> model;

    /**
     * Конструктор инициализации
     * */
    public FractalExplorer(int size) {
        displaySize = size;
        range = new Rectangle2D.Double();
        model = new DefaultComboBoxModel<String>();
        model.addElement("Mandelbrot");
        model.addElement("Tricorn");
        model.addElement("Burning Ship");
        comboBox = new JComboBox(model);
        chooseFractal();
        fractal.getInitialRange(range);
    }

    /**
     * Определяет вид фрактала в зависимости от текущего элемента в ComboBox
     */
    private void chooseFractal() {
        switch (comboBox.getSelectedIndex()) {
            case 0:
                if (!(fractal instanceof Mandelbrot))
                    fractal = new Mandelbrot();
                break;
            case 1:
                if (!(fractal instanceof Tricorn))
                    fractal = new Tricorn();
                break;
            case 2:
                if (!(fractal instanceof Mandelbrot))
                    fractal = new BurningShip();
                break;
        }
    }

    /**
     * Создает графический интерфейс
     * */
    public void createAndShowGUI() {
        frame = new JFrame("Fractal Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color crimson = new Color(220, 20, 60);
        Color darkGray = new Color(25, 25, 25);
        frame.setForeground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(darkGray);
        JPanel botPanel = new JPanel();
        botPanel.setBackground(darkGray);

        comboBox = new JComboBox<>(model);
        resetButton = new JButton("Reset Display");
        resetButton.setPreferredSize(new Dimension(120, 30));
        resetButton.setBackground(crimson);
        resetButton.setForeground(Color.WHITE);
        resetButton.setBorderPainted(false);
        resetButton.setFocusPainted(false);
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(120, 30));
        saveButton.setBackground(crimson);
        saveButton.setForeground(Color.white);
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        comboLabel = new JLabel("Fractal: ");
        comboLabel.setForeground(Color.WHITE);
        coordLabel = new JLabel();
        coordLabel.setForeground(crimson);
        image = new JImageDisplay(displaySize, displaySize);
        topPanel.add(comboLabel);
        topPanel.add(comboBox);
        topPanel.add(coordLabel);
        botPanel.add(saveButton);
        botPanel.add(resetButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(image, BorderLayout.CENTER);
        frame.add(botPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

        resetButton.addActionListener(new AListener());
        saveButton.addActionListener(new AListener());
        comboBox.addActionListener(new AListener());
        image.addMouseListener(new MListener());
    }

    /**
     * Отрисовка фрактала
     * */
    public void drawFractal() {
        enableUI(false);
        rowsRemaining = displaySize;
        for (int y=0 ; y < displaySize ; y++) {
            new FractalWorker(y).execute();
        }
    }

    /**
     * Обработчик щелчка по изображению
     * */
    private class AListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            if (e.getSource() == comboBox || e.getSource() == resetButton) {
                chooseFractal();
                fractal.getInitialRange(range);
                drawFractal();
            }
            if (e.getSource() == saveButton) {
                fileChooser = new JFileChooser();
                String extension = ".png";
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", extension);
                fileChooser.setFileFilter(filter);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int saving = fileChooser.showSaveDialog(frame);

                if (saving == JFileChooser.APPROVE_OPTION) {
                    try {
                        String path = fileChooser.getSelectedFile().getPath();
                        ImageIO.write(image.getImage(), "png", new File(path + extension));
                    }
                    catch (IOException exc) {
                        JOptionPane.showMessageDialog(
                                frame, exc.getMessage(), "Cannot Save Image!", JOptionPane.ERROR_MESSAGE
                        );
                    }
                    JOptionPane.showMessageDialog(frame, "Image were written successfully.");
                }
            }
        }
    }

    /**
     * В зависимости от переданного аргумента включает или отключает элементы
     * пользовательского интерфейса
     * */
    private void enableUI(boolean val) {
        if (!val) {
            saveButton.setEnabled(false);
            resetButton.setEnabled(false);
            comboBox.setEnabled(false);
        }
        else {
            saveButton.setEnabled(true);
            resetButton.setEnabled(true);
            comboBox.setEnabled(true);
        }
    }

    /**
     * Масштабирование изображения
     * */
    private class MListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (rowsRemaining == 0) {
                int x = e.getX();
                int y = e.getY();
                double xCoord =
                        fractal.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord =
                        fractal.getCoord(range.y, range.y + range.height, displaySize, y);

                coordLabel.setText("     [" + x + " ; " + y + "]");
                fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
                drawFractal();
            }
        }
    }

    /**
     * Класс, отвечающий за обработку одной строки фрактала
     * */
    private class FractalWorker extends SwingWorker<Object, Object> {
        private int yCoord;
        private int[] colors;

        private FractalWorker(int y) {
            yCoord = y;
        }

        /**
         * Вычисляет значения цвета пикселей дял одной строки фрактала
         * */
        @Override
        protected Object doInBackground() {
            colors = new int[displaySize];
            for (int x = 0 ; x < displaySize ; x++)
            {
                double xCoord =
                        fractal.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord =
                        fractal.getCoord(range.y, range.y + range.height, displaySize, this.yCoord);

                if (fractal.numIterations(xCoord, yCoord) == -1) {
                    colors[x] = 0;
                }
                else {
                    colors[x] = fractal.numIterations(xCoord, yCoord) << 19;
                }
            }
            return null;
        }

        /**
         * Отвечает за отрисовку пикселей и обновление изображения
         * */
        @Override
        protected void done() {
            for (int xCoord = 0; xCoord < displaySize; xCoord++) {
                image.drawPixel(xCoord, this.yCoord, colors[xCoord]);
            }
            image.repaint(0, this.yCoord, displaySize, 1);
            rowsRemaining--;
            if (rowsRemaining == 0) enableUI(true);
        }
    }
}
