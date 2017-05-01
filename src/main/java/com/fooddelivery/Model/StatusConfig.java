package com.fooddelivery.Model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by kewalins on 4/22/2017 AD.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "status_config")
public class StatusConfig {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "status_id")
    private long status_id;

    @NotNull
    @Column(name = "status_name")
    private String status_name;

    @NotNull
    @Column(name = "status_type")
    private String status_type;

    public long getStatus_id() {
        return status_id;
    }

    public void setStatus_id(long status_id) {
        this.status_id = status_id;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getStatus_type() {
        return status_type;
    }

    public void setStatus_type(String status_type) {
        this.status_type = status_type;
    }

}
