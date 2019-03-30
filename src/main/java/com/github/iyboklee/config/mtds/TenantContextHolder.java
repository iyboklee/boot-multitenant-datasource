/**
 * @Author iyboklee (iyboklee@gmail.com)
 */
package com.github.iyboklee.config.mtds;

public class TenantContextHolder {

    private static final ThreadLocal<TenantType> contextHolder = new ThreadLocal<>();

    public static void set(TenantType tenantType){
        contextHolder.set(tenantType);
    }

    public static TenantType get(){
        return contextHolder.get();
    }

    public static void clear(){
        contextHolder.remove();
    }

}