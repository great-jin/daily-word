package xyz.ibudai.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheCleaner implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            DicPreHeat.catalogueMap.clear();
            DicPreHeat.dictCache.clear();
        } catch (Exception e) {
            log.error("缓存清除异常", e);
        }
    }
}
