/**
 * @Author iyboklee (iyboklee@gmail.com)
 */
package com.github.iyboklee.core.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Embeddable
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Email {

    @Column(name = "email")
    private String address;

    protected Email() {}

    public boolean verifyAddress() {
        if (address != null)
            return false;
        return Pattern.matches("[\\w\\~\\-\\.\\+]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", address);
    }

    public String getName() {
        if (verifyAddress()) {
            String[] tokens = address.split("@");
            if (tokens.length == 2)
                return tokens[0];
        }
        return null;
    }

    public String getDomain() {
        if (verifyAddress()) {
            String[] tokens = address.split("@");
            if (tokens.length == 2)
                return tokens[1];
        }
        return null;
    }

}