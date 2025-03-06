package stirling.software.SPDF.controller.api.misc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import stirling.software.SPDF.model.api.misc.ChangeColorSpaceRequest;
import stirling.software.SPDF.service.ChangeColorSpaceService;
import stirling.software.SPDF.service.CustomPDDocumentFactory;
import stirling.software.SPDF.utils.WebResponseUtils;

@RestController
@RequestMapping("/api/v1/misc")
@Tag(name = "Misc", description = "Miscellaneous APIs")
public class ChangeColorSpaceController {

    private ChangeColorSpaceService changeColorSpaceService;
    private final CustomPDDocumentFactory pdfDocumentFactory;

    @Autowired
    public ChangeColorSpaceController(
            ChangeColorSpaceService changeColorSpaceService,
            CustomPDDocumentFactory pdfDocumentFactory) {
        this.changeColorSpaceService = changeColorSpaceService;
        this.pdfDocumentFactory = pdfDocumentFactory;
    }

    @PostMapping(consumes = "multipart/form-data", value = "/change-color-space-pdf")
    @Operation(
            summary = "Change Color Space of PDF",
            description =
                    "This endpoint accepts a PDF file and option of ICC Profile. Input:PDF Output:PDF Type:SISO")
    public ResponseEntity<byte[]> updateColorSpace(
            @ModelAttribute ChangeColorSpaceRequest changeColorSpaceRequest) throws IOException {
        PDDocument document = pdfDocumentFactory.load(changeColorSpaceRequest);
        PDDocument modifiedDocument =
                changeColorSpaceService.ChangeColorSpaceImages(
                        document, null // Change this null into a real icc-profile
                        );

        // Create a ByteArrayOutputStream to hold the modified PDF data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Save the modified PDF document to the output stream
        modifiedDocument.save(outputStream);
        modifiedDocument.close();

        String mergedFileName =
                changeColorSpaceRequest
                                .getFileInput()
                                .getOriginalFilename()
                                .replaceFirst("[.][^.]+$", "")
                        + "_update_color_space.pdf";

        // Return the modified PDF as a downloadable file
        // Convert the byte array to a web response and return it
        return WebResponseUtils.bytesToWebResponse(outputStream.toByteArray(), mergedFileName);
    }
}
