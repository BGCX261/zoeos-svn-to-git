package com.pcmsolutions.device.EMU.E4.zcommands.sample;

import com.pcmsolutions.device.EMU.E4.sample.ContextBasicEditableSample;
import com.pcmsolutions.device.EMU.E4.sample.ContextEditableSample;
import com.pcmsolutions.device.EMU.E4.zcommands.E4ContextBasicEditableSampleZCommandMarker;
import com.pcmsolutions.system.AbstractZMTCommand;
import com.pcmsolutions.system.ZCommandTargetsNotSpecifiedException;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 22-Mar-2003
 * Time: 15:05:57
 * To change this template use Options | File Templates.
 */
public  abstract class AbstractContextBasicEditableSampleZMTCommand extends AbstractZMTCommand<ContextBasicEditableSample> implements E4ContextBasicEditableSampleZCommandMarker {
    public String getPresentationCategory() {
        return "Sample";
    }
}
