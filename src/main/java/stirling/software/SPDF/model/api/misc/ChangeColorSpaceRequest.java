package stirling.software.SPDF.model.api.misc;

import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import lombok.Data;
import lombok.EqualsAndHashCode;
import stirling.software.SPDF.model.api.PDFFile;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChangeColorSpaceRequest extends PDFFile {
    private ICC_Profile iccProfile;
    private ICC_ColorSpace cmykColorSpace;

    public ChangeColorSpaceRequest() throws IOException {
        String iccFilePath = "resources/static/Coated_Fogra39L_VIGC_300.icc";

        try (InputStream iccProfileStream = new FileInputStream(iccFilePath)) {
            this.iccProfile = ICC_Profile.getInstance(iccProfileStream);
        } catch (FileNotFoundException e) {
            System.err.println("ICC-profilen kunde inte hittas: " + iccFilePath);
        }
        this.cmykColorSpace = new ICC_ColorSpace(iccProfile);
    }
}
