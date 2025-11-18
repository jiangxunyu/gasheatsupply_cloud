package com.chder.gasheatsupply_cloud.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ParamTypeUtils {
    // 定义基础类型和包装类型的Class集合（需排除的类型）
    private static final Set<Class<?>> BASIC_TYPES = new HashSet<>(Arrays.asList(
            // 基础类型
            byte.class, short.class, int.class, long.class,
            float.class, double.class, boolean.class, char.class,
            // 包装类型
            Byte.class, Short.class, Integer.class, Long.class,
            Float.class, Double.class, Boolean.class, Character.class,
            // 字符串和null
            String.class, null
    ));

    /**
     * 判断参数是否为自定义对象（非基础类型、非数组）
     */
    public static boolean isCustomObject(Object param) {
        if (param == null) {
            return false; // null不视为对象
        }
        Class<?> clazz = param.getClass();
        // 排除基础类型、包装类型、字符串
        if (BASIC_TYPES.contains(clazz)) {
            return false;
        }
        // 排除数组（数组需单独处理）
        if (clazz.isArray()) {
            return false;
        }
        // 排除集合、Map等JDK内置容器（可选，根据需求调整）
        if (isJdkContainer(clazz)) {
            return false;
        }
        // 剩余视为自定义对象
        return true;
    }

    /**
     * 判断是否为JDK内置容器（如List、Map、Set等）
     */
    private static boolean isJdkContainer(Class<?> clazz) {
        String packageName = clazz.getPackage().getName();
        return packageName.startsWith("java.") || packageName.startsWith("javax.");
    }
}
