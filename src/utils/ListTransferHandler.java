package utils;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

public class ListTransferHandler extends TransferHandler {
    
    private int[] indices = null;
    private int addIndex = -1;
    private int addCount = 0;

    public boolean canImport(TransferHandler.TransferSupport info) {
	if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	    return false;
	}
	return true;
    }

    protected Transferable createTransferable(JComponent c) {
	JList list = (JList) c;
	indices = list.getSelectedIndices();
	Object[] values = list.getSelectedValues();

	StringBuffer buff = new StringBuffer();

	for (int i = 0; i < values.length; i++) {
	    Object val = values[i];
	    buff.append(val == null ? "" : val.toString());
	    if (i != values.length - 1) {
		buff.append("\n");
	    }
	}

	return new StringSelection(buff.toString());
    }

    public int getSourceActions(JComponent c) {
	return TransferHandler.MOVE;
    }

    public boolean importData(TransferHandler.TransferSupport info) {
	if (!info.isDrop()) {
	    return false;
	}

	JList list = (JList) info.getComponent();
	DefaultListModel listModel = (DefaultListModel) list.getModel();
	JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
	int index = dl.getIndex();
	boolean insert = dl.isInsert();

	Transferable t = info.getTransferable();
	String data;
	try {
	    data = (String) t.getTransferData(DataFlavor.stringFlavor);
	} catch (Exception e) {
	    return false;
	}

	String[] values = data.split("\n");

	addIndex = index;
	addCount = values.length;

	for (int i = 0; i < values.length; i++) {
	    if (insert) {
		listModel.add(index++, values[i]);
	    } else {
		if (index < listModel.getSize()) {
		    listModel.set(index++, values[i]);
		} else {
		    listModel.add(index++, values[i]);
		}
	    }
	}
	return true;
    }

    protected void exportDone(JComponent c, Transferable data, int action) {
	JList source = (JList) c;
	DefaultListModel listModel = (DefaultListModel) source.getModel();

	if (action == TransferHandler.MOVE) {
	    for (int i = indices.length - 1; i >= 0; i--) {
		if (indices[i] < addIndex) {
		    listModel.remove(indices[i]);
		} else {
		    listModel.remove(indices[i] + addCount);
		}
	    }
	}

	indices = null;
	addCount = 0;
	addIndex = -1;
    }
    
}
