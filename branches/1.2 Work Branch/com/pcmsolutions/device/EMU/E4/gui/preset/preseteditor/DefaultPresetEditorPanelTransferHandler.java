package com.pcmsolutions.device.EMU.E4.gui.preset.preseteditor;

import com.pcmsolutions.device.EMU.E4.PresetContextMacros;
import com.pcmsolutions.device.EMU.E4.gui.preset.presetviewer.LinkTableTransferHandler;
import com.pcmsolutions.device.EMU.E4.gui.preset.presetviewer.PresetParameterTableTransferHandler;
import com.pcmsolutions.device.EMU.E4.gui.preset.presetviewer.VoiceOverviewTableTransferHandler;
import com.pcmsolutions.device.EMU.E4.gui.sample.samplecontext.SampleContextTransferHandler;
import com.pcmsolutions.device.EMU.E4.parameter.ParameterValueOutOfRangeException;
import com.pcmsolutions.device.EMU.E4.preset.*;
import com.pcmsolutions.device.EMU.E4.sample.NoSuchSampleException;
import com.pcmsolutions.device.EMU.E4.selections.ContextSampleSelection;
import com.pcmsolutions.device.EMU.E4.selections.LinkSelection;
import com.pcmsolutions.device.EMU.E4.selections.PresetParameterSelection;
import com.pcmsolutions.device.EMU.E4.selections.VoiceSelection;
import com.pcmsolutions.system.ZDeviceNotRunningException;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 19-May-2003
 * Time: 19:23:57
 * To change this template use Options | File Templates.
 */
public class DefaultPresetEditorPanelTransferHandler extends TransferHandler implements Transferable {

    public DefaultPresetEditorPanelTransferHandler() {
    }

    public boolean importData(JComponent comp, Transferable t) {
        if (comp instanceof EditableLinkTable.LinkSelectionAcceptor) {
            if (t.isDataFlavorSupported(LinkTableTransferHandler.linkFlavor)) {
                try {
                    LinkSelection ils = (LinkSelection) t.getTransferData(LinkTableTransferHandler.linkFlavor);
                    ((EditableLinkTable.LinkSelectionAcceptor) comp).setSelection(ils);
                    return true;
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (comp instanceof EditableVoiceOverviewTable.VoiceSelectionAcceptor) {
            if (t.isDataFlavorSupported(VoiceOverviewTableTransferHandler.voiceFlavor)) {
                try {
                    VoiceSelection vs = (VoiceSelection) t.getTransferData(VoiceOverviewTableTransferHandler.voiceFlavor);
                    ((EditableVoiceOverviewTable.VoiceSelectionAcceptor) comp).setSelection(vs);
                    return true;
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (comp instanceof EditablePresetParameterTable.PresetParameterSelectionAcceptor) {
            if (t.isDataFlavorSupported(PresetParameterTableTransferHandler.presetParameterFlavor)) {
                try {
                    PresetParameterSelection pps = (PresetParameterSelection) t.getTransferData(PresetParameterTableTransferHandler.presetParameterFlavor);
                    ((EditablePresetParameterTable.PresetParameterSelectionAcceptor) comp).setSelection(pps);
                    return true;
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (comp instanceof DefaultPresetEditorPanel) {
            if (t.isDataFlavorSupported(SampleContextTransferHandler.sampleContextFlavor)) {
                ContextSampleSelection sel;
                ReadablePreset rp = ((DefaultPresetEditorPanel) comp).getPreset();
                try {
                    sel = (ContextSampleSelection) t.getTransferData(SampleContextTransferHandler.sampleContextFlavor);
                    if (rp instanceof ContextEditablePreset) {
                        final Integer[] sampleIndexes = sel.getSampleIndexes();
                        if (sampleIndexes.length < 1)
                            return false;

                        try {
                            PresetContextMacros.applySamplesToPreset(((ContextEditablePreset) rp).getPresetContext(), rp.getPresetNumber(), sampleIndexes);
                        } catch (ParameterValueOutOfRangeException e) {
                            e.printStackTrace();
                        } catch (PresetEmptyException e) {
                            e.printStackTrace();
                        } catch (NoSuchContextException e) {
                            e.printStackTrace();
                        } catch (TooManyVoicesException e) {
                            e.printStackTrace();
                        } catch (NoSuchPresetException e) {
                            e.printStackTrace();
                        } catch (ZDeviceNotRunningException e) {
                            e.printStackTrace();
                        } catch (NoSuchSampleException e) {
                            e.printStackTrace();
                        } catch (TooManyZonesException e) {
                            e.printStackTrace();
                        }
                        // TODO!!

                        //if ( sampleIndexes.length == 1)

                        //  ((ContextEditablePreset) rp).applySampleToPreset(sampleIndexes[0], justVoices);
                        //else
                          //  ((ContextEditablePreset) rp).applySampleToPreset(sampleIndexes, justVoices);

                    }
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        DataFlavor cf = null;
        if (comp instanceof EditableLinkTable.LinkSelectionAcceptor) {
            for (int i = 0, n = transferFlavors.length; i < n; i++)
                if (transferFlavors[i].equals(LinkTableTransferHandler.linkFlavor)) {
                    cf = LinkTableTransferHandler.linkFlavor;
                    break;
                }
        }
        if (cf == null && comp instanceof EditableVoiceOverviewTable.VoiceSelectionAcceptor) {
            for (int i = 0, n = transferFlavors.length; i < n; i++)
                if (transferFlavors[i].equals(VoiceOverviewTableTransferHandler.voiceFlavor)) {
                    cf = VoiceOverviewTableTransferHandler.voiceFlavor;
                    break;
                }
        }
        if (cf == null && comp instanceof EditablePresetParameterTable.PresetParameterSelectionAcceptor) {
            for (int i = 0, n = transferFlavors.length; i < n; i++)
                if (transferFlavors[i].equals(PresetParameterTableTransferHandler.presetParameterFlavor)) {
                    cf = PresetParameterTableTransferHandler.presetParameterFlavor;
                    break;
                }
        }

        if (cf == null && comp instanceof DefaultPresetEditorPanel) {
            for (int i = 0, n = transferFlavors.length; i < n; i++)
                if (transferFlavors[i].equals(SampleContextTransferHandler.sampleContextFlavor) /*&& SampleContextTransferHandler.sampleContextFlavor.numRows() == 1*/) {
                    //   ((PresetContextTable) comp).setDropFeedbackActive(true);
                    // ((PresetContextTable) comp).setChosenDropFlavor(SampleContextTransferHandler.sampleContextFlavor);
                    return true;
                }

            //    if (transferFlavors[i].equals(PresetParameterTableTransferHandler.presetParameterFlavor)) {
            //      cf = PresetParameterTableTransferHandler.presetParameterFlavor;
            //    break;
            // }
        }
        boolean rv = false;
        if (cf != null) {
            rv = true;
        }
        return rv;
    }

    public int getSourceActions(JComponent c) {
        return 0;
    }

    protected Transferable createTransferable(JComponent c) {
        return null;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{};
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return false;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return null;
    }
}
