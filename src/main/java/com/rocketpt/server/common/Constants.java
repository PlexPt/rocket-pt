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

        String PROTOCOL = "udp";

        String HOSTNAME = "rocket.pt.local";

        Integer PORT = 9113;

    }
}
