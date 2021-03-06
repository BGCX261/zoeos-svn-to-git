package com.pcmsolutions.device.EMU.E4.gui.preset.presetcontext;

import com.pcmsolutions.device.EMU.E4.RemoteObjectStates;
import com.pcmsolutions.device.EMU.E4.gui.AbstractTableCellRenderer;
import com.pcmsolutions.device.EMU.E4.gui.colors.UIColors;
import com.pcmsolutions.device.EMU.E4.preset.NoSuchPresetException;
import com.pcmsolutions.device.EMU.E4.preset.PresetEmptyException;
import com.pcmsolutions.device.EMU.E4.preset.ReadablePreset;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 01-May-2003
 * Time: 05:25:13
 * To change this template use Options | File Templates.
 */
public class PresetContextTableCellRenderer extends AbstractTableCellRenderer implements TableCellRenderer {
    private double presetInitStatus;

    private static double bubAccelFactor = 2.0;
    private static int bubMinWidth = 15;
    private static int bubVerticalInset = 2;  // at top and bottom
    private static int arcWidth = 20;
    private static int arcHeight = 20;

    public PresetContextTableCellRenderer() {
        setHorizontalAlignment(JLabel.LEFT);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = ((Graphics2D) g);
        int w = getWidth();
        int h = getHeight();
        Icon i = getIcon();
        int iw = 0;
        if (i != null)
            iw = i.getIconWidth();
        int rm = w - iw;
        int lm = iw + 1;
        Color e_bg = getBackground();
        Color tbg = UIColors.applyAlpha(e_bg, UIColors.tableAlpha);
        Color tsbg = e_bg;
        if (isDropCell) {
            GradientPaint gp;
            gp = new GradientPaint(0, h * UIColors.tableDropTargetGradientFactor, UIColors.getDropColor(), 0, 0, Color.white, false);
            g2d.setPaint(gp);
            //g2d.fillRoundRect(0, 0, w, h, 10, 10);
            g2d.fillRect(0, 0, w, h);
            setOpaque(true);
        } else if (selected) {
            GradientPaint gp = new GradientPaint(0, h * UIColors.tableSelectionGradientFactor, tbg, 0, 0, tsbg, false);
            g2d.setPaint(gp);
            //g2d.fillRoundRect(iw, 0, w - iw, h, arcWidth, arcHeight);
            //g2d.fillRoundRect(0, 0, w, h, arcWidth, arcHeight);
            g2d.fillRect(0, 0, w, h);
            setBorder(bdrSel);
            setOpaque(false);
        } else{
            setOpaque(true);
            setBorder(null);
        }
        setBackground(tbg);
        super.paintComponent(g);
        setBackground(e_bg);

        if (presetInitStatus != 0 && presetInitStatus != RemoteObjectStates.STATUS_INITIALIZED) {
            boolean localInit = presetInitStatus > 0;

            presetInitStatus = Math.abs(presetInitStatus);

            double rem = (1.0 - presetInitStatus) * bubAccelFactor;
            int rx = lm + (int) (presetInitStatus * rm);
            int lx = rx - (int) (rem * rm);
            if (rx - lx < bubMinWidth)
                lx = rx - bubMinWidth;
            if (lx < lm)
                lx = lm;

            GradientPaint gp;
            Color c1;
            Color c2;
            c1 = UIColors.getBeginBubbleColor();
            c2 = UIColors.getEndBubbleColor();

            if (localInit) {
                gp = new GradientPaint(rm + lm - rx, 0, c1, rm + lm - lx, 0, c2, false);
                g2d.setPaint(gp);
                g2d.fillRoundRect(rm + lm - rx, bubVerticalInset, rx - lx, h - bubVerticalInset * 2, arcWidth, arcHeight);
            } else {
                gp = new GradientPaint(lx, 0, c1, rx, 0, c2, false);
                g2d.setPaint(gp);
                g2d.fillRoundRect(lx, bubVerticalInset, rx - lx, h - bubVerticalInset * 2, arcWidth, arcHeight);
            }
        }
    }

    protected void buildLabel(Object value) {
        super.buildLabel(value);
        if (value instanceof ReadablePreset) {
            try {
                presetInitStatus = ((ReadablePreset) value).getInitializationStatus();
            } catch (NoSuchPresetException e) {
                presetInitStatus = RemoteObjectStates.STATUS_INITIALIZED;
            } catch (PresetEmptyException e) {
                presetInitStatus = RemoteObjectStates.STATUS_INITIALIZED;
            }
        }
    }

    protected void setupLook(JTable table, Object value, boolean isSelected, int row, int column) {
        super.setupLook(table, value, isSelected, row, column);
        setForeground(UIColors.getTableFirstSectionFG());
        setBackground(UIColors.getTableFirstSectionBG());
    }
}
