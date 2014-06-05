package fr.utbm.gi.vi51.project.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.util.Random;
import javax.swing.ImageIcon;

public class ImageUtils {

 public static Image rotateImage(Image img,double angle){
     BufferedImage bufImg = toBufferedImage(img);

     return tilt(bufImg,angle);
 }
 
 public static String randomColor() {
        Random random = new Random(); // Probably really put this somewhere where it gets executed only once
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return Integer.toHexString(new Color(red, green, blue).getRGB());
    }
 
 
 public static BufferedImage changeColor(BufferedImage bImage, int color) {
            int w = bImage.getWidth(null);
            int h = bImage.getHeight(null);
            BufferedImage bImage2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < w; i++) {
              for (int j = 0; j < h; j++) {
                int p = bImage.getRGB(i, j);
                //int a = (((p >> 16) & 0xff) + ((p >> 8) & 0xff) + (p & 0xff)) / 3;
                if (p == 0xffffffff)
                    bImage2.setRGB(i, j, color);
              }
            }
            return bImage2;
          }

 public static BufferedImage tilt(BufferedImage image, double angle) {
     double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
     int w = image.getWidth(), h = image.getHeight();
     int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
     GraphicsConfiguration gc = getDefaultConfiguration();
     BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
     Graphics2D g = result.createGraphics();
     g.translate((neww-w)/2, (newh-h)/2);
     g.rotate(angle, w/2, h/2);
     g.drawRenderedImage(image, null);
     g.dispose();
     return result;
 }

 public static GraphicsConfiguration getDefaultConfiguration() {
     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
     GraphicsDevice gd = ge.getDefaultScreenDevice();
     return gd.getDefaultConfiguration();
 }
 
 
 
 public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
 

 // http://www.exampledepot.com/egs/java.awt.image/Image2Buf.html
 // An Image object cannot be converted to a BufferedImage object.
 // The closest equivalent is to create a buffered image and then draw the image on the buffered image.
 // This example defines a method that does this.

 // This method returns a buffered image with the contents of an image
 /*public static BufferedImage toBufferedImage(Image image) {
     if (image instanceof BufferedImage) {
         return (BufferedImage)image;
     }

     // This code ensures that all the pixels in the image are loaded
     image = new ImageIcon(image).getImage();

     // Determine if the image has transparent pixels; for this method's
     // implementation, see e661 Determining If an Image Has Transparent Pixels
     boolean hasAlpha = hasAlpha(image);

     // Create a buffered image with a format that's compatible with the screen
     BufferedImage bimage = null;
     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
     try {
         // Determine the type of transparency of the new buffered image
         int transparency = Transparency.OPAQUE;
         if (hasAlpha) {
             transparency = Transparency.BITMASK;
         }

         // Create the buffered image
         GraphicsDevice gs = ge.getDefaultScreenDevice();
         GraphicsConfiguration gc = gs.getDefaultConfiguration();
         bimage = gc.createCompatibleImage(
             image.getWidth(null), image.getHeight(null), transparency);
     } catch (HeadlessException e) {
         // The system does not have a screen
     }

     if (bimage == null) {
         // Create a buffered image using the default color model
         int type = BufferedImage.TYPE_INT_RGB;
         if (hasAlpha) {
             type = BufferedImage.TYPE_INT_ARGB;
         }
         bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
     }

     // Copy image to buffered image
     Graphics g = bimage.createGraphics();

     // Paint the image onto the buffered image
     g.drawImage(image, 0, 0, null);
     g.dispose();

     return bimage;
 }*/

 // http://www.exampledepot.com/egs/java.awt.image/HasAlpha.html
 // This method returns true if the specified image has transparent pixels
 public static boolean hasAlpha(Image image) {
     // If buffered image, the color model is readily available
     if (image instanceof BufferedImage) {
         BufferedImage bimage = (BufferedImage)image;
         return bimage.getColorModel().hasAlpha();
     }

     // Use a pixel grabber to retrieve the image's color model;
     // grabbing a single pixel is usually sufficient
      PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
     try {
         pg.grabPixels();
     } catch (InterruptedException e) {
     }

     // Get the image's color model
     ColorModel cm = pg.getColorModel();
     return cm.hasAlpha();
 }
}
