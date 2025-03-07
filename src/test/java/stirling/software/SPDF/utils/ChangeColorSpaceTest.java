package stirling.software.SPDF.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class ErrorUtilsTest {

    @Test
    public void test_ChangeColorSpace(){

        File tempFile = File.createTempFile("testCMYK-", "-image");
        String iccFilePath = "src/main/resources/static/Coated_Fogra39L_VIGC_300.icc";
        ICC_Profile icc = ICC_Profile().getInstance(iccFilePath);
        tempFile.deleteOnExit();
    
        BufferedImage image = new BufferedImage(6, 1, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
    
        g.setColor(new java.awt.Color(1.0, 0.0, 0.0));
        g.fillRect(0, 0, 1, 1);
        g.setColor(new java.awt.Color(0.0, 1.0, 0.0));
        g.fillRect(1, 0, 1, 1);
        g.setColor(new java.awt.Color(0.0, 0.0, 1.0));
        g.fillRect(2, 0, 1, 1);
        g.setColor(new java.awt.Color(1.0, 1.0, 0.0));
        g.fillRect(3, 0, 1, 1);
        g.setColor(new java.awt.Color(1.0, 0.0, 1.0));
        g.fillRect(4, 0, 1, 1);
        g.setColor(new java.awt.Color(0.0, 1.0, 1.0));
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
    
            PDImageXObject testImage = PDImageXObject.createFromFile(tempFile, document);
    
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page_temp)) {
                contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth(), pdImage.getHeight());
            }
    
            PDDocument translated_document = changeColorSpaceImages(document, icc);
    
            for (PDPage page : translated_document.getPages()) {
                PDResources resources = page.getResources();
                List<COSName> namesToUpdate = new ArrayList<>();
    
                for (COSName name : resources.getXObjectNames()) {
                    if (resources.isImageXObject(name)) {
                        PDImageXObject img = (PDImageXObject) resources.getXObject(name);
                        AssertEquals(4, img.getBitsPerComponent());
                        BufferedImage buf_img = img.getImage();
                        int[] red = {137, 70, 32};
                        int[] yellow = {249, 215, 154};
                        int[] cyan = {128, 163, 209};
                        int[] blue = {0, 44, 69};
                        int[] green = {101, 112, 124};
                        int[] magenta = {60, 56, 52};
                        AssertEquals(red, getRGBVals(buf_img.getRGB(0,0)));
                        AssertEquals(yellow, getRGBVals(buf_img.getRGB(1,0)));
                        AssertEquals(cyan, getRGBVals(buf_img.getRGB(2,0)));
                        AssertEquals(blue, getRGBVals(buf_img.getRGB(3,0)));
                        AssertEquals(green, getRGBVals(buf_img.getRGB(4,0)));
                        AssertEquals(magenta, getRGBVals(buf_img.getRGB(5,0)));
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
