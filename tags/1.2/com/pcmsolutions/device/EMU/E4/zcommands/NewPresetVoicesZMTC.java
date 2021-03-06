package com.pcmsolutions.device.EMU.E4.zcommands;

import com.pcmsolutions.device.EMU.E4.preset.ContextEditablePreset;
import com.pcmsolutions.device.EMU.E4.preset.NoSuchPresetException;
import com.pcmsolutions.device.EMU.E4.preset.PresetEmptyException;
import com.pcmsolutions.device.EMU.E4.preset.TooManyVoicesException;
import com.pcmsolutions.system.CommandFailedException;
import com.pcmsolutions.system.IntPool;
import com.pcmsolutions.system.ZMTCommand;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 22-Mar-2003
 * Time: 14:40:54
 * To change this template use Options | File Templates.
 */
public class NewPresetVoicesZMTC extends AbstractContextEditablePresetZMTCommand {
    private int voiceCount;

    public NewPresetVoicesZMTC() {
        this("New Voice", "Add 1 new voice to preset", 1);
    }

    public int getMnemonic() {
        if (voiceCount == 1)
            return KeyEvent.VK_V;

        return super.getMnemonic();
    }

    public NewPresetVoicesZMTC(String presString, String descString, int voiceCount) {
        super(presString, descString, null, null);
        this.voiceCount = voiceCount;
    }

    public JComponent getComponentForArgument(int index) throws IllegalArgumentException  // exception for index out of range
    {
        return null;
    }

    public ZMTCommand getNextMode() {
        if (voiceCount == 8)
            return null;
        return new NewPresetVoicesZMTC((voiceCount + 1) + " voices", "Add " + (voiceCount + 1) + " new voices to preset", voiceCount + 1);
    }

    public void execute(Object invoker, Object[] arguments) throws IllegalArgumentException, CommandFailedException  // IllegalArgumentException thrown for insufficient number of arguments
    {
        ContextEditablePreset[] presets = getTargets();
        int num = presets.length;
        ContextEditablePreset p;
        try {
            if (num == 0) {
                // try use primary target
                p = getTarget();
                newVoice(p);
            } else {
                HashMap done = new HashMap();
                for (int n = 0; n < num; n++) {
                    if (!done.containsKey(presets[n])) {
                        newVoice(presets[n]);
                        done.put(presets[n], null);
                    }
                    Thread.yield();
                }
            }
        } catch (NoSuchPresetException e) {
            throw new CommandFailedException("Preset Not Found");
        } catch (PresetEmptyException e) {
            throw new CommandFailedException("Preset Empty");
        } catch (TooManyVoicesException e) {
            throw new CommandFailedException("Too Many Voices");
        }
    }

    private void newVoice(ContextEditablePreset p) throws NoSuchPresetException, PresetEmptyException, TooManyVoicesException {
        Integer[] samples = new Integer[voiceCount];
        for (int i = 0, n = voiceCount; i < n; i++)
            samples[i] = IntPool.get(0);

        p.newVoices(IntPool.get(voiceCount), samples);
    }

    public String getMenuPathString() {
        if (voiceCount == 1)
            return "";
        return ";New;Voices";
    }
}

