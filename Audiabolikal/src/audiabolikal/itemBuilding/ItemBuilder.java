package audiabolikal.itemBuilding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import audiabolikal.Parser;
import audiabolikal.equipment.Item;

/**
 * A class for defining and viewing the items present in the system.
 * 
 * @author Samuel J. Sarjant
 */
public class ItemBuilder extends JFrame implements ActionListener {
	public static final int GAP_SIZE = 5;
	public static final int BORDER = 10;
	protected ItemsListPanel itemsList_;
	protected ItemDetailsPanel itemDetails_;
	protected ItemModelPanel itemModel_;
	private Item currentItem_;
	private Collection<Item> totalItems_;
	private JMenuBar mainMenu_;
	JFileChooser fc_;

	public ItemBuilder() {
		totalItems_ = new TreeSet<Item>(new ListNameComparator<Item>());
		fc_ = new JFileChooser();
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

		mainMenu_ = new JMenuBar();
		JMenu main = new JMenu("File");
		JMenuItem load = new JMenuItem("Load...");
		load.setActionCommand("Load");
		load.addActionListener(this);
		JMenuItem save = new JMenuItem("Save...");
		save.setActionCommand("Save");
		save.addActionListener(this);
		main.add(load);
		main.add(save);
		mainMenu_.add(main);
		setJMenuBar(mainMenu_);
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
		JPanel labelComp = new JPanel(new BorderLayout(GAP_SIZE, GAP_SIZE));
		labelComp.add(new JLabel(componentLabel), BorderLayout.WEST);
		labelComp.add(component, BorderLayout.CENTER);
		return labelComp;
	}

	/**
	 * Creates a button linked to an action listener with the action command
	 * equal to the button text.
	 * 
	 * @param buttonText
	 *            The text of the button and the action command.
	 * @param al
	 *            The action listener.
	 * @return The newly created button.
	 */
	public static JButton createButton(String buttonText, ActionListener al) {
		JButton addItem = new JButton(buttonText);
		addItem.setActionCommand(buttonText);
		addItem.addActionListener(al);
		return addItem;
	}

	/**
	 * Initialises the item types.
	 * 
	 * @param includeAbstracts
	 *            If the list of items include abstract item types also.
	 * @return The item types.
	 */
	public static String[] getItemTypes(boolean includeAbstracts) {
		if (includeAbstracts) {
			String[] itemTypes = { "Item", "Headgear", "Face", "Aura",
					"Attire", "Footwear", "Weapon", "OneHanded", "TwoHanded",
					"DualWield", "AttackAndDefense" };
			return itemTypes;
		} else {
			String[] itemTypes = { "Headgear", "Face", "Aura", "Attire",
					"Footwear", "OneHanded", "TwoHanded", "DualWield",
					"AttackAndDefense" };
			return itemTypes;
		}
	}

	/**
	 * Gets the current item being shown.
	 * 
	 * @return The current item.
	 */
	public Item getCurrentItem() {
		return currentItem_;
	}

	/**
	 * Sets the current item.
	 * 
	 * @param newItem
	 *            The item to be the current item.
	 * @param callingPanel
	 *            The panel which called this method.
	 */
	public void setCurrentItem(Item newItem, JPanel callingPanel) {
		// If our calling panel was the items list
		if (callingPanel.equals(itemsList_)) {
			// Load the item up in the details and the model panels
			itemDetails_.loadItemDetails(newItem);
			itemModel_.loadItemDetails(newItem);
			// Otherwise, if our calling panel is the details, the new item may
			// be NEW
		} else if (callingPanel.equals(itemDetails_)) {
			// If the new item is not in the list (different class)
			if (!newItem.equals(currentItem_)) {
				totalItems_.remove(currentItem_);
				totalItems_.add(newItem);
			}
			itemsList_.setSelectedItem(newItem);
			itemModel_.loadItemDetails(newItem);
		}

		currentItem_ = newItem;
	}

	/**
	 * Gets the total items.
	 * 
	 * @return The collection of all items.
	 */
	public Collection<Item> getTotalItems() {
		return totalItems_;
	}

	/**
	 * Adds an item to the total items.
	 * 
	 * @param newItem
	 *            The item being added
	 */
	public void addItem(Item newItem) {
		totalItems_.add(newItem);
		currentItem_ = newItem;
		itemDetails_.loadItemDetails(currentItem_);
		itemModel_.loadItemDetails(currentItem_);
	}

	/**
	 * Removes an item from the total items.
	 * 
	 * @param item
	 *            The item to be removed.
	 */
	public void removeItem(Item item) {
		if (totalItems_.remove(item))
			currentItem_ = null;
		itemDetails_.loadItemDetails(currentItem_);
		itemModel_.loadItemDetails(currentItem_);
	}

	protected JFileChooser getFileChooser() {
		return fc_;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals("Load")) {
				fc_.setFileFilter(new CSVFilter());
				int result = fc_.showOpenDialog(this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fc_.getSelectedFile();

					FileReader reader = new FileReader(file);
					BufferedReader bf = new BufferedReader(reader);

					String strItem = bf.readLine();
					if ((strItem != null) && (!strItem.equals(""))) {
						Item loaded = Parser.parseItem(strItem);
						addItem(loaded);
					}

					bf.close();
					reader.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Loads an item from string format into the program.
	 * 
	 * @param strItem
	 *            The string representation of the item details.
	 */
	private void loadItem(String strItem) {
		
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

	private class CSVFilter extends FileFilter {
		private static final String ITEMFILE_EXTENSION = "csv";

		@Override
		public boolean accept(File pathname) {
			if (pathname.isDirectory())
				return true;

			String filename = pathname.getName();
			if (filename.toLowerCase().endsWith(ITEMFILE_EXTENSION))
				return true;
			return false;
		}

		@Override
		public String getDescription() {
			return "Comma-separated item files.";
		}

	}
}
