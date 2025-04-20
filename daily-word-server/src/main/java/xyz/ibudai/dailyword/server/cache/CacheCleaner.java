package xyz.ibudai.dailyword.server.cache;

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
            DictionaryCache.WORDS.clear();
            DictionaryCache.VOCABULARY.clear();
        } catch (Exception e) {
            log.error("CacheCleaner clear catalog went wrong", e);
        }
    }
}
