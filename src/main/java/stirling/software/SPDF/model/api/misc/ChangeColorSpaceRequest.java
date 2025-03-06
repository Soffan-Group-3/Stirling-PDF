package stirling.software.SPDF.model.api.misc;

import lombok.Data;
import lombok.EqualsAndHashCode;

import stirling.software.SPDF.model.api.PDFFile;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChangeColorSpaceRequest extends PDFFile {

    // Add schemas here
    // We need to in someway get an Inputstream (or similar for the request ICC profile)

}
