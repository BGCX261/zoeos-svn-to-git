package com.pcmsolutions.device.EMU.E4.zcommands.icons;

import com.pcmsolutions.device.EMU.E4.gui.colors.UIColors;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

/**
 * User: paulmeehan
 * Date: 05-May-2004
 * Time: 14:53:07
 */
public class PresetPackageIcon extends PackageIcon implements Icon{
    PresetPackageIcon(boolean open){
        super(open, UIColors.getTableFirstSectionHeaderBG(), UIColors.getTableSecondSectionHeaderBG(), UIColors.getTableThirdSectionHeaderBG());
    }     
}
