package org.delusion.katengine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.delusion.katengine.utils.Version;

public class KatEngine {

    public static final Logger LOGGER = LogManager.getLogger("engine");

    public static final Version VERSION = new Version(0,0,1);
    public static final String NAME = "KatEngineJ";
}
