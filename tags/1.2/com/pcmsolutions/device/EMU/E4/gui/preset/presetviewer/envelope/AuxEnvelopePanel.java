package com.pcmsolutions.device.EMU.E4.gui.preset.presetviewer.envelope;

import com.pcmsolutions.device.EMU.E4.parameter.IllegalParameterIdException;
import com.pcmsolutions.device.EMU.E4.parameter.ParameterCategories;
import com.pcmsolutions.device.EMU.E4.preset.ReadablePreset;
import com.pcmsolutions.device.EMU.E4.DevicePreferences;
import com.pcmsolutions.system.IntPool;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.*;
import java.awt.event.ActionEvent;


public class AuxEnvelopePanel extends VoiceEnvelopePanel {
    Action toggleAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            String s = DevicePreferences.ZPREF_auxEnvelopeMode.getValue();
            if (s.equals(DevicePreferences.ENVELOPE_MODE_FIXED))
                DevicePreferences.ZPREF_auxEnvelopeMode.putValue(DevicePreferences.ENVELOPE_MODE_SCALED);
            else if (s.equals(DevicePreferences.ENVELOPE_MODE_SCALED))
                DevicePreferences.ZPREF_auxEnvelopeMode.putValue(DevicePreferences.ENVELOPE_MODE_FIXED);
        }
    };
    ChangeListener cl = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            updateEnvelope();
        }
    };

    public AuxEnvelopePanel init(ReadablePreset.ReadableVoice voice) throws IllegalParameterIdException {
        super.init(voice, ParameterCategories.VOICE_AUX_ENVELOPE, IntPool.get(117), "Aux Envelope", toggleAction);
        DevicePreferences.ZPREF_fillAuxEnvelopes.addChangeListener(cl);
        DevicePreferences.ZPREF_auxEnvelopeMode.addChangeListener(cl);
        getEnvelope().addMouseListener(new RatesEnvelopeMouseListener(getEnvelope(), DevicePreferences.ZPREF_fillAuxEnvelopes, DevicePreferences.ZPREF_auxEnvelopeMode));
        updateEnvelope();
        return this;
    }

    protected void updateEnvelope() {
        this.getEnvelope().setFill(DevicePreferences.ZPREF_fillAuxEnvelopes.getValue());
        String s = DevicePreferences.ZPREF_auxEnvelopeMode.getValue();
        if (s.equals(DevicePreferences.ENVELOPE_MODE_FIXED))
            getEnvelope().setMode(RatesEnvelope.MODE_FIXED);
        else if (s.equals(DevicePreferences.ENVELOPE_MODE_SCALED))
            getEnvelope().setMode(RatesEnvelope.MODE_SCALED);
    }

    public void zDispose() {
        super.zDispose();
        DevicePreferences.ZPREF_fillAuxEnvelopes.removeChangeListener(cl);
        DevicePreferences.ZPREF_auxEnvelopeMode.removeChangeListener(cl);
    }
}
