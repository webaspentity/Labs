package com.evseev;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

/**Класс для создания и отображения фрактала*/
public class FractalExplorer {
    private int displaySize;
    private JImageDisplay image;
    private Rectangle2D.Double range;
    private JLabel label;
    private JButton resetButton;
    private JButton saveButton;
    private JComboBox comboBox;
    private JLabel comboLabel;
    private JFileChooser fileChooser;
    private JFrame frame;
    private FractalGenerator fractal;
    DefaultComboBoxModel<String> model;

    public JImageDisplay getImage() {
        return image;
    }

    /**Конструктор инициализации*/
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

    /**Создание графического интерфейса*/
    public void createAndShowGUI() {
        frame = new JFrame("Fractal Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resetButton = new JButton("Reset Display");
        saveButton = new JButton("Save");
        comboLabel = new JLabel("Fractal: ");
        label = new JLabel();
        image = new JImageDisplay(displaySize, displaySize);

        JPanel topPanel = new JPanel();
        JPanel botPanel = new JPanel();

        topPanel.add(comboLabel);
        topPanel.add(comboBox);
        topPanel.add(label);
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

    /**Отрисовка фрактала*/
    public void drawFractal() {
        for (int y = 0 ; y < displaySize ; y++) {
            for (int x = 0 ; x < displaySize ; x++)
            {
                double xCoord =
                        fractal.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord =
                        fractal.getCoord(range.y, range.y + range.height, displaySize, y);

                if (fractal.numIterations(xCoord, yCoord) == -1) {
                    image.drawPixel(x, y, 0);
                }
                else {
                    image.drawPixel(x, y, fractal.numIterations(xCoord, yCoord) << 19);
                }
                image.repaint();
            }
        }
    }

    /**Обработчик щелчка по кнопке*/
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
     * Масштабирование изображения
     */
    private class MListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            double xCoord =
                    fractal.getCoord(range.x, range.x + range.width, displaySize, x);
            double yCoord =
                    fractal.getCoord(range.y, range.y + range.height, displaySize, y);
            label.setText("     [ " + x + " ; " + y + " ]");
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }
}
