package com.github.andre10dias.spring_rest_api.data.dto.v1;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import lombok.Data;
import lombok.NoArgsConstructor;

@ArraySchema
@NoArgsConstructor
@Data
public class UploadFileResponseDTO {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

}
