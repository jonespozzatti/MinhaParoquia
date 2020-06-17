package org.paroquia.api.monitoramento;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;
  
@Component @EnableScheduling 
public class CacheSingletonBeanExemplo {
  
         private Map<Integer, String> meuCache;
  
         @PostConstruct
         public void start() {
                   Logger.getLogger("MeuLoggerGlobal").info("Iniciou!");
                   meuCache = new HashMap<Integer, String>();
         }
  
         public void addUsuario(Integer id, String nome){
                   meuCache.put(id, nome);
         }
  
         public String getNome(Integer id){
                   return meuCache.get(id);
         }
}