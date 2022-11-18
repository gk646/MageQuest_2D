package Main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;

public class MouseHandler implements MouseListener {
    public boolean mousePressed, mousedReleased;

    public Point getMouseLocation() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        return p;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            mousePressed = true;
        }


    }


    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == 1) {
            mousePressed = false;
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
