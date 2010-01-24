package audiabolikal.itemBuilding;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ItemDetailsPanel extends JPanel {
	private Frame parentFrame_;
	
	public ItemDetailsPanel(Frame parent) {
		parentFrame_ = parent;
		initialise();
	}

	private void initialise() {
		BorderLayout layout = new BorderLayout(ItemBuilder.GAP_SIZE,
				ItemBuilder.GAP_SIZE);
		setLayout(layout);
		
		setBorder(BorderFactory.createEtchedBorder());
	}
}
