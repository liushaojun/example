
package com.brook.asm.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DesFilterMap {
    private static Map<String, DesFilter> desFilterMap = new ConcurrentHashMap<>();
    public static void put(Class<? extends DesFilter> c, DesFilter baseDesFilter){
        desFilterMap.put(c.getName(), baseDesFilter);
    }
    public static DesFilter get(Class<? extends DesFilter> c){
        return desFilterMap.get(c.getName());
    }
    public static DesFilter getByClassName(String className){
        return desFilterMap.get(className);
    }
}