package audiabolikal.itemBuilding;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;

import audiabolikal.equipment.Item;

public class ItemDetailsPanel extends JPanel implements ActionListener {
	private static final int ATTRIBS_TOTAL = 40;
	private ItemBuilder parentFrame_;
	private JComboBox itemTypeCom_;
	private JTextField itemNameFld_;
	private JLabel attribsLeftLbl_;
	private JSpinner[] attribsSpn_;
	private JSpinner[] varianceSpn_;
	private JTextField valueModFld_;
	private JLabel averageValueLbl_;
	private JTable genreTable_;
	private JTable colourTable_;
	private JTextField maleMeshFld_;
	private JTextField maleTextureFld_;
	private JTextField femaleMeshFld_;
	private JTextField femaleTextureFld_;
	private JTextField rotationX_;
	private JTextField rotationY_;
	private JTextField rotationZ_;
	private JTextField scaleX_;
	private JTextField scaleY_;
	private JTextField scaleZ_;
	
	private Item currentItem_;
	private boolean detailsChanged_;
	
	
	public ItemDetailsPanel(ItemBuilder parent) {
		parentFrame_ = parent;
		detailsChanged_ = false;
		initialise();
	}

	private void initialise() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		setBorder(BorderFactory.createEtchedBorder());
	}

	/**
	 * Loads an item's details into their appropriate boxes.
	 * 
	 * @param item The item being loaded.
	 */
	public void loadItemDetails(Item item) {
		if (item != currentItem_) {
			currentItem_ = item;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Apply")) {
			
		} else if (e.getActionCommand().equals("Cancel")) {
			
		}
	}
}
