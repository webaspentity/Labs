package com.evseev;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class JImageDisplay extends JComponent {
    private BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }

    /**Конструктор инициализации*/
    public JImageDisplay(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        super.setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    /** Устанавливает все пиксели изображения в черный цвет*/
    public void clearImage() {
        int color = 0;
        for (int y=0 ; y < image.getHeight() ; y++) {
            for (int x=0 ; x < image.getWidth() ; x++)
            {
                image.setRGB(x, y, color);
            }
        }
    }

    /**Устанавливает пиксель в заданный цвет*/
    public void drawPixel(int x, int y, int rgbColor) {
        image.setRGB(x, y, rgbColor);
    }
}
