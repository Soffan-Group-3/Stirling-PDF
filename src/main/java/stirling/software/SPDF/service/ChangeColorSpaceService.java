package stirling.software.SPDF.service;

import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSName;
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

    public ICC_Profile loadICCProfile(String profileName)
            throws IOException, FileNotFoundException {

        // Change this to relative path for deployment
        // Or should probably be made in a prettier way...
        String iccFilePath = "src/main/resources/static/" + profileName;
        ICC_Profile iccProfile = null;
        try (InputStream iccProfileStream = new FileInputStream(iccFilePath)) {
            iccProfile = ICC_Profile.getInstance(iccProfileStream);
        }
        return iccProfile;
    }

    public PDDocument changeColorSpace(PDDocument document, ICC_Profile icc_profile)
            throws IOException {
        PDDocument pdf_with_changed_images = changeColorSpaceImages(document, icc_profile);
        PDDocument pdf_with_changed_text =
                changeColorSpaceTextAndBackground(pdf_with_changed_images, icc_profile);

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
    public PDDocument changeColorSpaceImages(PDDocument document, ICC_Profile icc_profile)
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
            PDDocument document, ICC_Profile icc_profile) throws IOException {
        ICC_ColorSpace isc = new ICC_ColorSpace(icc_profile);
        for (PDPage page : document.getPages()) {
            PDFStreamParser parser = new PDFStreamParser(page);
            List<Object> tokens = parser.parse();
            List<Object> newTokens = ChangeColorSpace.changeRGBtoCMYK(tokens, isc);
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
