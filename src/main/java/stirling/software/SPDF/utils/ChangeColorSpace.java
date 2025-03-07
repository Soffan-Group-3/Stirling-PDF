package stirling.software.SPDF.utils;

import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class ChangeColorSpace {

    public static BufferedImage transformToICC(PDImageXObject src, ICC_Profile icc_profile)
            throws IOException {
        BufferedImage src_img = src.getImage();
        ICC_ColorSpace ics = new ICC_ColorSpace(icc_profile);
        ColorConvertOp cco = new ColorConvertOp(ics, null);
        BufferedImage result = cco.filter(src_img, null);
        return result;
    }

    public static List<Object> changeRGBtoCMYK(List<Object> tokens, ICC_ColorSpace isc) {
        List<Object> newTokens = new ArrayList<Object>();
        for (int j = 0; j < tokens.size(); j++) {
            Object next = tokens.get(j);
            if (next instanceof Operator op) {
                if ("rg".equals(op.getName()) || "RG".equals(op.getName())) {
                    COSNumber r = (COSNumber) tokens.get(j - 3);
                    COSNumber g = (COSNumber) tokens.get(j - 2);
                    COSNumber b = (COSNumber) tokens.get(j - 1);

                    float[] rgb = {r.floatValue(), g.floatValue(), b.floatValue()};
                    float[] icc_color = isc.fromRGB(rgb);

                    int size = newTokens.size();
                    newTokens.remove(size - 1);
                    newTokens.remove(size - 2);
                    newTokens.remove(size - 3);
                    size = newTokens.size();

                    if (icc_color.length == 4) {
                        newTokens.add(new COSFloat(icc_color[0]));
                        newTokens.add(new COSFloat(icc_color[1]));
                        newTokens.add(new COSFloat(icc_color[2]));
                        newTokens.add(new COSFloat(icc_color[3]));
                        String newOp = "rg".equals(op.getName()) ? "k" : "K";
                        newTokens.add(Operator.getOperator(newOp));
                    } else {
                        newTokens.add(new COSFloat(icc_color[0]));
                        newTokens.add(new COSFloat(icc_color[1]));
                        newTokens.add(new COSFloat(icc_color[2]));
                        newTokens.add(Operator.getOperator(op.getName()));
                    }
                    continue;
                }
            }
            newTokens.add(next);
        }
        return newTokens;
    }
}
