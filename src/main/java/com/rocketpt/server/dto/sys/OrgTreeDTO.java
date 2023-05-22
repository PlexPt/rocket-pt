package com.rocketpt.server.dto.sys;

import com.rocketpt.server.dto.entity.OrganizationEntity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author plexpt
 */
@Getter
@Setter
public class OrgTreeDTO {
    private final Long id;
    private final String name;
    private final OrganizationEntity.Type type;
    private final List<OrgTreeDTO> children;

    public OrgTreeDTO(OrganizationEntity organizationEntity) {
        this.id = organizationEntity.getId();
        this.name = organizationEntity.getName();
        this.type = organizationEntity.getType();
        this.children = organizationEntity.getChildren().stream().map(OrgTreeDTO::new).toList();
    }

    public boolean getIsLeaf() {
        return getChildren().isEmpty();
    }

}
