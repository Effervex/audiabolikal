package audiabolikal.itemBuilding;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A class for defining and viewing the items present in the system.
 * 
 * @author Samuel J. Sarjant
 */
public class ItemBuilder extends JFrame {
	public static final int GAP_SIZE = 5;
	private ItemsListPanel itemsList_;
	private ItemDetailsPanel itemDetails_;
	private ItemModelPanel itemModel_;

	public ItemBuilder() {
		initialise();
		pack();
		setVisible(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initialise() {
		GridLayout layout = new GridLayout(1, 3);
		layout.setVgap(GAP_SIZE);
		layout.setHgap(GAP_SIZE);
		setLayout(layout);

		itemsList_ = new ItemsListPanel(this);
		add(itemsList_);

		itemDetails_ = new ItemDetailsPanel(this);
		add(itemDetails_);

		itemModel_ = new ItemModelPanel(this);
		add(itemModel_);
	}

	public static void main(String[] args) {
		ItemBuilder ib = new ItemBuilder();
	}

	/**
	 * Creates a labelled component set within a JPanel.
	 * 
	 * @param component
	 *            The component receiving a label.
	 * @param componentLabel
	 *            The label for the component.
	 * @return A JPanel containing the label followed by the component.
	 */
	public static Component createLabelledComponent(Component component,
			String componentLabel) {
		JPanel labelComp = new JPanel();
		labelComp.add(new JLabel(componentLabel));
		labelComp.add(component);
		return labelComp;
	}
}
