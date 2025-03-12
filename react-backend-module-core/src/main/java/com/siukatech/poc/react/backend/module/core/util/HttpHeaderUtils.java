package com.siukatech.poc.react.backend.module.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

@Slf4j
public class HttpHeaderUtils {

    private HttpHeaderUtils() {
    }

    public static void logHttpHeaders(HttpHeaders httpHeaders) {
        httpHeaders.entrySet().forEach(stringListEntry -> {
            log.debug("logHttpHeaders - httpHeaders - stringListEntry.getKey: [{}]"
                            + ", stringListEntry.getValue: [{}]"
                    , stringListEntry.getKey()
                    , StringUtils.join(stringListEntry.getValue(), ",")
            );
        });
    }

}
