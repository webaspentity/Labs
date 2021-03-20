package com.evseev;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.awt.geom.*;

/**Класс для создания и отображения фрактала*/
public class FractalExplorer {
    private int displaySize;
    private JImageDisplay image;
    private Mandelbrot mandelbrot;
    private Rectangle2D.Double range;
    private JLabel label;

    public JImageDisplay getImage() {
        return image;
    }

    /**Конструктор инициализации*/
    public FractalExplorer(int size) {
        displaySize = size;
        range = new Rectangle2D.Double();
        mandelbrot = new Mandelbrot();
        mandelbrot.getInitialRange(range);
    }

    /**Создание графического интерфейса*/
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel labelPanel = new JPanel();

        label = new JLabel();
        image = new JImageDisplay(displaySize, displaySize);
        labelPanel.add(label);

        JButton button = new JButton("Reset Display");

        frame.add(image, BorderLayout.CENTER);
        frame.add(button, BorderLayout.SOUTH);
        frame.add(labelPanel, BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

        ActionListener actionListener = new AListener();
        button.addActionListener(actionListener);
        MouseListener mouseListener = new MListener();
        image.addMouseListener(mouseListener);
    }

    /**Отрисовка фрактала*/
    public void drawFractal() {
        for (int y = 0 ; y < displaySize ; y++) {
            for (int x = 0 ; x < displaySize ; x++)
            {
                double xCoord =
                        mandelbrot.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord =
                        mandelbrot.getCoord(range.y, range.y + range.height, displaySize, y);

                if (mandelbrot.numIterations(xCoord, yCoord) == -1) {
                    image.drawPixel(x, y, 0);
                }
                else {
                    image.drawPixel(x, y, mandelbrot.numIterations(xCoord, yCoord) << 19);
                }
                image.repaint();
            }
        }
    }

    /**Обработчик щелчка по кнопке*/
    private class AListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            mandelbrot.getInitialRange(range);
            drawFractal();
        }
    }

    /**Обработчик щелчка по изображению*/
    private class MListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            double xCoord =
                    mandelbrot.getCoord(range.x, range.x + range.width, displaySize, x);
            double yCoord =
                    mandelbrot.getCoord(range.y, range.y + range.height, displaySize, y);
            label.setText("(" + x + " ; " + y + ")");
            mandelbrot.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }
}
