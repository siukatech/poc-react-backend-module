package com.siukatech.poc.react.backend.module.core.security.aop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ToString
@Setter
@Getter
public class ReqVariableData {
    private Map<String, String> pathVarMap = new HashMap<>();
    private Map<String, String[]> paramVarMap = new HashMap<>();
    public ReqVariableData(Map<String, String> pathVarMap, Map<String, String[]> paramVarMap) {
        if (Objects.nonNull(pathVarMap)) this.pathVarMap.putAll(pathVarMap);
        if (Objects.nonNull(paramVarMap)) this.paramVarMap.putAll(paramVarMap);
    }
}
