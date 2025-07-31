package com.github.andre10dias.spring_rest_api.data.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Relation(collectionRelation = "permission")
public class PermissionDTO extends RepresentationModel<PermissionDTO> {

    private Long id;
    private String description;

}
