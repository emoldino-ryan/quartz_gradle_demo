package com.quartz_gradle.job.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scheduler {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private long initialOffsetMs;
    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean cluster;
    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean onService;
    @Column
    private String cronExpress;
    @Column
    private String serviceName;
    @Column
    private String methodName;
    @Column
    private String jobGroup;
    @Column
    private String jobIdentity;
}
