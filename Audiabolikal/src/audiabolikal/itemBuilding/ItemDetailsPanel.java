package audiabolikal.itemBuilding;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import audiabolikal.TagHierarchy;
import audiabolikal.equipment.Item;
import audiabolikal.util.ProbabilityDistribution;

@SuppressWarnings("serial")
public class ItemDetailsPanel extends JPanel implements ActionListener,
		FocusListener, ChangeListener {
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
	private JButton applyButton_;

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
				{ b, p, p, b, p, p, p, p, p, f, p, p, f, p, p, p, p, p, p, p,
						b, p, b } };
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
		add(ItemBuilder.createButton("Jiggle Attributes", this), "3,4,4,4,r,c");
		String[] attribs = { "ATK", "DEF", "HIT", "EVA" };
		for (int i = 0; i < attribsSpn_.length; i++) {
			attribsSpn_[i] = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
			attribsSpn_[i].addChangeListener(this);
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
		TableModel model = new DefaultTableModel(new Object[] { "Genre",
				"Value" }, 0);
		genreTable_ = new JTable(model);
		genreTable_.setPreferredScrollableViewportSize(new Dimension(10, 50));
		JScrollPane genreScroller = new JScrollPane(genreTable_);
		add(genreScroller, "1, 9, 4, 9, f, f");
		add(ItemBuilder.createButton("Add Genre", this), "1,10,2,10,c,c");
		add(ItemBuilder.createButton("Remove Genre", this), "3,10,4,10,c,c");

		// Colours list
		add(new JLabel("Colours:"), "1, 11");
		model = new ColourTableModel(new Object[] { "Colour", "Value" }, 0);
		colourTable_ = new JTable(model);
		colourTable_.setPreferredScrollableViewportSize(new Dimension(10, 50));
		colourTable_.setDefaultRenderer(Color.class, new ColorRenderer(true));
		JScrollPane colourScroller = new JScrollPane(colourTable_);
		add(colourScroller, "1, 12, 4, 12, f, f");
		add(ItemBuilder.createButton("Add Colour", this), "1,13,2,13,c,c");
		add(ItemBuilder.createButton("Remove Colour", this), "3,13,4,13,c,c");

		// Mesh and Texture buttons
		maleMeshFld_ = new JTextField();
		maleMeshFld_.addFocusListener(this);
		add(ItemBuilder.createLabelledComponent(maleMeshFld_, "Male Mesh:"),
				"1,14,4,14,f,c");
		maleTextureFld_ = new JTextField();
		maleTextureFld_.addFocusListener(this);
		add(ItemBuilder.createLabelledComponent(maleTextureFld_,
				"Male Texture:"), "1,15,4,15,f,c");
		femaleMeshFld_ = new JTextField();
		femaleMeshFld_.addFocusListener(this);
		add(
				ItemBuilder.createLabelledComponent(femaleMeshFld_,
						"Female Mesh:"), "1,16,4,16,f,c");
		femaleTextureFld_ = new JTextField();
		femaleTextureFld_.addFocusListener(this);
		add(ItemBuilder.createLabelledComponent(femaleTextureFld_,
				"Female Texture:"), "1,17,4,17,f,c");

		// Rotation boxes
		JPanel rotationPanel = new JPanel();
		rotationX_ = new JTextField("0", 3);
		rotationPanel.add(ItemBuilder.createLabelledComponent(rotationX_,
				"Rotation - X:"));
		rotationY_ = new JTextField("0", 3);
		rotationPanel
				.add(ItemBuilder.createLabelledComponent(rotationY_, "Y:"));
		rotationZ_ = new JTextField("0", 3);
		rotationPanel
				.add(ItemBuilder.createLabelledComponent(rotationZ_, "Z:"));
		add(rotationPanel, "1,18,4,18,f,c");

		// Scale boxes
		JPanel scalePanel = new JPanel();
		scaleX_ = new JTextField("1.0", 5);
		scalePanel.add(ItemBuilder.createLabelledComponent(scaleX_,
				"Scale - X:"));
		scaleY_ = new JTextField("1.0", 5);
		scalePanel.add(ItemBuilder.createLabelledComponent(scaleY_, "Y:"));
		scaleZ_ = new JTextField("1.0", 5);
		scalePanel.add(ItemBuilder.createLabelledComponent(scaleZ_, "Z:"));
		add(scalePanel, "1,19,4,19,f,c");

		// Apply and Cancel buttons
		applyButton_ = ItemBuilder.createButton("Apply", this);
		applyButton_.setEnabled(false);
		add(applyButton_, "4,21");
		// add(ItemBuilder.createButton("Cancel", this), "4,21");

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
		if (item == null) {
			applyButton_.setEnabled(false);
		} else {
			applyButton_.setEnabled(true);
			itemTypeCom_.setSelectedItem(item.getClass().getSimpleName());
			itemNameFld_.setText(item.getName());

			// For each attribute
			attribsSpn_[0].setValue(item.getBaseAttack());
			attribsSpn_[1].setValue(item.getBaseDefense());
			attribsSpn_[2].setValue(item.getBaseHit());
			attribsSpn_[3].setValue(item.getBaseEvasion());
			varianceSpn_[0].setValue(item.getAttackVariance());
			varianceSpn_[1].setValue(item.getDefenseVariance());
			varianceSpn_[2].setValue(item.getHitVariance());
			varianceSpn_[3].setValue(item.getEvasionVariance());
			valueModFld_.setText(item.getValueMod() + "");
			updateAttribsStats();
		}
	}

	/**
	 * Stores the current details into the item.
	 */
	private void storeDetails(Item item) {
		// Parsing the data
		String name = itemNameFld_.getText();
		Map<String, Double> genres = compileGenres();
		ProbabilityDistribution<Color> itemColors = compileColour();
		float valueMod = Float.parseFloat(valueModFld_.getText());
		float[] attributes = new float[attribsSpn_.length * 2];
		for (int i = 0; i < attribsSpn_.length; i++) {
			attributes[i * 2] = ((SpinnerNumberModel) attribsSpn_[i].getModel())
					.getNumber().intValue();
			attributes[i * 2 + 1] = ((SpinnerNumberModel) varianceSpn_[i]
					.getModel()).getNumber().intValue();
		}
		File[] modelFiles = { new File(maleMeshFld_.getText()),
				new File(femaleMeshFld_.getText()),
				new File(maleTextureFld_.getText()),
				new File(femaleTextureFld_.getText()) };
		float[] rotation = { Float.parseFloat(rotationX_.getText()),
				Float.parseFloat(rotationY_.getText()),
				Float.parseFloat(rotationZ_.getText()) };
		float[] scale = { Float.parseFloat(scaleX_.getText()),
				Float.parseFloat(scaleY_.getText()),
				Float.parseFloat(scaleZ_.getText()) };

		item.initialiseMouldItem(name, genres, itemColors, valueMod,
				attributes, modelFiles, rotation, scale);
	}

	/**
	 * Compiles the mapping of genres from the genre table.
	 * 
	 * @return A mapping of genres to weights for the item.
	 */
	private Map<String, Double> compileGenres() {
		Map<String, Double> genreMap = new HashMap<String, Double>();
		DefaultTableModel model = (DefaultTableModel) genreTable_.getModel();

		// For each genre in the table.
		for (int y = 0; y < model.getRowCount(); y++) {
			String genre = (String) model.getValueAt(y, 0);
			Double value = (Double) model.getValueAt(y, 1);
			genreMap.put(genre, value);
		}

		return genreMap;
	}

	/**
	 * Compiles a distribution of colours the item can take on.
	 * 
	 * @return A normalised Probability Distribution of Colors.
	 */
	private ProbabilityDistribution<Color> compileColour() {
		ProbabilityDistribution<Color> colors = new ProbabilityDistribution<Color>();

		DefaultTableModel model = (DefaultTableModel) genreTable_.getModel();

		// For each genre in the table.
		for (int y = 0; y < model.getRowCount(); y++) {
			Color genre = (Color) model.getValueAt(y, 0);
			Double value = (Double) model.getValueAt(y, 1);
			colors.add(genre, value);
		}

		return colors;
	}

	/**
	 * Removes all occurrences from a table from a collection.
	 * 
	 * @param table
	 *            The table.
	 * @param collection
	 *            The collection of things.
	 */
	private void removeExisting(JTable table, Collection<?> collection) {
		for (int y = 0; y < table.getRowCount(); y++) {
			collection.remove(table.getValueAt(y, 0));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Apply")) {
			try {
				// Check item is of the same type.
				Item item = parentFrame_.getCurrentItem();
				if (!itemTypeCom_.getSelectedItem().equals(item.getClassName())) {
					item = (Item) Class
							.forName(
									"audiabolikal.equipment."
											+ itemTypeCom_.getSelectedItem()
													.toString()).newInstance();
				}

				storeDetails(item);
				parentFrame_.setCurrentItem(item, this);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (e.getActionCommand().equals("Add Genre")) {
			TreeSet<String> genreSet = new TreeSet<String>(TagHierarchy
					.getInstance().getTags());
			removeExisting(genreTable_, genreSet);
			String[] genres = genreSet.toArray(new String[genreSet.size()]);
			String result = (String) JOptionPane.showInputDialog(parentFrame_,
					"Choose a genre:", "Genre Weighting",
					JOptionPane.PLAIN_MESSAGE, null, genres, null);
			if (result == null)
				return;

			// Get the weight value
			Double value = -1.0;
			while ((value <= 0) || (value > 1)) {
				String weight = JOptionPane.showInputDialog(parentFrame_,
						"Weighting for '" + result + "' (0.1-1.0):",
						"Genre Weighting", JOptionPane.PLAIN_MESSAGE);
				if (weight == null)
					return;
				try {
					value = Double.parseDouble(weight);
				} catch (Exception ex) {
				}
			}

			DefaultTableModel model = (DefaultTableModel) genreTable_
					.getModel();
			model.addRow(new Object[] { result, value });
		} else if (e.getActionCommand().equals("Remove Genre")) {
			int row = genreTable_.getSelectedRow();
			if (row != -1) {
				DefaultTableModel model = (DefaultTableModel) genreTable_
						.getModel();
				model.removeRow(row);
			}
		} else if (e.getActionCommand().equals("Add Colour")) {
			// Bring up a colour chooer to choose a colour.
			JDialog dialog = new JDialog(parentFrame_, true);
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			ColourChooserPanel ccp = new ColourChooserPanel(dialog);
			dialog.add(ccp);
			dialog.pack();
			dialog.setVisible(true);

			Color chosenColour = ccp.getChosenColour();
			if (chosenColour == null)
				return;

			// Get the weight value
			Double value = -1.0;
			while ((value <= 0) || (value > 1)) {
				String weight = JOptionPane.showInputDialog(parentFrame_,
						"Weighting for '" + chosenColour + "' (0.1-1.0):",
						"Genre Weighting", JOptionPane.PLAIN_MESSAGE);
				if (weight == null)
					return;
				try {
					value = Double.parseDouble(weight);
				} catch (Exception ex) {
				}
			}

			DefaultTableModel model = (DefaultTableModel) colourTable_
					.getModel();
			model.addRow(new Object[] { chosenColour, value });
		} else if (e.getActionCommand().equals("Remove Colour")) {
			int row = colourTable_.getSelectedRow();
			if (row != -1) {
				DefaultTableModel model = (DefaultTableModel) colourTable_
						.getModel();
				model.removeRow(row);
			}
		} else if (e.getActionCommand().equals("Jiggle Attributes")) {
			double jiggleAmount = ATTRIBS_TOTAL / 15.0;
			Random random = new Random();
			for (int i = 0; i < attribsSpn_.length; i++) {
				// Attribute
				SpinnerNumberModel model = (SpinnerNumberModel) attribsSpn_[i]
						.getModel();
				double value = model.getNumber().doubleValue();
				value += Math.max(0,
						(int) (random.nextGaussian() * jiggleAmount));
				model.setValue(value);

				// Variance
				model = (SpinnerNumberModel) varianceSpn_[i].getModel();
				value = model.getNumber().doubleValue();
				value += Math.max(0,
						(int) (random.nextGaussian() * jiggleAmount * 0.5));
				model.setValue(value);
			}
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		Component focusGained = arg0.getComponent();
		System.out.println(focusGained);
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// Do nothing
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		updateAttribsStats();
	}

	public class ColourTableModel extends DefaultTableModel {
		public ColourTableModel(Object[] columnNames, int rows) {
			super(columnNames, rows);
		}

		public Class<?> getColumnClass(int column) {
			if (column == 0) {
				return Color.class;
			} else if (column == 1) {
				return String.class;
			}
			return null;
		}
	}
}
