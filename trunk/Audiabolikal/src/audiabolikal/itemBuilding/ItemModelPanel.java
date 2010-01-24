package audiabolikal.itemBuilding;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import audiabolikal.equipment.Item;

public class ItemModelPanel extends JPanel {
	private Frame parentFrame_;
	
	public ItemModelPanel(Frame parent) {
		parentFrame_ = parent;
		initialise();
	}

	private void initialise() {
		BorderLayout layout = new BorderLayout(ItemBuilder.GAP_SIZE,
				ItemBuilder.GAP_SIZE);
		setLayout(layout);
		
		setBorder(BorderFactory.createEtchedBorder());
	}

	/**
	 * Loads an item's model onto the model box, if it has one.
	 * 
	 * @param item The item being loaded up.
	 */
	public void loadItemDetails(Item item) {
		// TODO Auto-generated method stub
		
	}
}
