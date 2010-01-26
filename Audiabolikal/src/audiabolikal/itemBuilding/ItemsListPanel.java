package audiabolikal.itemBuilding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import audiabolikal.TagHierarchy;
import audiabolikal.equipment.Headgear;
import audiabolikal.equipment.Item;
import audiabolikal.util.ProbabilityDistribution;

public class ItemsListPanel extends JPanel implements ActionListener,
		ListSelectionListener {
	private static final int NUM_ROWS_VISIBLE = 10;
	private ItemBuilder parentFrame_;
	private JComboBox itemTypeCom_;
	private JTextField maxValueModFld_;
	private JList itemsList_;
	private JLabel totalATKLbl_;
	private JLabel totalDEFLbl_;
	private JLabel totalHITLbl_;
	private JLabel totalEVALbl_;

	public ItemsListPanel(ItemBuilder parent) {
		parentFrame_ = parent;
		initialise();
	}

	private void initialise() {
		BorderLayout layout = new BorderLayout(ItemBuilder.GAP_SIZE,
				ItemBuilder.GAP_SIZE);
		setLayout(layout);

		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createEtchedBorder(), BorderFactory.createEmptyBorder(
				ItemBuilder.BORDER, ItemBuilder.BORDER, ItemBuilder.BORDER,
				ItemBuilder.BORDER)));

		// Item combo box and value mod
		String[] itemTypes = ItemBuilder.getItemTypes(true);
		itemTypeCom_ = new JComboBox(itemTypes);
		itemTypeCom_.addActionListener(this);
		maxValueModFld_ = new JTextField(8);
		maxValueModFld_.setText("-1");
		maxValueModFld_.addActionListener(this);
		JPanel listSelector = new JPanel();
		BorderLayout northLayout = new BorderLayout(ItemBuilder.GAP_SIZE,
				ItemBuilder.GAP_SIZE);
		listSelector.setLayout(northLayout);
		listSelector.add(itemTypeCom_, BorderLayout.CENTER);
		listSelector.add(ItemBuilder.createLabelledComponent(maxValueModFld_,
				"Value Mod <= "), BorderLayout.EAST);
		add(listSelector, BorderLayout.NORTH);

		// Adding the list
		JPanel listPanel = new JPanel(new BorderLayout());
		itemsList_ = new JList(new DefaultListModel());
		itemsList_.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		itemsList_.setVisibleRowCount(NUM_ROWS_VISIBLE);
		itemsList_.addListSelectionListener(this);
		JScrollPane listScroll = new JScrollPane(itemsList_);
		listPanel.add(listScroll, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton addItem = ItemBuilder.createButton("Add Item", this);
		buttonPanel.add(addItem);
		JButton removeItem = ItemBuilder.createButton("Remove Item", this);
		buttonPanel.add(removeItem);
		listPanel.add(buttonPanel, BorderLayout.SOUTH);
		add(listPanel, BorderLayout.CENTER);

		// Adding the statistics
		JPanel statsPanel = new JPanel();
		statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
		BoxLayout downLayout = new BoxLayout(statsPanel, BoxLayout.Y_AXIS);
		statsPanel.setLayout(downLayout);
		totalATKLbl_ = new JLabel("Total ATK:");
		statsPanel.add(totalATKLbl_);
		totalDEFLbl_ = new JLabel("Total DEF:");
		statsPanel.add(totalDEFLbl_);
		totalHITLbl_ = new JLabel("Total HIT:");
		statsPanel.add(totalHITLbl_);
		totalEVALbl_ = new JLabel("Total EVA:");
		statsPanel.add(totalEVALbl_);
		statsPanel.add(new JSeparator(JSeparator.HORIZONTAL));

		JButton uncoveredGenres = ItemBuilder.createButton("Uncovered Genres",
				this);
		statsPanel.add(uncoveredGenres);
		add(statsPanel, BorderLayout.SOUTH);

		updateListPanel();
	}

	/**
	 * Updates the list and the statistics regarding the list items using the
	 * item combo and value mod as filters.
	 */
	public void updateListPanel() {
		try {
			// Get the type and clean of '-' chars
			String typeString = (String) itemTypeCom_.getSelectedItem();
			Class typeClass = getCorrespondingClass(typeString);

			// Get the max value mod
			float maxValue = Float.parseFloat(maxValueModFld_.getText());
			if (maxValue == -1)
				maxValue = Float.MAX_VALUE;

			// Run through the list of all items, filtering out those not within
			// the bounds.
			DefaultListModel listModel = (DefaultListModel) itemsList_
					.getModel();
			listModel.clear();
			float[] totals = new float[4];
			float[] variances = new float[4];
			for (Item item : parentFrame_.getTotalItems()) {
				// If the item is of the same or subclass of the type
				if (typeClass.isAssignableFrom(item.getClass())) {
					if (item.getValueMod() <= maxValue) {
						listModel.addElement(item);
						totals[0] += item.getBaseAttack();
						totals[1] += item.getBaseDefense();
						totals[2] += item.getBaseHit();
						totals[3] += item.getBaseEvasion();
						variances[0] += item.getAttackVariance();
						variances[1] += item.getDefenseVariance();
						variances[2] += item.getHitVariance();
						variances[3] += item.getEvasionVariance();
					}
				}
			}

			// Update the statistics
			totalATKLbl_
					.setText(formStatString("ATK", totals[0], variances[0]));
			totalDEFLbl_
					.setText(formStatString("DEF", totals[1], variances[1]));
			totalHITLbl_
					.setText(formStatString("HIT", totals[2], variances[2]));
			totalEVALbl_
					.setText(formStatString("EVA", totals[3], variances[3]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Forms a string for the statistics labels.
	 * 
	 * @param stat
	 *            The stat.
	 * @param statTotal
	 *            The statistic total count.
	 * @param statVariance
	 *            The statistic total variance.
	 * @return A string covering this information.
	 */
	private String formStatString(String stat, float statTotal,
			float statVariance) {
		return "Total " + stat + ": " + (int) statTotal + " ± "
				+ (int) statVariance;
	}

	/**
	 * Gets the corresponding class to the string.
	 * 
	 * @param classString
	 *            The class string.
	 * @return The corresponding class.
	 */
	private Class getCorrespondingClass(String classString) throws Exception {
		return Class.forName("audiabolikal.equipment." + classString);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Uncovered Genres")) {
			// Find the uncovered tags
			// Note that many may be superfluous.
			List<String> tags = new ArrayList<String>(TagHierarchy
					.getInstance().getTags());
			ListModel model = itemsList_.getModel();
			for (int i = 0; i < model.getSize(); i++) {
				Item item = (Item) model.getElementAt(i);
				Set<String> itemGenres = item.getGenres();
				for (String genre : itemGenres) {
					tags.remove(genre);
					tags.removeAll(TagHierarchy.getInstance()
							.getChildren(genre));
				}
			}
			Collections.sort(tags);

			// Display the list of uncovered genres in a separate window
			JDialog dialog = new JDialog(parentFrame_, tags.size()
					+ " Uncovered Genres");
			JList genreList = new JList(tags.toArray(new String[tags.size()]));
			genreList.setVisibleRowCount(NUM_ROWS_VISIBLE);
			JScrollPane scrollPane = new JScrollPane(genreList);
			dialog.add(scrollPane);

			dialog.pack();
			dialog.setVisible(true);
		} else if (e.getActionCommand().equals("Add Item")) {
			// Create a new item (of type headgear as default) with no values.
			Item newItem = new Headgear();
			newItem.initialiseMouldItem("Headgear",
					new HashMap<String, Double>(),
					new ProbabilityDistribution<Color>(), 0, new float[8],
					new File[4], new float[3], new float[] { 1, 1, 1 });
			parentFrame_.addItem(newItem);

			// Add the item to the list and select it, loading up the
			// information in the detail panel.
			setSelectedItem(newItem);
			// valueChanged(null);
		} else if (e.getActionCommand().equals("Remove Item")) {
			parentFrame_.removeItem((Item) itemsList_.getSelectedValue());
			updateListPanel();
		} else {
			updateListPanel();
		}
	}

	/**
	 * Sets an item in the list to be selected.
	 * 
	 * @param newItem
	 *            The item to be selected.
	 */
	public void setSelectedItem(Item newItem) {
		itemTypeCom_.setSelectedIndex(0);
		updateListPanel();
		itemsList_.setSelectedValue(newItem, true);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		Item item = (Item) itemsList_.getSelectedValue();
		if (item != null) {
			if (!item.equals(parentFrame_.getCurrentItem())) {
				// If an item in the list is selected, load up the data in the
				// details and model panels.
				parentFrame_.setCurrentItem(item, this);
			}
		}
	}
}
