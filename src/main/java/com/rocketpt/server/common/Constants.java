package com.rocketpt.server.common;

/**
 * @author plexpt
 */
public interface Constants {

    String TOKEN_HEADER_NAME = "Authorization";


    String SESSION_CURRENT_USER = "currentUser";

    /**
     * 菜单根id
     */
    long RESOURCE_ROOT_ID = 1L;

    interface Order {
        String DEFAULT_ORDER_TYPE = "desc";

        String[] ORDER_TYPE = new String[]{"desc", "asc", "DESC", "ASC"};
    }
}
