/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.orbisgis.core.renderer.se.parameter.color;

import com.sun.media.jai.widget.DisplayJAI;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import junit.framework.TestCase;

/**
 *
 * @author maxence
 */
public class ColorHelperTest extends TestCase {
    
    public ColorHelperTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getColorWithAlpha method, of class ColorHelper.
     */
    public void testGetColorWithAlpha() {
        System.out.println("getColorWithAlpha");
        Color c = new Color(40, 40, 40);
        double alpha = 0.5;
        Color expResult = new Color(40, 40, 40, 127);
        Color result = ColorHelper.getColorWithAlpha(c, alpha);
        assertEquals(expResult, result);
        alpha = -1.0;
        result = ColorHelper.getColorWithAlpha(c, alpha);
        expResult = new Color(40, 40, 40, 0);
        alpha = 2.0;
        result = ColorHelper.getColorWithAlpha(c, alpha);
        expResult = new Color(40, 40, 40, 255);
    }

    /**
     * Test of invert method, of class ColorHelper.
     */
    public void testInvert() {
        System.out.println("invert");
        Color c = new Color(40, 40, 40);
        Color expResult = new Color(215, 215, 215);
        Color result = ColorHelper.invert(c);
        assertEquals(expResult, result);
        //When the inverted color is too dark or too light, we are supposed to obtain some default yellow.
        c = new Color(5,5,5);
        expResult = new Color(ColorHelper.MAX_RGB_VALUE, ColorHelper.MAX_RGB_VALUE, 40);
        result = ColorHelper.invert(c);
        assertEquals(expResult, result);
        c = new Color(250,250,250);
        expResult = new Color(ColorHelper.MAX_RGB_VALUE, ColorHelper.MAX_RGB_VALUE, 40);
        result = ColorHelper.invert(c);
        assertEquals(expResult, result);
        
    }

    public void testColorSpace() throws IOException{
        BufferedImage colorSpace = ColorHelper.getColorSpaceImage();
            File file = new File("/tmp/colorSpace.png");
            ImageIO.write(colorSpace, "png", file);
    }

}
