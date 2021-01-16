package scripts;

import utils.HLInstance;
import utils.container.ContainerException;

public enum ScriptNamesMaster implements ScriptNames {
//TODO: Rajouter les noms des scripts du principal (voir sur l'ancien projet HL)
    ;
    private Class<? extends Script> scriptClass;
    private String scriptName;

    ScriptNamesMaster(Class<? extends Script> scriptClass, String scriptName){
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
