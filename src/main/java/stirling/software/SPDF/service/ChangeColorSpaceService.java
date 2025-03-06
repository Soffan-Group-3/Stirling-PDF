package stirling.software.SPDF.service;

import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import stirling.software.SPDF.utils.ChangeColorSpace;

@Service
public class ChangeColorSpaceService {

    public PDDocument changeColorSpace(PDDocument document, InputStream icc_profile) throws IOException{
        PDDocument pdf_with_changed_images = changeColorSpaceImages(document, icc_profile);
        PDDocument pdf_with_changed_text = changeColorSpaceTextAndBackground(pdf_with_changed_images, icc_profile);

        return pdf_with_changed_text;
    }

    /**
     * Updates all image objects from the provided PDF document to the provided ICC profile.
     *
     * <p>This method iterates over each page in the document and Updates any image XObjects found
     * in the page's resources to the provided ICC profile color space.
     *
     * @param document The PDF document from which images will be updated.
     * @param icc_profile The ICC profile to use to convert images to.
     * @return The modified PDF document with updated images.
     * @throws IOException If an error occurs while processing the PDF document.
     */
    public PDDocument changeColorSpaceImages(PDDocument document, InputStream icc_profile)
            throws IOException {
        // Iterate over each page in the PDF document
        for (PDPage page : document.getPages()) {
            PDResources resources = page.getResources();
            // Collect the XObject names to remove
            List<COSName> namesToUpdate = new ArrayList<>();

            // Iterate over all XObject names in the page's resources
            for (COSName name : resources.getXObjectNames()) {
                // Check if the XObject is an image
                if (resources.isImageXObject(name)) {
                    // Collect the name for removal
                    namesToUpdate.add(name);
                }
            }

            // Now, modify the resources by removing the collected names
            for (COSName name : namesToUpdate) {
                PDImageXObject old_img = (PDImageXObject) resources.getXObject(name);
                PDImageXObject new_img =
                        LosslessFactory.createFromImage(
                                document, ChangeColorSpace.transformToICC(old_img, icc_profile));
                resources.put(name, (PDXObject) new_img);
            }
        }
        return document;
    }

    /**
     * Updates the color space from text and backgrounds from the provided PDF document to the
     * colorspace of the provided ICC profile.
     *
     * <p>This method iterates over each page in the document and updates any "rg" or "RG" into a
     * converted version.
     *
     * @param document The PDF document from which text will be updated.
     * @param icc_profile The ICC profile to use to convert text to.
     * @return The modified PDF document with updated text.
     * @throws IOException If an error occurs while processing the PDF document.
     */
    public PDDocument changeColorSpaceTextAndBackground(
            PDDocument document, InputStream icc_profile) throws IOException {
        ICC_Profile ip = ICC_Profile.getInstance(icc_profile);
        ICC_ColorSpace isc = new ICC_ColorSpace(ip);
        for (PDPage page : document.getPages()) {
            PDFStreamParser parser = new PDFStreamParser(page);
            List<Object> tokens = parser.parse();
            List<Object> newTokens = new ArrayList<Object>();
            for (int j = 0; j < tokens.size(); j++) {
                Object next = tokens.get(j);
                if (next instanceof Operator op) {
                    if ("rg".equals(op.getName()) || "RG".equals(op.getName())) {
                        // Get values:
                        COSNumber r = (COSNumber) tokens.get(j - 3);
                        COSNumber g = (COSNumber) tokens.get(j - 2);
                        COSNumber b = (COSNumber) tokens.get(j - 1);

                        // Convert values:
                        float[] rgb = {r.floatValue(), g.floatValue(), b.floatValue()};
                        float[] icc_color = isc.fromRGB(rgb);

                        // Remove old color values
                        int size = newTokens.size();
                        newTokens.remove(size - 1);
                        newTokens.remove(size - 2);
                        newTokens.remove(size - 3);
                        size = newTokens.size();

                        // Add new color values
                        if (icc_color.length == 4) {
                            //Four values is for CMYK
                            newTokens.add(new COSFloat(icc_color[0]));
                            newTokens.add(new COSFloat(icc_color[1]));
                            newTokens.add(new COSFloat(icc_color[2]));
                            newTokens.add(new COSFloat(icc_color[3]));
                            String newOp = "rg".equals(op.getName()) ? "k" : "K";
                            newTokens.add(Operator.getOperator(newOp));
                        } else {
                            //Three values is for RGB
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
            PDStream updatedStream = new PDStream(document);
            OutputStream out = updatedStream.createOutputStream(COSName.FLATE_DECODE);
            ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
            tokenWriter.writeTokens(newTokens);
            out.close();
            page.setContents(updatedStream);
        }
        return document;
    }
}
