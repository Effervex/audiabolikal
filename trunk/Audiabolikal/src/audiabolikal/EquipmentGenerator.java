package audiabolikal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import audiabolikal.equipment.*;
import audiabolikal.util.ProbabilityDistribution;

/**
 * The class that handles generation of equipment, given a tag and a desired
 * item type. The equipment generation distributions are initialised by forming
 * distributions from existing equipment.
 * 
 * @author Samuel J. Sarjant
 */
public class EquipmentGenerator {
	/** The singleton instance. */
	private static EquipmentGenerator instance_;

	/** The equipment distribution. */
	private TagEquipmentDistribution ted_;

	/**
	 * A constructor.
	 * 
	 * @param items
	 *            The collection of all possible items that could be generated.
	 */
	private EquipmentGenerator(Collection<Item> items) {
		ted_ = formDistributions(items);
	}

	/**
	 * Forms the distributions of the items across tags and equipment.
	 * 
	 * @param items
	 *            The items to be added to the distributions.
	 * @param tagHierarchy
	 *            The tag hierarchy.
	 */
	private TagEquipmentDistribution formDistributions(Collection<Item> items) {
		TagEquipmentDistribution ted = new TagEquipmentDistribution();
		// Run through each item, adding it to necessary distributions
		for (Item item : items) {
			ted.insertItem(item);
		}

		// Normalise all distributions
		ted.normaliseAll();
		return ted;
	}

	/**
	 * Generates headgear for a tag.
	 * 
	 * @param tag
	 *            The tag for the distribution.
	 * @return Headgear for that tag, or null if tag doesn't exist/has no items.
	 */
	public Headgear generateHeadgear(String tag) {
		return (Headgear) generateItem(tag, Headgear.class.getName());
	}

	/**
	 * Generates face for a tag.
	 * 
	 * @param tag
	 *            The tag for the distribution.
	 * @return Face for that tag, or null if tag doesn't exist/has no items.
	 */
	public Face generateFace(String tag) {
		return (Face) generateItem(tag, Face.class.getName());
	}

	/**
	 * Generates aura for a tag.
	 * 
	 * @param tag
	 *            The tag for the distribution.
	 * @return Aura for that tag, or null if tag doesn't exist/has no items.
	 */
	public Aura generateAura(String tag) {
		return (Aura) generateItem(tag, Aura.class.getName());
	}

	/**
	 * Generates attire for a tag.
	 * 
	 * @param tag
	 *            The tag for the distribution.
	 * @return Attire for that tag, or null if tag doesn't exist/has no items.
	 */
	public Attire generateAttire(String tag) {
		return (Attire) generateItem(tag, Attire.class.getName());
	}

	/**
	 * Generates footwear for a tag.
	 * 
	 * @param tag
	 *            The tag for the distribution.
	 * @return Footwear for that tag, or null if tag doesn't exist/has no items.
	 */
	public Footwear generateFootwear(String tag) {
		return (Footwear) generateItem(tag, Footwear.class.getName());
	}

	/**
	 * Generates weapon for a tag.
	 * 
	 * @param tag
	 *            The tag for the distribution.
	 * @return Weapon for that tag, or null if tag doesn't exist/has no items.
	 */
	public Weapon generateWeapon(String tag) {
		return (Weapon) generateItem(tag, Weapon.class.getName());
	}

	/**
	 * Generates an item from a tag's particular item distribution.
	 * 
	 * @param tag
	 *            The tag for the distribution.
	 * @param itemType
	 *            The type of item being generated.
	 * @return An item of the requested type from the tag's distribution, or
	 *         null if the tag doesn't exist/has no items.
	 */
	private Item generateItem(String tag, String itemType) {
		return ted_.sample(tag, itemType);
	}

	public static EquipmentGenerator initInstance(Collection<Item> items) {
		instance_ = new EquipmentGenerator(items);
		return instance_;
	}

	/**
	 * Gets the singleton instance.
	 * 
	 * @return The singleton instance.
	 */
	public static EquipmentGenerator getInstance() {
		return instance_;
	}
}
