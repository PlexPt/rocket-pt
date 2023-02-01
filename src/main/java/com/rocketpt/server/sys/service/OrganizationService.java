package com.rocketpt.server.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.exception.UserException;
import com.rocketpt.server.dto.entity.Organization;
import com.rocketpt.server.sys.repository.OrganizationRepository;
import com.rocketpt.server.sys.dto.OrgTreeDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

/**
 * @author plexpt
 */
@Service
@RequiredArgsConstructor
public class OrganizationService extends ServiceImpl<OrganizationRepository, Organization> {

    private final OrganizationRepository organizationRepository;

    public Organization findOrganization(Long id) {
        Organization organization = getById(id);
        if (organization == null) {
            throw new UserException(CommonResultStatus.RECORD_NOT_EXIST);
        }
        return organization;
    }

    @Transactional(rollbackFor = Exception.class)
    public Organization createOrganization(String name, Organization.Type type, Long parentId) {
        Organization organization = new Organization();
        organization.setName(name);
        organization.setType(type);
        organization.setParentId(parentId);
        Organization parent = findOrganization(parentId);
        organization.setParentIds(parent.makeSelfAsParentIds());
        save(organization);
        return organization;
    }

    @Transactional(rollbackFor = Exception.class)
    public Organization updateOrganization(Long id, String name) {
        Organization organization = findOrganization(id);
        organization.setName(name);
        updateById(organization);
        return organization;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteOrganization(Long id) {
        removeById(id);
    }

    public List<OrgTreeDTO> findOrgTree(Long parentId) {
        List<Organization> list = organizationRepository.findByParentId(parentId);
        List<Organization> organizations = list();
        return list
                .stream()
                .peek(e -> buildChildren(e, organizations))
                .map(OrgTreeDTO::new)
                .collect(Collectors.toList());
    }

    public void buildChildren(Organization organization, List<Organization> list) {

        Set<Organization> result = new LinkedHashSet<>();
        for (Organization org : list) {

            Long organizationId = organization.getId();
            if (organizationId.equals(org.getParentId())) {
                result.add(org);
                if (hasChildren(org, list)) {
                    buildChildren(org, list);
                }
            }
        }
        organization.setChildren(result);
    }

    private static boolean hasChildren(Organization org, List<Organization> lists) {
        Set<Long> set = lists.stream()
                .map(Organization::getParentId)
                .collect(Collectors.toSet());
        return set.contains(org.getId());
    }
}
