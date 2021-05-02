package scripts;

import utils.HLInstance;
import utils.container.ContainerException;

public enum ScriptNamesSlave implements ScriptNames {
 //TODO: rajouter les noms de nouveaux scripts (voir sur l'ancien code HL)
    LOLO(ScriptLoisSlave.class,"LoisSlave");

    private Class<? extends Script> scriptClass;
    private String scriptName;

    ScriptNamesSlave(Class<? extends Script> scriptClass, String scriptName){
        this.scriptClass = scriptClass;
        this.scriptName = scriptName;
    }

    @Override
    public Script createScript(HLInstance hl) throws ContainerException {
        return hl.module(scriptClass);
    }

    /**
     * Renvoie le nom du script
     * @return String du nom du script
     */
    public String getName(){
        return this.scriptName;
    }

}
