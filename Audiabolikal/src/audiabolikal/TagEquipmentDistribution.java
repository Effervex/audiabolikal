package audiabolikal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import audiabolikal.equipment.Item;
import audiabolikal.util.ProbabilityDistribution;

/**
 * A simplicity class for managing the tag distributions for equipment.
 * 
 * @author Samuel J. Sarjant
 */
public class TagEquipmentDistribution {
	/** The tag equipment distribution. */
	private Map<String, Map<String, ProbabilityDistribution<Item>>> tagEquipmentDistribution_;

	/** The tag hierarchy. */
	private TagHierarchy tagHierarchy_;

	/**
	 * Constructor for a new tag equipment distribution
	 */
	public TagEquipmentDistribution() {
		tagHierarchy_ = TagHierarchy.getInstance();
		tagEquipmentDistribution_ = new HashMap<String, Map<String, ProbabilityDistribution<Item>>>();
	}

	/**
	 * Inserts an item into the distribution, under the correct tag/s and item.
	 * 
	 * @param item
	 *            The item being inserted.
	 */
	public void insertItem(Item item) {
		Set<String> associatedGenres = item.getGenres();
		// For every genre the item is linked to.
		for (String genre : associatedGenres) {
			List<String> children = tagHierarchy_.getChildren(genre);
			// Counting the children, if there are some.
			int numChildren = 0;
			Iterator<String> childIter = null;
			if (children != null) {
				numChildren = children.size();
				// For every child that contains the parent as part of it's
				// distro.
				childIter = children.iterator();
			}

			// For this genre and any children beneath it
			for (int i = 0; i < numChildren + 1; i++) {
				double itemWeight = item.getGenreWeight(genre);
				String tag = (i == 0) ? genre : childIter.next();
				// Get/create the equipment distribution
				Map<String, ProbabilityDistribution<Item>> equipmentDistro = tagEquipmentDistribution_
						.get(tag);
				if (equipmentDistro == null) {
					equipmentDistro = new HashMap<String, ProbabilityDistribution<Item>>();
					tagEquipmentDistribution_.put(tag, equipmentDistro);
				}

				// Get/create the particular item distribution
				ProbabilityDistribution<Item> distro = equipmentDistro.get(item
						.getClassName());
				if (distro == null) {
					distro = new ProbabilityDistribution<Item>();
					equipmentDistro
							.put(item.getClassName(), distro);
				}

				// If for a child, modify the weight based on relationship
				// to parent.
				if (i != 0)
					itemWeight *= tagHierarchy_.getParentWeighting(tag, genre);
				
				// If the item already exists in there, use the largest weight
				if (distro.contains(item))
					distro.set(item, Math.max(distro.getProb(item), itemWeight));
				else
					distro.add(item, itemWeight);
			}
		}
	}

	/**
	 * Normalise all the distributions.
	 */
	public void normaliseAll() {
		for (String tagKey : tagEquipmentDistribution_.keySet()) {
			Map<String, ProbabilityDistribution<Item>> equipmentDistro = tagEquipmentDistribution_
					.get(tagKey);
			for (String itemKey : equipmentDistro.keySet()) {
				equipmentDistro.get(itemKey).normaliseProbs();
			}
		}
	}

	/**
	 * Samples an item from the distribution, if the tag has a distribution.
	 * 
	 * @param tag
	 *            The tag to sample from.
	 * @param itemType
	 *            The type of item requested.
	 * @return An item of the type requested from the tag's distribution, or
	 *         null if there are no items/the tag has no distribution.
	 */
	public Item sample(String tag, String itemType) {
		if (tagEquipmentDistribution_.containsKey(tag)) {
			Map<String, ProbabilityDistribution<Item>> equipmentDistro = tagEquipmentDistribution_
					.get(tag);
			if (equipmentDistro.containsKey(itemType)) {
				return equipmentDistro.get(itemType).sample();
			}
		}
		return null;
	}

	/**
	 * Clears the distributions. Used by tests, mainly.
	 */
	public void clear() {
		tagEquipmentDistribution_.clear();
	}

	/**
	 * Gets the probability of a particular item being chosen.
	 * 
	 * @param tag
	 *            The tag under which the item lies.
	 * @param item
	 *            The item being checked.
	 * @return The probability of the item being chosen or -1.
	 */
	public double getProbability(String tag, Item item) {
		// TODO Modify the multimap to deal with the Probability Distribution
		Map<String, ProbabilityDistribution<Item>> eD = tagEquipmentDistribution_
				.get(tag);
		if (eD != null) {
			ProbabilityDistribution<Item> items = eD.get(item.getClass()
					.getSimpleName());
			if (items != null) {
				return items.getProb(item);
			}
		}
		return -1;
	}
}
