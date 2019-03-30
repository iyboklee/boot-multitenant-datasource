/**
 * @Author iyboklee (iyboklee@gmail.com)
 */
package com.github.iyboklee.config.mtds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultitenantDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContextHolder.get();
    }

}