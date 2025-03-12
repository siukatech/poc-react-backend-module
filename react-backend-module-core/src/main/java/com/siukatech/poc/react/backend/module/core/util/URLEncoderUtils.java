package com.siukatech.poc.react.backend.module.core.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@NoArgsConstructor
public class URLEncoderUtils {

    public static String encodeToQueryString(List<NameValuePair> nameValuePairList) {
        List<String> queryList = nameValuePairList.stream().map(nameValuePair -> {
                    String str = "";
                    try {
                        if (nameValuePair.getValue() != null) {
                            str = URLEncoder.encode(nameValuePair.getName(), StandardCharsets.UTF_8.name())
                                    + "="
                                    + URLEncoder.encode(nameValuePair.getValue(), StandardCharsets.UTF_8.name())
                            ;
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    return str;
                })
                .filter(s -> StringUtils.isNoneEmpty(s)).toList();
        String queryString = StringUtils.join(queryList, "&");
        return queryString;
    }
}
