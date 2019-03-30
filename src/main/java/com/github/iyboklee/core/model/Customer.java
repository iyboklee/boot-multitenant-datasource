/**
 * @Author iyboklee (iyboklee@gmail.com)
 */
package com.github.iyboklee.core.model;

import javax.persistence.*;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@Table(name = "customers")
@EqualsAndHashCode(of = "seq")
@ToString(exclude = "regtime")
public class Customer {

    @Id
    @GeneratedValue
    private Long seq;

    @Column(name = "name")
    private String name;

    @Embedded
    private Email email;

    @Column(name = "regtime")
    private LocalDateTime regtime;

}