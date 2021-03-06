package com.pcmsolutions.comms;

import javax.sound.midi.MidiDevice;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 02-Sep-2003
 * Time: 17:09:17
 * To change this template use Options | File Templates.
 */
public class MidiDeviceNotPermittedException extends Exception {
    private MidiDevice.Info unpermittedDevice;

    public MidiDeviceNotPermittedException(MidiDevice.Info device) {
        this.unpermittedDevice = device;
    }

    public MidiDevice.Info getUnpermittedDevice() {
        return unpermittedDevice;
    }
}
