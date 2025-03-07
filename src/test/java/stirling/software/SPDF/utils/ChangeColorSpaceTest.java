package stirling.software.SPDF.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Graphics;
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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.awt.color.ICC_ColorSpace;
import stirling.software.SPDF.utils.ChangeColorSpace;
import java.awt.color.ICC_Profile;

import stirling.software.SPDF.service.ChangeColorSpaceService;

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
        ChangeColorSpaceService changeColorSpaceService = null;

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
                        assertEquals(4, img.getBitsPerComponent());
                        BufferedImage buf_img = img.getImage();
                        int[] red = {137, 70, 32};
                        int[] yellow = {249, 215, 154};
                        int[] cyan = {128, 163, 209};
                        int[] blue = {0, 44, 69};
                        int[] green = {101, 112, 124};
                        int[] magenta = {60, 56, 52};
                        assertEquals(red, getRGBVals(buf_img.getRGB(0,0)));
                        assertEquals(green, getRGBVals(buf_img.getRGB(1,0)));
                        assertEquals(blue, getRGBVals(buf_img.getRGB(2,0)));
                        assertEquals(yellow, getRGBVals(buf_img.getRGB(3,0)));
                        assertEquals(magenta, getRGBVals(buf_img.getRGB(4,0)));
                        assertEquals(cyan, getRGBVals(buf_img.getRGB(5,0)));
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
