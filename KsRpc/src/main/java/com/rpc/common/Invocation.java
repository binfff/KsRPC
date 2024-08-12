package com.rpc.common;

import java.io.Serializable;

/**
 * Invocation
 *
 * @author gjh
 * @version 1.0
 * @Date 2023-07-21 18:56
 * @description TODO
 */
public class Invocation implements Serializable {
    private String interfaceName;
    private String methodName;
    private Class[] parametersType;
    private Object[] parameters;

    public Invocation(String interfaceName, String methodName, Class[] parametersType,
        Object[] parameters) {
      this.interfaceName = interfaceName;
      this.methodName = methodName;
      this.parametersType = parametersType;
      this.parameters = parameters;
    }

    public String getInterfaceName() {
      return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
      this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParametersType() {
        return parametersType;
    }

    public void setParametersType(Class[] parametersType) {
        this.parametersType = parametersType;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
