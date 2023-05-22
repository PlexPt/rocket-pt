package com.rocketpt.server.service.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.exception.UserException;
import com.rocketpt.server.dto.entity.OrganizationEntity;
import com.rocketpt.server.dto.sys.OrgTreeDTO;
import com.rocketpt.server.dao.OrganizationDao;

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
public class OrganizationService extends ServiceImpl<OrganizationDao, OrganizationEntity> {

    private final OrganizationDao organizationDao;

    public OrganizationEntity findOrganization(Long id) {
        OrganizationEntity organizationEntity = getById(id);
        if (organizationEntity == null) {
            throw new UserException(CommonResultStatus.RECORD_NOT_EXIST);
        }
        return organizationEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public OrganizationEntity createOrganization(String name, OrganizationEntity.Type type, Long parentId) {
        OrganizationEntity organizationEntity = new OrganizationEntity();
        organizationEntity.setName(name);
        organizationEntity.setType(type);
        organizationEntity.setParentId(parentId);
        OrganizationEntity parent = findOrganization(parentId);
        organizationEntity.setParentIds(parent.makeSelfAsParentIds());
        save(organizationEntity);
        return organizationEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public OrganizationEntity updateOrganization(Long id, String name) {
        OrganizationEntity organizationEntity = findOrganization(id);
        organizationEntity.setName(name);
        updateById(organizationEntity);
        return organizationEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteOrganization(Long id) {
        removeById(id);
    }

    public List<OrgTreeDTO> findOrgTree(Long parentId) {
        List<OrganizationEntity> list = organizationDao.findByParentId(parentId);
        List<OrganizationEntity> organizationEntities = list();
        return list
                .stream()
                .peek(e -> buildChildren(e, organizationEntities))
                .map(OrgTreeDTO::new)
                .collect(Collectors.toList());
    }

    public void buildChildren(OrganizationEntity organizationEntity, List<OrganizationEntity> list) {

        Set<OrganizationEntity> result = new LinkedHashSet<>();
        for (OrganizationEntity org : list) {

            Long organizationId = organizationEntity.getId();
            if (organizationId.equals(org.getParentId())) {
                result.add(org);
                if (hasChildren(org, list)) {
                    buildChildren(org, list);
                }
            }
        }
        organizationEntity.setChildren(result);
    }

    private static boolean hasChildren(OrganizationEntity org, List<OrganizationEntity> lists) {
        Set<Long> set = lists.stream()
                .map(OrganizationEntity::getParentId)
                .collect(Collectors.toSet());
        return set.contains(org.getId());
    }
}
