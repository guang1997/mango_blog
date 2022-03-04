package com.myblog.service.base.util;


import com.esotericsoftware.reflectasm.MethodAccess;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射实现的属性复制工具类
 *
 * @author 李斯特
 * @date 2021/09/27
 */
public class BeanUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);

    // key:Class对象 value:MethodAccess对象
    private static Map<Class, MethodAccess> classMethodAccessMap = new ConcurrentHashMap<>();

    // key:Class对象 value:key:方法名 value:方法下标
    private static Map<Class, Map<String, Integer>> methodIndexMap = new ConcurrentHashMap<>();

    // key:Class对象 value:属性集合
    private static Map<Class, List<Field>> fieldMap = new ConcurrentHashMap<>();

    /**
     * 复制属性
     * @param source 属性来源对象
     * @param target 要复制到的目标对象
     */
    public static void copyProperties(Object source, Object target) {
        if (Objects.equals(source, null) || Objects.equals(target, null)) {
            LOGGER.error("copyProperties failed, source:{}, target:{}", source, target);
            return;
        }

    }

    private static MethodAccess getFromCache(Object object) {
        MethodAccess methodAccess = classMethodAccessMap.get(object.getClass());
        if (methodAccess == null) {
            synchronized (object.getClass()) {
                methodAccess = classMethodAccessMap.get(object.getClass());
                if (methodAccess != null) {
                    return methodAccess;
                }
                Class<?> objClazz = object.getClass();
                MethodAccess newMethodAccess = MethodAccess.get(objClazz);
                if (newMethodAccess == null) {

                }
                Field[] fields = objClazz.getDeclaredFields();
                List<Field> fieldList = Arrays.asList(fields);
                for (Field field : fieldList) {
                    // 非公共的私有变量
                    if (Modifier.isPrivate(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                        // 获取属性名称
                        String fieldName = StringUtils.capitalize(field.getName());
                        int getIndex = methodAccess.getIndex("get" + fieldName);
                    }
                }
                classMethodAccessMap.put(objClazz, newMethodAccess);
                fieldMap.put(objClazz, fieldList);
            }
        }
        return methodAccess;
    }
}
