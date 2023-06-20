package com.flexcub.resourceplanning.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class NullPropertyName {

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static String checkNull(String data) {
        if (null == data) {
            return null;
        }
        return data;
    }

    public static Integer checkNull(Integer data) {
        if (null == data) {
            return null;
        }
        return data;
    }

    public static Boolean checkNull(Boolean data) {
        if (null == data) {
            return null;
        }
        return data;
    }

    public static Object checkNull(Object data) {
        if (null == data) {
            return null;
        }
        return data;
    }

    public static Date checkNull(Date data) {
        if (null == data) {
            return null;
        }
        return data;
    }

    public static java.sql.Date checkNull(java.sql.Date data) {
        if (null == data) {
            return null;
        }
        return data;
    }
}
