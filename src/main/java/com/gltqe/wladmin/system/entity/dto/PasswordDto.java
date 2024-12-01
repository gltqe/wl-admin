package com.gltqe.wladmin.system.entity.dto;

import lombok.Data;

/**
 * @author gltqe
 */
@Data
public class PasswordDto {
    private String uid;
    private String old;
    private String new1;
    private String new2;

}
