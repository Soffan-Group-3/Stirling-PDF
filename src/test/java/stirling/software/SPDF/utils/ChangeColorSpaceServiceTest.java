package stirling.software.SPDF.utils;

import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import java.awt.color.ICC_Profile;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.awt.color.ICC_Profile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import java.awt.color.ICC_ColorSpace;

import stirling.software.SPDF.service.ChangeColorSpaceService;
import stirling.software.SPDF.utils.ChangeColorSpace;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSFloat;


public class ChangeColorSpaceServiceTest {


    @Test
    public void testRGBtoCMYK(){
        ICC_ColorSpace mockIcc = Mockito.mock(ICC_ColorSpace.class);
        
        // Define the behavior for the mock: fromRGB converts RGB to CMYK
        Mockito.when(mockIcc.fromRGB(Mockito.any(float[].class))).thenReturn(new float[]{0.0f, 1.0f, 1.0f, 0.0f});
        List<Object> testTokens = new ArrayList<>();
        testTokens.add(new COSFloat(1));
        testTokens.add(new COSFloat(0));   
        testTokens.add(new COSFloat(0));   
        testTokens.add(Operator.getOperator("rg"));



        //ICC_ColorSpace isc = new ICC_ColorSpace(icc_profile);

        List<Object> result = ChangeColorSpace.changeRGBtoCMYK(testTokens, mockIcc);
        System.out.println(result);

        assertEquals(5, result.size());
        assertEquals("k", ((Operator) result.get(4)).getName());

    }

    


}
