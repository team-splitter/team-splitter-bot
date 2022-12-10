package com.max.team.splitter.core;

import com.max.team.splitter.persistence.PersistenceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        PersistenceConfig.class,
})
@ComponentScan
public class TeamSplitterCoreConfig {



}
