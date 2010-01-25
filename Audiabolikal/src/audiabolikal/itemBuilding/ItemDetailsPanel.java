package audiabolikal.itemBuilding;

import info.clearthought.layout.TableLayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import audiabolikal.equipment.Item;

@SuppressWarnings("serial")
public class ItemDetailsPanel extends JPanel implements ActionListener,
		FocusListener {
	private static final int ATTRIBS_TOTAL = 40;
	private ItemBuilder parentFrame_;
	private JComboBox itemTypeCom_;
	private JTextField itemNameFld_;
	private JLabel attribsLeftLbl_;
	private JSpinner[] attribsSpn_ = new JSpinner[4];
	private JSpinner[] varianceSpn_ = new JSpinner[4];
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

	public ItemDetailsPanel(ItemBuilder parent) {
		parentFrame_ = parent;
		initialise();
	}

	private void initialise() {
		double p = TableLayout.PREFERRED;
		double b = ItemBuilder.BORDER;
		double f = TableLayout.FILL;
		double[][] size = {
				{ b, f, f, f, f, b },
				{ b, p, p, b, p, p, p, p, p, f, p, p, f, p, p, p, p, p, p, p, b,
						p, b } };
		TableLayout layout = new TableLayout(size);
		layout.setVGap(ItemBuilder.GAP_SIZE);
		layout.setHGap(ItemBuilder.GAP_SIZE);
		setLayout(layout);

		setBorder(BorderFactory.createEtchedBorder());

		// Type and name
		String[] itemTypes = ItemBuilder.getItemTypes(false);
		itemTypeCom_ = new JComboBox(itemTypes);
		add(ItemBuilder.createLabelledComponent(itemTypeCom_, "Item Type:"),
				"1,1,4,1,f,c");
		itemNameFld_ = new JTextField();
		add(ItemBuilder.createLabelledComponent(itemNameFld_, "Name:"),
				"1,2,4,2,f,c");

		// Attributes
		attribsLeftLbl_ = new JLabel("Attribute Points: " + ATTRIBS_TOTAL);
		add(attribsLeftLbl_, "1,4,2,4,f,c");
		String[] attribs = { "ATK", "DEF", "HIT", "EVA" };
		for (int i = 0; i < attribsSpn_.length; i++) {
			attribsSpn_[i] = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
			String loc = (i % 2) * 2 + 1 + "," + (i / 2 + 5);
			add(ItemBuilder.createLabelledComponent(attribsSpn_[i], attribs[i]
					+ ": "), loc);
			varianceSpn_[i] = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
			loc = (i % 2) * 2 + 2 + "," + (i / 2 + 5);
			add(ItemBuilder.createLabelledComponent(varianceSpn_[i], " ±"), loc);
		}

		// Value mod
		valueModFld_ = new JTextField("0");
		add(ItemBuilder.createLabelledComponent(valueModFld_, "Value Mod:"),
				"1, 7, 2, 7, f, c");
		averageValueLbl_ = new JLabel("Average Value: 0");
		add(averageValueLbl_, "3, 7, 4, 7, r, c");

		// Genres list
		add(new JLabel("Genres:"), "1, 8");
		TableModel model = new DistributionTableModel("Genre");
		genreTable_ = new JTable(model);
		genreTable_.setPreferredScrollableViewportSize(new Dimension(100, 100));
		JScrollPane genreScroller = new JScrollPane(genreTable_);
		add(genreScroller, "1, 9, 4, 9, f, f");
		add(ItemBuilder.createButton("Add Genre", this), "1,10,2,10,c,c");
		add(ItemBuilder.createButton("Remove Genre", this), "3,10,4,10,c,c");

		// Colours list
		add(new JLabel("Colours:"), "1, 11");
		model = new DistributionTableModel("Colour");
		colourTable_ = new JTable(model);
		colourTable_
				.setPreferredScrollableViewportSize(new Dimension(100, 100));
		JScrollPane colourScroller = new JScrollPane(colourTable_);
		add(colourScroller, "1, 12, 4, 12, f, f");
		add(ItemBuilder.createButton("Add Colour", this), "1,13,2,13,c,c");
		add(ItemBuilder.createButton("Remove Colour", this), "3,13,4,13,c,c");

		// Mesh and Texture buttons
		maleMeshFld_ = new JTextField();
		maleMeshFld_.addFocusListener(this);
		add(ItemBuilder.createLabelledComponent(maleMeshFld_, "Male Mesh:"), "1,14,4,14,f,c");
		maleTextureFld_ = new JTextField();
		maleTextureFld_.addFocusListener(this);
		add(ItemBuilder.createLabelledComponent(maleTextureFld_, "Male Texture:"), "1,15,4,15,f,c");
		femaleMeshFld_ = new JTextField();
		femaleMeshFld_.addFocusListener(this);
		add(ItemBuilder.createLabelledComponent(femaleMeshFld_, "Female Mesh:"), "1,16,4,16,f,c");
		femaleTextureFld_ = new JTextField();
		femaleTextureFld_.addFocusListener(this);
		add(ItemBuilder.createLabelledComponent(femaleTextureFld_, "Female Texture:"), "1,17,4,17,f,c");
		
		// Rotation boxes
		JPanel rotationPanel = new JPanel();
		rotationX_ = new JTextField("0", 3);
		rotationPanel.add(ItemBuilder.createLabelledComponent(rotationX_, "Rotation - X:"));
		rotationY_ = new JTextField("0", 3);
		rotationPanel.add(ItemBuilder.createLabelledComponent(rotationY_, "Y:"));
		rotationZ_ = new JTextField("0", 3);
		rotationPanel.add(ItemBuilder.createLabelledComponent(rotationZ_, "Z:"));
		add(rotationPanel, "1,18,4,18,f,c");
		
		// Scale boxes
		JPanel scalePanel = new JPanel();
		scaleX_ = new JTextField("1.0", 5);
		scalePanel.add(ItemBuilder.createLabelledComponent(scaleX_, "Scale - X:"));
		scaleY_ = new JTextField("1.0", 5);
		scalePanel.add(ItemBuilder.createLabelledComponent(scaleY_, "Y:"));
		scaleZ_ = new JTextField("1.0", 5);
		scalePanel.add(ItemBuilder.createLabelledComponent(scaleZ_, "Z:"));
		add(scalePanel, "1,19,4,19,f,c");
		
		// Apply and Cancel buttons
		add(ItemBuilder.createButton("Apply", this), "3,21");
		add(ItemBuilder.createButton("Cancel", this), "4,21");

		updateAttribsStats();
	}

	/**
	 * Updates the number of attributes remaining and the average value of the
	 * item.
	 */
	private void updateAttribsStats() {
		int sum = 0;
		int i = 0;
		for (i = 0; i < attribsSpn_.length; i++) {
			sum += ((SpinnerNumberModel) attribsSpn_[i].getModel()).getNumber()
					.intValue();
		}
		attribsLeftLbl_.setText("Attribute Points: " + (ATTRIBS_TOTAL - sum));

		double value = Double.parseDouble(valueModFld_.getText());
		if (sum == 0)
			sum = 1;

		averageValueLbl_.setText("Average Value: " + (int) (value * sum));
	}

	/**
	 * Loads an item's details into their appropriate boxes.
	 * 
	 * @param item
	 *            The item being loaded.
	 */
	public void loadItemDetails(Item item) {
		if (item != currentItem_) {
			currentItem_ = item;
		}
	}

	/**
	 * Stores the current details into the item.
	 */
	private void storeDetails() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Apply")) {
			storeDetails();
			parentFrame_.itemsList_.updateListPanel();
			parentFrame_.itemModel_.loadItemDetails(currentItem_);
		} else if (e.getActionCommand().equals("Cancel")) {

		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		Component focusGained = arg0.getComponent();
		System.out.println(focusGained);
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("serial")
	private class DistributionTableModel extends AbstractTableModel {
		private String[] columnHeader_;
		private ArrayList<Object> data_;
		private ArrayList<Double> values_;

		public DistributionTableModel(String dataName) {
			columnHeader_ = new String[2];
			columnHeader_[0] = dataName;
			columnHeader_[1] = "Value";

			data_ = new ArrayList<Object>();
			values_ = new ArrayList<Double>();
		}

		@Override
		public String getColumnName(int index) {
			return columnHeader_[index];
		}

		@Override
		public Class<?> getColumnClass(int index) {
			if (index == 0)
				return Object.class;
			if (index == 1)
				return Double.class;
			return null;
		}

		@Override
		public int getColumnCount() {
			return columnHeader_.length;
		}

		@Override
		public int getRowCount() {
			return data_.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return data_.get(rowIndex);
			} else if (columnIndex == 1) {
				return values_.get(rowIndex);
			}
			return null;
		}

	}
}
