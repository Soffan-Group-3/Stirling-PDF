package stirling.software.SPDF.utils;

import java.awt.Graphics;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import stirling.software.SPDF.service.ChangeColorSpaceService;




public class ChangeColorSpaceTest {

    @Test
    public void testRGBtoCMYK() throws IOException{

        String iccFilePath = "src/main/resources/static/Coated_Fogra39L_VIGC_300.icc";
        ICC_Profile icc = ICC_Profile.getInstance(iccFilePath);
        ICC_ColorSpace isc = new ICC_ColorSpace(icc);
        

        
        List<Object> testTokens = new ArrayList<>();
        testTokens.add(new COSFloat(1));
        testTokens.add(new COSFloat(0));   
        testTokens.add(new COSFloat(0));   
        testTokens.add(Operator.getOperator("rg"));

        List<Object> result = ChangeColorSpace.changeRGBtoCMYK(testTokens, isc);
        System.out.println(result);

        assertEquals(5, result.size());
        assertEquals("k", ((Operator) result.get(4)).getName());

    }

    @Test
    public void test_ChangeColorSpace() throws IOException{
        ChangeColorSpaceService changeColorSpaceService = new ChangeColorSpaceService();

        File tempFile = File.createTempFile("testCMYK-", "-image");
        String iccFilePath = "src/main/resources/static/Coated_Fogra39L_VIGC_300.icc";
        ICC_Profile icc = ICC_Profile.getInstance(iccFilePath);
        tempFile.deleteOnExit();
    
        BufferedImage image = new BufferedImage(6, 1, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
    
        g.setColor(new java.awt.Color(255, 0, 0));
        g.fillRect(0, 0, 1, 1);
        g.setColor(new java.awt.Color(0, 255, 0));
        g.fillRect(1, 0, 1, 1);
        g.setColor(new java.awt.Color(0, 0, 255));
        g.fillRect(2, 0, 1, 1);
        g.setColor(new java.awt.Color(255, 255, 0));
        g.fillRect(3, 0, 1, 1);
        g.setColor(new java.awt.Color(255, 0, 255));
        g.fillRect(4, 0, 1, 1);
        g.setColor(new java.awt.Color(0, 255, 255));
        g.fillRect(5, 0, 1, 1);
    
        try 
        {
            ImageIO.write(image, "PNG", tempFile);
        } catch(IOException error) 
        {
            error.printStackTrace();
        }
    
        try (PDDocument document = new PDDocument()) {
            PDPage page_temp = new PDPage();
            document.addPage(page_temp);
    
            PDImageXObject testImage = PDImageXObject.createFromFileByContent(tempFile, document);
    
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page_temp)) {
                contentStream.drawImage(testImage, 0, 0, testImage.getWidth(), testImage.getHeight());
            }
    
            PDDocument translated_document = changeColorSpaceService.changeColorSpaceImages(document, icc);
    
            for (PDPage page : translated_document.getPages()) {
                PDResources resources = page.getResources();
                List<COSName> namesToUpdate = new ArrayList<>();
    
                for (COSName name : resources.getXObjectNames()) {
                    if (resources.isImageXObject(name)) {
                        PDImageXObject img = (PDImageXObject) resources.getXObject(name);
                        System.out.println(img.getBitsPerComponent());
                        //assertEquals(4, img.getBitsPerComponent());
                        BufferedImage buf_img = img.getImage();
                        int[] red = {241, 50, 40};
                        int[] yellow = {255, 237, 85};
                        int[] cyan = {171, 217, 219};
                        int[] blue = {31, 66, 147};
                        int[] green = {110, 181, 84};
                        int[] magenta = {177, 103, 167};
                        assertArrayEquals(red, getRGBVals(buf_img.getRGB(0,0)));
                        assertArrayEquals(green, getRGBVals(buf_img.getRGB(1,0)));
                        assertArrayEquals(blue, getRGBVals(buf_img.getRGB(2,0)));
                        assertArrayEquals(yellow, getRGBVals(buf_img.getRGB(3,0)));
                        assertArrayEquals(magenta, getRGBVals(buf_img.getRGB(4,0)));
                        assertArrayEquals(cyan, getRGBVals(buf_img.getRGB(5,0)));
                    }
                }
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private int[] getRGBVals(int clr){
        int red =   (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue =   clr & 0x000000ff;
        int[] rgb = {red,green,blue};
        return rgb;
    }

}
