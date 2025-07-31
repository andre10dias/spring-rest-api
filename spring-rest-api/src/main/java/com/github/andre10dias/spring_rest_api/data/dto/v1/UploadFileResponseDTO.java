package com.github.andre10dias.spring_rest_api.data.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadFileResponseDTO {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

}
