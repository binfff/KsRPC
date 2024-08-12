package com.rpc.register;

import java.util.HashMap;
import java.util.Map;

/**
 * LocalRegister
 *
 * @author gjh
 * @version 1.0
 * @Date 2023-07-21 19:04
 * @description TODO
 */
public class LocalRegister {
    private static Map<String,Class> map =new HashMap<>();

    public static void regist(String interfaceName,Class implClass){
        map.put(interfaceName,implClass);
    }

    public static Class get(String interfaceName){
        return map.get(interfaceName);
    }

}
