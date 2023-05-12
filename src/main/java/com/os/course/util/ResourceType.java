package com.os.course.util;

import lombok.Getter;

@Getter
public enum ResourceType {
    RESOURCE_SERVICE("RESOURCE-SERVICE", 8081),
    SONG_SERVICE("SONG-SERVICE", 8082),

    STORAGE_SERVICE("STORAGE-SERVICE", 8085),

    AUTHORIZATION_SERVICE("AUTHORIZATION-SERVICE", 8086);

    private final String resourceId;

    private final int port;

    ResourceType(String resourceId, int port) {
        this.resourceId = resourceId;
        this.port = port;
    }
}
