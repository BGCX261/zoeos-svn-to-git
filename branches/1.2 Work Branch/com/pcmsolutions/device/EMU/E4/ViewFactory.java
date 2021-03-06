package com.pcmsolutions.device.EMU.E4;

import com.pcmsolutions.device.EMU.E4.gui.TitleProvider;
import com.pcmsolutions.device.EMU.E4.gui.TitleProviderListener;
import com.pcmsolutions.device.EMU.E4.gui.colors.UIColors;
import com.pcmsolutions.device.EMU.E4.gui.device.DefaultDeviceEnclosurePanel;
import com.pcmsolutions.device.EMU.E4.gui.device.DevicePanel;
import com.pcmsolutions.device.EMU.E4.gui.device.DeviceWorkspaceEnclosurePanel;
import com.pcmsolutions.device.EMU.E4.gui.device.PropertiesPanel;
import com.pcmsolutions.device.EMU.E4.gui.master.MasterEnclosurePanel;
import com.pcmsolutions.device.EMU.E4.gui.multimode.MultiModeEnclosurePanel;
import com.pcmsolutions.device.EMU.E4.gui.preset.presetcontext.PresetContextEnclosurePanel;
import com.pcmsolutions.device.EMU.E4.gui.preset.preseteditor.*;
import com.pcmsolutions.device.EMU.E4.gui.preset.preseteditor.envelope.EditableAmpEnvelopePanel;
import com.pcmsolutions.device.EMU.E4.gui.preset.preseteditor.envelope.EditableAuxEnvelopePanel;
import com.pcmsolutions.device.EMU.E4.gui.preset.preseteditor.envelope.EditableFilterEnvelopePanel;
import com.pcmsolutions.device.EMU.E4.gui.preset.presetviewer.*;
import com.pcmsolutions.device.EMU.E4.gui.preset.presetviewer.envelope.AmpEnvelopePanel;
import com.pcmsolutions.device.EMU.E4.gui.preset.presetviewer.envelope.AuxEnvelopePanel;
import com.pcmsolutions.device.EMU.E4.gui.preset.presetviewer.envelope.FilterEnvelopePanel;
import com.pcmsolutions.device.EMU.E4.gui.sample.samplecontext.SampleContextEnclosurePanel;
import com.pcmsolutions.device.EMU.E4.preset.ContextEditablePreset;
import com.pcmsolutions.device.EMU.E4.preset.ReadablePreset;
import com.pcmsolutions.gui.ComponentGenerationException;
import com.pcmsolutions.gui.GriddedPanel;
import com.pcmsolutions.gui.desktop.ViewInstance;
import com.pcmsolutions.gui.desktop.ZDesktopManager;
import com.pcmsolutions.system.Indexable;
import com.pcmsolutions.system.ZDisposable;
import com.pcmsolutions.system.paths.DesktopName;
import com.pcmsolutions.system.paths.ViewPath;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * User: paulmeehan
 * Date: 20-Jan-2004
 * Time: 09:03:00
 */
class ViewFactory {

    private static DesktopName[] join(DesktopName[] parent, DesktopName[] child) {
        DesktopName[] names = new DesktopName[parent.length + child.length];
        System.arraycopy(parent, 0, names, 0, parent.length);
        System.arraycopy(child, 0, names, parent.length, child.length);
        return names;
    }

    private static DesktopName[] join(DesktopName[] parent, DesktopName child) {
        return join(parent, new DesktopName[]{child});
    }

    public static DesktopName[] provideDefaultDesktopNames(DeviceContext device) {
        return new DesktopName[]{new DesktopName(Object.class, PathFactory.provideDevicePath(device))};
    }

    // DEVICE
    public static ViewInstance provideDefaultView(final DeviceContext device) {
        final DesktopName[] names = ViewFactory.provideDefaultDesktopNames(device);
        final ViewPath vp = new ViewPath(ZDesktopManager.dockWORKSPACE, names);

        // final DesktopName name = new DesktopName(Object.class, PathFactory.provideDevicePath(device));
        return new ViewInstance() {
           // private transient DefaultDeviceEnclosurePanel view;

            public JComponent getView() throws ComponentGenerationException {
                DefaultDeviceEnclosurePanel view;
              //  if (view == null) {
                    view = new DefaultDeviceEnclosurePanel();
                    try {
                        view.init(device, new JPanel() {
                            public Color getForeground() {
                                return UIColors.getDefaultFG();
                            }

                            public Color getBackground() {
                                return UIColors.getDefaultBG();
                            }
                        });
                    } catch (Exception e) {
                        throw new ComponentGenerationException(e.getMessage());
                    }
               // }
                return view;
            }

            public DesktopName getDesktopName() {
                return names[names.length - 1];
            }

            public ViewPath getViewPath() {
                return vp;
            }
        };
    }

    // PRESET
    public static DesktopName[] provideDefaultDesktopNames(ReadablePreset p) {
        return join(provideDefaultDesktopNames(p.getDeviceContext()), new DesktopName(DefaultPresetViewerPanel.class, PathFactory.providePresetPath(p)));
    }

    public static ViewInstance provideDefaultView(final ReadablePreset p) {
        final DesktopName[] names = provideDefaultDesktopNames(p);
        final ViewPath vp = new ViewPath(ZDesktopManager.dockWORKSPACE, names);
        return new ViewInstance() {
            public JComponent getView() throws ComponentGenerationException {
                try {
                    DeviceWorkspaceEnclosurePanel dep = new DeviceWorkspaceEnclosurePanel();
                    dep.init(p.getDeviceContext(), new DefaultPresetViewerPanel(p));
                    return dep;
                } catch (Exception e) {
                    throw new ComponentGenerationException(e.getMessage());
                }
            }

            public DesktopName getDesktopName() {
                return names[names.length - 1];
            }

            public ViewPath getViewPath() {
                return vp;
            }
        };
    }

    public static ViewInstance provideDefaultView(final ContextEditablePreset p) {
        final DesktopName[] names = provideDefaultDesktopNames(p);
        final ViewPath vp = new ViewPath(ZDesktopManager.dockWORKSPACE, names);
        return new ViewInstance() {
            public JComponent getView() throws ComponentGenerationException {
                try {
                    DeviceWorkspaceEnclosurePanel dep = new DeviceWorkspaceEnclosurePanel();
                    dep.init(p.getDeviceContext(), new DefaultPresetEditorPanel(p));
                    return dep;
                } catch (Exception e) {
                    throw new ComponentGenerationException(e.getMessage());
                }
            }

            public DesktopName getDesktopName() {
                return names[names.length - 1];
            }

            public ViewPath getViewPath() {
                return vp;
            }
        };
    }

    // VOICE
    // this is the invisible parent view for the sections of a readable voice in tabbed view
    private static class EmptyReadableVoicePanel extends JPanel implements ZDisposable, TitleProvider, Indexable {
        private ReadablePreset.ReadableVoice voice;
        private VoiceTitleProvider vtp;

        public void init(ReadablePreset.ReadableVoice voice) {
            this.voice = voice;
            vtp = makeVoiceTitleProvider();
        }

        protected VoiceTitleProvider makeVoiceTitleProvider() {
            VoiceTitleProvider vtp = new VoiceTitleProvider();
            vtp.init(voice);
            return vtp;
        }

        public Color getBackground() {
            return UIColors.getDefaultBG();
        }

        public Color getForeground() {
            return UIColors.getDefaultFG();
        }

        public void zDispose() {
            vtp.zDispose();
        }

        public String getTitle() {
            return vtp.getTitle();
        }

        public String getReducedTitle() {
            return vtp.getReducedTitle();
        }

        public void addTitleProviderListener(TitleProviderListener tpl) {
            vtp.addTitleProviderListener(tpl);
        }

        public void removeTitleProviderListener(TitleProviderListener tpl) {
            vtp.addTitleProviderListener(tpl);
        }

        public Icon getIcon() {
            return vtp.getIcon();
        }

        public Integer getIndex() {
            return voice.getVoiceNumber();
        }
    }

    // this is the invisible parent view for the sections of an editable voice in tabbed view
    private static class EmptyEditableVoicePanel extends EmptyReadableVoicePanel {
        private ContextEditablePreset.EditableVoice[] voices;

        public void init(ContextEditablePreset.EditableVoice[] voices) {
            this.voices = voices;
            init(voices[0]);
        }

        protected VoiceTitleProvider makeVoiceTitleProvider() {
            EditableVoiceTitleProvider vtp = new EditableVoiceTitleProvider();
            vtp.init(voices);
            return vtp;
        }

        public Integer getIndex() {
            if (voices.length > 1)
                return ViewIndexFactory.getMultiVoiceIndex(voices);
            else
                return voices[0].getVoiceNumber();
        }
    }

    // this is where the actual sections of a voice are shown in tabbed mode
    private static class VoiceSectionPanel extends GriddedPanel implements TitleProvider, Indexable {
        private TitleProvider titleProvider;
        private Integer index;

        public VoiceSectionPanel(TitleProvider titleProvider, Integer index) {
            this.titleProvider = titleProvider;
            this.index = index;
        }

        public Color getBackground() {
            return UIColors.getDefaultBG();
        }

        public Color getForeground() {
            return UIColors.getDefaultFG();
        }

        public String getTitle() {
            return titleProvider.getTitle();
        }

        public String getReducedTitle() {
            return titleProvider.getReducedTitle();
        }

        public void addTitleProviderListener(TitleProviderListener tpl) {
            titleProvider.addTitleProviderListener(tpl);
        }

        public void removeTitleProviderListener(TitleProviderListener tpl) {
            titleProvider.removeTitleProviderListener(tpl);
        }

        public Icon getIcon() {
            return titleProvider.getIcon();
        }

        public Integer getIndex() {
            return index;
        }
    }

    public static DesktopName[] provideDefaultDesktopNames(ReadablePreset.ReadableVoice v) {
        return join(provideDefaultDesktopNames(v.getPreset()), new DesktopName(VoicePanel.class, PathFactory.provideVoicePath(v)));
    }

    public static DesktopName[] provideDefaultDesktopNames(ReadablePreset.ReadableVoice v, int sections) {
        return join(provideDefaultDesktopNames(v), new DesktopName(VoiceSectionPanel.class, PathFactory.provideVoiceSectionPath(v, sections)));
    }

    public static DesktopName[] provideDefaultDesktopNames(ContextEditablePreset.EditableVoice v, int sections) {
        return (provideDefaultDesktopNames(new ContextEditablePreset.EditableVoice[]{v}, sections));
        /*     if (v.getGroupMode())
                 return join(provideDefaultDesktopNames(v), new DesktopName(VoiceSectionPanel.class, PathFactory.provideGroupSectionPath(v, sections)));
             else
                 return join(provideDefaultDesktopNames(v), new DesktopName(VoiceSectionPanel.class, PathFactory.provideVoiceSectionPath(v, sections)));
                 */
    }

    public static DesktopName[] provideDefaultDesktopNames(ContextEditablePreset.EditableVoice[] voices) {
        // if (v.getGroupMode())
        return join(provideDefaultDesktopNames(voices[0].getPreset()), new DesktopName(EditableVoicePanel.class, PathFactory.provideVoicePaths(voices)));
        //  else
        //      return join(provideDefaultDesktopNames(v.getPreset()), new DesktopName(EditableVoicePanel.class, PathFactory.provideVoicePath(v)));
    }

    public static DesktopName[] provideDefaultDesktopNames(ContextEditablePreset.EditableVoice[] voices, int sections) {
        // if (v.getGroupMode())
        return join(provideDefaultDesktopNames(voices), new DesktopName(EditableVoicePanel.class, PathFactory.provideVoiceSectionPaths(voices, sections)));
        //  else
        //      return join(provideDefaultDesktopNames(v.getPreset()), new DesktopName(EditableVoicePanel.class, PathFactory.provideVoicePath(v)));
    }

    public static ViewInstance provideDefaultView(final ReadablePreset.ReadableVoice voice, final boolean empty) {
        final DesktopName[] names = provideDefaultDesktopNames(voice);
        final ViewPath vp = new ViewPath(ZDesktopManager.dockWORKSPACE, names);
        return new ViewInstance() {
            public JComponent getView() throws ComponentGenerationException {
                try {
                    DeviceWorkspaceEnclosurePanel dep = new DeviceWorkspaceEnclosurePanel();
                    if (empty) {
                        EmptyReadableVoicePanel evp = new EmptyReadableVoicePanel();
                        evp.init(voice);
                        dep.init(voice.getPreset().getDeviceContext(), evp);
                    } else
                        dep.init(voice.getPreset().getDeviceContext(), new VoicePanel().init(voice));
                    return dep;
                } catch (Exception e) {
                    throw new ComponentGenerationException(e.getMessage());
                }
            }

            public DesktopName getDesktopName() {
                return names[names.length - 1];
            }

            public ViewPath getViewPath() {
                return vp;
            }
        };
    }

    public static ViewInstance provideDefaultView(final ReadablePreset.ReadableVoice voice, final int sections, final TitleProvider tp) {
        final DesktopName[] names = provideDefaultDesktopNames(voice, sections);
        final ViewPath vp = new ViewPath(ZDesktopManager.dockWORKSPACE, names);
        return new ViewInstance() {
            public JComponent getView() throws ComponentGenerationException {
                try {
                    DeviceWorkspaceEnclosurePanel dep = new DeviceWorkspaceEnclosurePanel();
                    VoiceSectionPanel vsp = new VoiceSectionPanel(tp, ViewIndexFactory.getVoiceSectionIndex(sections));
                    int gridIndex = 0;
                    if ((sections & VoiceSections.VOICE_CORDS) != 0) {
                        CordPanel p = new CordPanel();
                        p.init(voice);
                        vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                    }
                    if ((sections & VoiceSections.VOICE_AMP_FILTER) != 0) {
                        AmplifierPanel amp = new AmplifierPanel();
                        amp.init(voice);
                        vsp.addAnchoredComponent(amp, gridIndex++, 0, GridBagConstraints.NORTH);

                        FilterPanel filt = new FilterPanel();
                        filt.init(voice);
                        vsp.addAnchoredComponent(filt, gridIndex++, 0, GridBagConstraints.NORTH);
                    } else {
                        if ((sections & VoiceSections.VOICE_AMP) != 0) {
                            AmplifierPanel p = new AmplifierPanel();
                            p.init(voice);
                            vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                        }
                        if ((sections & VoiceSections.VOICE_FILTER) != 0) {
                            FilterPanel p = new FilterPanel();
                            p.init(voice);
                            vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                        }
                    }
                    if ((sections & VoiceSections.VOICE_LFO) != 0) {
                        LFOPanel p = new LFOPanel();
                        p.init(voice);
                        vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                    }
                    if ((sections & VoiceSections.VOICE_TUNING) != 0) {
                        TuningPanel p = new TuningPanel();
                        p.init(voice);
                        vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                    }
                    if ((sections & VoiceSections.VOICE_ENVELOPES) != 0) {
                        AmpEnvelopePanel amp = new AmpEnvelopePanel();
                        amp.init(voice);
                        vsp.addAnchoredComponent(amp, gridIndex++, 0, GridBagConstraints.NORTH);

                        FilterEnvelopePanel filt = new FilterEnvelopePanel();
                        filt.init(voice);
                        vsp.addAnchoredComponent(filt, gridIndex++, 0, GridBagConstraints.NORTH);

                        AuxEnvelopePanel aux = new AuxEnvelopePanel();
                        aux.init(voice);
                        vsp.addAnchoredComponent(aux, gridIndex++, 0, GridBagConstraints.NORTH);
                    } else {
                        if ((sections & VoiceSections.VOICE_AMP_ENVELOPE) != 0) {
                            AmpEnvelopePanel p = new AmpEnvelopePanel();
                            p.init(voice);
                            vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                        }
                        if ((sections & VoiceSections.VOICE_FILTER_ENVELOPE) != 0) {
                            FilterEnvelopePanel p = new FilterEnvelopePanel();
                            p.init(voice);
                            vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                        }
                        if ((sections & VoiceSections.VOICE_AUX_ENVELOPE) != 0) {
                            AuxEnvelopePanel p = new AuxEnvelopePanel();
                            p.init(voice);
                            vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                        }
                    }
                    dep.init(voice.getPreset().getDeviceContext(), vsp);
                    return dep;
                } catch (Exception e) {
                    throw new ComponentGenerationException(e.getMessage());
                }
            }

            public DesktopName getDesktopName() {
                return names[names.length - 1];
            }

            public ViewPath getViewPath() {
                return vp;
            }
        };
    }

    public static ViewInstance provideDefaultView(final ContextEditablePreset.EditableVoice[] voices, final boolean empty) {
        final DesktopName[] names = provideDefaultDesktopNames(voices);
        final ViewPath vp = new ViewPath(ZDesktopManager.dockWORKSPACE, names);
        return new ViewInstance() {
            public JComponent getView() throws ComponentGenerationException {
                try {
                    DeviceWorkspaceEnclosurePanel dep = new DeviceWorkspaceEnclosurePanel();
                    if (empty) {
                        EmptyEditableVoicePanel evp = new EmptyEditableVoicePanel();
                        evp.init(voices);
                        dep.init(voices[0].getPreset().getDeviceContext(), evp);
                    } else
                        dep.init(voices[0].getPreset().getDeviceContext(), new EditableVoicePanel().init(voices));
                    return dep;
                } catch (Exception e) {
                    throw new ComponentGenerationException(e.getMessage());
                }
            }

            public DesktopName getDesktopName() {
                return names[names.length - 1];
            }

            public ViewPath getViewPath() {
                return vp;
            }
        };
    }

    public static ViewInstance provideDefaultView(final ContextEditablePreset.EditableVoice[] voices, final int sections, final TitleProvider tp) {
        final DesktopName[] names = provideDefaultDesktopNames(voices, sections);
        final ViewPath vp = new ViewPath(ZDesktopManager.dockWORKSPACE, names);
        return new ViewInstance() {
            public JComponent getView() throws ComponentGenerationException {
                try {
                    int gridIndex = 0;
                    DeviceWorkspaceEnclosurePanel dep = new DeviceWorkspaceEnclosurePanel();
                    VoiceSectionPanel vsp = new VoiceSectionPanel(tp, ViewIndexFactory.getVoiceSectionIndex(sections));
                    if ((sections & VoiceSections.VOICE_CORDS) != 0) {
                        EditableCordPanel p = new EditableCordPanel();
                        p.init(voices);
                        vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                    }
                    if ((sections & VoiceSections.VOICE_AMP_FILTER) != 0) {
                        EditableAmplifierPanel amp = new EditableAmplifierPanel();
                        amp.init(voices);
                        vsp.addAnchoredComponent(amp, gridIndex++, 0, GridBagConstraints.NORTH);

                        EditableFilterPanel filt = new EditableFilterPanel();
                        filt.init(voices);
                        vsp.addAnchoredComponent(filt, gridIndex++, 0, GridBagConstraints.NORTH);
                    } else {
                        if ((sections & VoiceSections.VOICE_AMP) != 0) {
                            EditableAmplifierPanel p = new EditableAmplifierPanel();
                            p.init(voices);
                            vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                        }
                        if ((sections & VoiceSections.VOICE_FILTER) != 0) {
                            EditableFilterPanel p = new EditableFilterPanel();
                            p.init(voices);
                            vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                        }
                    }
                    if ((sections & VoiceSections.VOICE_LFO) != 0) {
                        EditableLFOPanel p = new EditableLFOPanel();
                        p.init(voices);
                        vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                    }
                    if ((sections & VoiceSections.VOICE_TUNING) != 0) {
                        EditableTuningPanel p = new EditableTuningPanel();
                        p.init(voices);
                        vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                    }
                    if ((sections & VoiceSections.VOICE_ENVELOPES) != 0) {
                        EditableAmpEnvelopePanel amp = new EditableAmpEnvelopePanel();
                        amp.init(voices);
                        vsp.addAnchoredComponent(amp, gridIndex++, 0, GridBagConstraints.NORTH);

                        EditableFilterEnvelopePanel filt = new EditableFilterEnvelopePanel();
                        filt.init(voices);
                        vsp.addAnchoredComponent(filt, gridIndex++, 0, GridBagConstraints.NORTH);

                        EditableAuxEnvelopePanel aux = new EditableAuxEnvelopePanel();
                        aux.init(voices);
                        vsp.addAnchoredComponent(aux, gridIndex++, 0, GridBagConstraints.NORTH);
                    } else {
                        if ((sections & VoiceSections.VOICE_AMP_ENVELOPE) != 0) {
                            EditableAmpEnvelopePanel p = new EditableAmpEnvelopePanel();
                            p.init(voices);
                            vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                        }
                        if ((sections & VoiceSections.VOICE_FILTER_ENVELOPE) != 0) {
                            EditableFilterEnvelopePanel p = new EditableFilterEnvelopePanel();
                            p.init(voices);
                            vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                        }
                        if ((sections & VoiceSections.VOICE_AUX_ENVELOPE) != 0) {
                            EditableAuxEnvelopePanel p = new EditableAuxEnvelopePanel();
                            p.init(voices);
                            vsp.addAnchoredComponent(p, gridIndex++, 0, GridBagConstraints.NORTH);
                        }
                    }
                    dep.init(voices[0].getPreset().getDeviceContext(), vsp);
                    return dep;
                } catch (Exception e) {
                    throw new ComponentGenerationException(e.getMessage());
                }
            }

            public DesktopName getDesktopName() {
                return names[names.length - 1];
            }

            public ViewPath getViewPath() {
                return vp;
            }
        };
    }

    // PALETTES
    public static ViewInstance provideDeviceView(final DeviceContext device) {
        final DesktopName name = new DesktopName(DevicePanel.class, PathFactory.provideDevicePath(device));
        final ViewPath vp = new ViewPath(ZDesktopManager.dockDEVICES, name);
        return new ViewInstance() {
            public JComponent getView() throws ComponentGenerationException {
                try {
                    DefaultDeviceEnclosurePanel dep = new DefaultDeviceEnclosurePanel();
                    DevicePanel dp = new DevicePanel(device);
                    dep.init(device, dp);
                    return dep;
                } catch (Exception e) {
                    throw new ComponentGenerationException(e.getMessage());
                }
            }

            public DesktopName getDesktopName() {
                return name;
            }

            public ViewPath getViewPath() {
                return vp;
            }
        };
    }

    public static ViewInstance providePropertiesView(final DeviceContext device) {
        final DesktopName name = new DesktopName(PropertiesPanel.class, PathFactory.providePropertiesPath(device));
        final ViewPath vp = new ViewPath(ZDesktopManager.dockPROPERTIES, name);
        return new ViewInstance() {
            public JComponent getView() throws ComponentGenerationException {
                try {
                    ArrayList props = new ArrayList();
                    props.addAll(device.getDevicePreferences().getPropertyList());
                    props.addAll(device.getRemotePreferences().getPropertyList());
                    PropertiesPanel pp = new PropertiesPanel(props, device);
                    return pp;
                } catch (Exception e) {
                    throw new ComponentGenerationException(e.getMessage());
                }
            }

            public DesktopName getDesktopName() {
                return name;
            }

            public ViewPath getViewPath() {
                return vp;
            }
        };
    }

    public static ViewInstance provideDefaultPresetContextView(final DeviceContext device) {
        final DesktopName name = new DesktopName(PresetContextEnclosurePanel.class, PathFactory.providePresetsPath(device));
        final ViewPath vp = new ViewPath(ZDesktopManager.dockPRESETS, name);
        return new ViewInstance() {
            public JComponent getView() throws ComponentGenerationException {
                try {
                    PresetContextEnclosurePanel pcep = new PresetContextEnclosurePanel();
                    pcep.init(device);
                    return pcep;
                } catch (Exception e) {
                    throw new ComponentGenerationException(e.getMessage());
                }
            }

            public DesktopName getDesktopName() {
                return name;
            }

            public ViewPath getViewPath() {
                return vp;
            }
        };
    }

    public static ViewInstance provideDefaultSampleContextView(final DeviceContext device) {
        final DesktopName name = new DesktopName(SampleContextEnclosurePanel.class, PathFactory.provideSamplesPath(device));
        final ViewPath vp = new ViewPath(ZDesktopManager.dockSAMPLES, name);
        return new ViewInstance() {
            public JComponent getView() throws ComponentGenerationException {
                try {
                    SampleContextEnclosurePanel scep = new SampleContextEnclosurePanel();
                    scep.init(device);
                    return scep;
                } catch (Exception e) {
                    throw new ComponentGenerationException(e.getMessage());
                }
            }

            public DesktopName getDesktopName() {
                return name;
            }

            public ViewPath getViewPath() {
                return vp;
            }
        };
    }

    public static ViewInstance provideMultiModeView(final DeviceContext device) {
        final DesktopName name = new DesktopName(MultiModeEnclosurePanel.class, PathFactory.provideMultiModePath(device));
        final ViewPath vp = new ViewPath(ZDesktopManager.dockMULTI, name);
        return new ViewInstance() {
            public JComponent getView() throws ComponentGenerationException {
                try {
                    MultiModeEnclosurePanel mmep = new MultiModeEnclosurePanel();
                    mmep.init(device);
                    return mmep;
                } catch (Exception e) {
                    throw new ComponentGenerationException(e.getMessage());
                }
            }

            public DesktopName getDesktopName() {
                return name;
            }

            public ViewPath getViewPath() {
                return vp;
            }
        };
    }

    public static ViewInstance provideMasterView(final DeviceContext device) {
        final DesktopName name = new DesktopName(MasterEnclosurePanel.class, PathFactory.provideMasterPath(device));
        final ViewPath vp = new ViewPath(ZDesktopManager.dockMASTER, name);
        return new ViewInstance() {
            public JComponent getView() throws ComponentGenerationException {
                try {
                    MasterEnclosurePanel mep = new MasterEnclosurePanel();
                    mep.init(device);
                    return mep;
                } catch (Exception e) {
                    throw new ComponentGenerationException(e.getMessage());
                }
            }

            public DesktopName getDesktopName() {
                return name;
            }

            public ViewPath getViewPath() {
                return vp;
            }
        };
    }
}
