package stirling.software.SPDF.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import stirling.software.SPDF.utils.ChangeColorSpace;

@Service
public class ChangeColorSpaceService {

    /**
     * Updates all image objects from the provided PDF document to the provided ICC profile.
     *
     * <p>This method iterates over each page in the document and Updates any image XObjects found
     * in the page's resources to the provided ICC profile color space.
     *
     * @param document The PDF document from which images will be updated.
     * @param icc The ICC profile to use to convert images to.
     * @return The modified PDF document with updated images.
     * @throws IOException If an error occurs while processing the PDF document.
     */
    public PDDocument ChangeColorSpaceImages(PDDocument document, InputStream icc_profile)
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
}
