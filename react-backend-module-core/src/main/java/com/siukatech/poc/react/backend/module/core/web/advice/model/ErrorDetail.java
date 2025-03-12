package com.siukatech.poc.react.backend.module.core.web.advice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Data
@AllArgsConstructor
public class ErrorDetail {

    private String code;
    private String[] codeRawList;
    private String[] codeWithBracketList;
    private String[] codeWoBracketList;
    private String defaultMessage;
    private String objectName;
    private String fieldName;
    private Object rejectedValue;
    private List<Object> argumentList;

    public static String[] refineCodes(String[] codes, boolean withBracket) {
        List<String> codeList = codes == null ? List.of() : Arrays.asList(codes).stream()
                .filter(c -> {
                    boolean result = withBracket ? c.contains("[") : !c.contains("[");
                    log.trace("refineCodes - codes.stream - withBracket: [{}], result: [{}], c: [{}]", result, withBracket, c);
                    return result;
                })
                .toList();
        log.debug("refineCodes - withBracket: [{}], codes: [{}]", withBracket, StringUtils.join(codes, ", "));
        log.debug("refineCodes - withBracket: [{}], codeList: [{}]", withBracket, StringUtils.join(codeList, ", "));
        if (codeList.size() <= 2) {
            return codes;
        }
        else {
            String firstStr = codeList.getFirst();
            String lastStr = codeList.getLast();
            log.debug("refineCodes - firstStr: [{}], lastStr: [{}]", firstStr, lastStr);
            return codeList.toArray(new String[0]);
        }
    }
}
