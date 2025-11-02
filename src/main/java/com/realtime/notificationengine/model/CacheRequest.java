package com.realtime.notificationengine.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.With;

@Data
@With
@Builder
@Value
public class CacheRequest {
    String key;
    String value;
}
