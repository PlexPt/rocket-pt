package com.rocketpt.server.dto.sys;

import com.rocketpt.server.dto.entity.Organization;

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
    private final Organization.Type type;
    private final List<OrgTreeDTO> children;

    public OrgTreeDTO(Organization organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.type = organization.getType();
        this.children = organization.getChildren().stream().map(OrgTreeDTO::new).toList();
    }

    public boolean getIsLeaf() {
        return getChildren().isEmpty();
    }

}
