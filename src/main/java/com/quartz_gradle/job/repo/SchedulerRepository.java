package com.quartz_gradle.job.repo;

import com.quartz_gradle.job.model.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {
    public Optional<Scheduler> findSchedulersByClusterEquals(boolean cluster);

}