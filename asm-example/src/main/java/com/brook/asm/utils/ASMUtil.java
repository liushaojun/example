package com.brook.asm.utils;

public class ASMUtil {
    /**
     * L +className+ ;
     * @param c
     * @return
     */
    public static String getASMDescByClass(Class c){
        return "L"+ c.getName().replaceAll("\\.","/")+";";
    }

    /**
     * 包名. 转换 /
     */
    public static String getASMOwnerByClass(Class c){
        return c.getName().replaceAll("\\.","/");
    }
    public static Class getClassByASMDesc(String desc) throws ClassNotFoundException {
        String className = desc.substring(1, desc.length()-1).replace("/", ".");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.loadClass(className);
    }
}
