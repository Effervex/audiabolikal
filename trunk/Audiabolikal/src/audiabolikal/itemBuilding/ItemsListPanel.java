package audiabolikal.itemBuilding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
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
import javax.swing.JTextField;
import javax.swing.ListModel;

import audiabolikal.TagHierarchy;
import audiabolikal.equipment.Item;

public class ItemsListPanel extends JPanel {
	private static final int NUM_ROWS_VISIBLE = 20;
	private Frame parentFrame_;
	private JComboBox itemTypeCom_;
	private JTextField maxValueMod_;
	private JList itemsList_;
	private JLabel totalATK_;
	private JLabel totalDEF_;
	private JLabel totalHIT_;
	private JLabel totalEVA_;

	private Collection<Item> totalItems_;

	public ItemsListPanel(Frame parent) {
		parentFrame_ = parent;
		totalItems_ = new TreeSet<Item>(new ListNameComparator<Item>());
		initialise();
	}

	private void initialise() {
		BorderLayout layout = new BorderLayout(ItemBuilder.GAP_SIZE,
				ItemBuilder.GAP_SIZE);
		setLayout(layout);

		setBorder(BorderFactory.createEtchedBorder());

		// Item combo box and value mod
		String[] itemTypes = initialiseItemTypes();
		itemTypeCom_ = new JComboBox(itemTypes);
		maxValueMod_ = new JTextField(8);
		maxValueMod_.setText("-1");
		JPanel listSelector = new JPanel();
		BorderLayout northLayout = new BorderLayout(ItemBuilder.GAP_SIZE,
				ItemBuilder.GAP_SIZE);
		listSelector.setLayout(northLayout);
		listSelector.add(itemTypeCom_, BorderLayout.CENTER);
		listSelector.add(ItemBuilder.createLabelledComponent(maxValueMod_,
				"Value Mod <= "), BorderLayout.EAST);
		add(listSelector, BorderLayout.NORTH);

		// Adding the list
		itemsList_ = new JList(new DefaultListModel());
		itemsList_.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		itemsList_.setVisibleRowCount(NUM_ROWS_VISIBLE);
		JScrollPane listScroll = new JScrollPane(itemsList_);
		add(listScroll, BorderLayout.CENTER);

		// Adding the statistics
		JPanel statsPanel = new JPanel();
		statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
		BoxLayout downLayout = new BoxLayout(statsPanel, BoxLayout.Y_AXIS);
		statsPanel.setLayout(downLayout);
		totalATK_ = new JLabel("Total ATK:");
		statsPanel.add(totalATK_);
		totalDEF_ = new JLabel("Total DEF:");
		statsPanel.add(totalDEF_);
		totalHIT_ = new JLabel("Total HIT:");
		statsPanel.add(totalHIT_);
		totalEVA_ = new JLabel("Total EVA:");
		statsPanel.add(totalEVA_);
		JButton uncoveredGenres = new JButton("Uncovered Genres");
		uncoveredGenres.addActionListener(new UncoveredGenresActionListener());
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
			typeString = typeString.replaceAll("-", "");
			Class typeClass = getCorrespondingClass(typeString);

			// Get the max value mod
			float maxValue = Float.parseFloat(maxValueMod_.getText());
			if (maxValue == -1)
				maxValue = Float.MAX_VALUE;

			// Run through the list of all items, filtering out those not within
			// the bounds.
			DefaultListModel listModel = (DefaultListModel) itemsList_
					.getModel();
			float[] totals = new float[4];
			float[] variances = new float[4];
			for (Item item : totalItems_) {
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
			totalATK_.setText(formStatString("ATK", totals[0], variances[0]));
			totalDEF_.setText(formStatString("DEF", totals[1], variances[1]));
			totalHIT_.setText(formStatString("HIT", totals[2], variances[2]));
			totalEVA_.setText(formStatString("EVA", totals[3], variances[3]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Forms a string for the statistics labels.
	 * 
	 * @param stat The stat.
	 * @param statTotal The statistic total count.
	 * @param statVariance The statistic total variance.
	 * @return A string covering this information.
	 */
	private String formStatString(String stat, float statTotal, float statVariance) {
		return "Total " + stat + ": " + (int) statTotal + " ± " + (int) statVariance;
	}

	/**
	 * Initialises the item types.
	 * 
	 * @return The item types.
	 */
	private String[] initialiseItemTypes() {
		String[] itemTypes = { "Item", "-Headgear", "-Face", "-Aura",
				"-Attire", "-Footwear", "-Weapon", "--OneHanded",
				"--TwoHanded", "--DualWield", "--AttackAndDefense" };
		return itemTypes;
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

	/**
	 * A simple class which compares strings.
	 * 
	 * @author Samuel J. Sarjant
	 */
	private class ListNameComparator<T> implements Comparator<T> {

		@Override
		public int compare(T o1, T o2) {
			return o1.toString().compareTo(o2.toString());
		}

	}

	/**
	 * An action listener for a button to open the uncovered genres dialog.
	 * 
	 * @author Samuel J. Sarjant
	 */
	private class UncoveredGenresActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
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
		}
	}
}
