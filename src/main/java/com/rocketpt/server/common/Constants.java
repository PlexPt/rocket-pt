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
    Integer RESOURCE_ROOT_ID = 1;

    interface Order {
        String DEFAULT_ORDER_TYPE = "desc";

        String[] ORDER_TYPE = new String[]{"desc", "asc", "DESC", "ASC"};
    }

    interface FinishStatus {

        /**
         * 已完成并测试
         */
        String FINISHED = " (已完成并测试通过)";

        /**
         * 未完成未测试
         */
        String UNFINISHED = " (未完成未测试)";

        /**
         * 已完成但未测试
         */
        String FINISHED_NOT_TEST = " (已完成但未测试)";

    }

    interface Source {
        String PREFIX = "[RKT] ";

        String NAME = "rocket pt";
    }

    interface Announce {

        String PROTOCOL = "http";

        String HOSTNAME = "192.168.6.112";

        Integer PORT = 9966;

    }
}
