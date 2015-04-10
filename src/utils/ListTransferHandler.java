/*
Copyright (C) <2015>  <Ziliang Zhao and Shih-Lung Shaw>

STEAM is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

STEAM is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with STEAM.  If not, see <http://www.gnu.org/licenses/>
*/

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
