package stirling.software.SPDF.controller.api.misc;

import java.awt.color.ICC_Profile;
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
        ICC_Profile icc =
                changeColorSpaceService.loadICCProfile(changeColorSpaceRequest.getIccProfileName());
        PDDocument modifiedDocument = changeColorSpaceService.changeColorSpace(document, icc);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        modifiedDocument.save(outputStream);
        modifiedDocument.close();

        String mergedFileName =
                changeColorSpaceRequest
                                .getFileInput()
                                .getOriginalFilename()
                                .replaceFirst("[.][^.]+$", "")
                        + "_updated_color_space.pdf";

        return WebResponseUtils.bytesToWebResponse(outputStream.toByteArray(), mergedFileName);
    }
}
