package stirling.software.SPDF.model.api.misc;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

import stirling.software.SPDF.model.api.PDFFile;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChangeColorSpaceRequest extends PDFFile {

    @Schema(
            description =
                    "The name (or identifier) of the ICC profile to use. "
                            + "For example: 'Coated_Fogra39L_VIGC_300.icc'",
            example = "Coated_Fogra39L_VIGC_300.icc",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            defaultValue = "Coated_Fogra39L_VIGC_300.icc")
    private String iccProfileName;
}
