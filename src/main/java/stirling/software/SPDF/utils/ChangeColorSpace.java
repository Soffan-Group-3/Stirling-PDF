package stirling.software.SPDF.utils;

import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class ChangeColorSpace {

    public static BufferedImage transformToICC(PDImageXObject src, InputStream icc_profile)
            throws IOException {
        BufferedImage src_img = src.getImage();
        ICC_Profile ip = ICC_Profile.getInstance(icc_profile);
        ICC_ColorSpace ics = new ICC_ColorSpace(ip);
        ColorConvertOp cco = new ColorConvertOp(ics, null);
        BufferedImage result = cco.filter(src_img, null);
        return result;
    }
}
