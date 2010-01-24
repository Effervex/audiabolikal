package audiabolikal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import audiabolikal.util.MultiMap;

/**
 * A class which organises the tags into a hierarchical graph. The graph must
 * not contain loops! This class is used to share information between general
 * tags (e.g. metal -> thrash metal -> melodic thrash metal). The class also
 * contains weights between relationships to denote amount of similarity.
 * 
 * @author Samuel J. Sarjant
 */
public class TagHierarchy {
	public static final String TAG_FILE = "tagHierarchy.csv";

	/** The singleton instance. */
	private static TagHierarchy instance_;

	/** The tag relations, given higher tags and their weights. */
	private MultiMap<String, WeightedRelation> relations_;

	/**
	 * The inverse of the tag relations, denoting which non-leaf tags are
	 * parents of other tags, for efficient loop checking.
	 */
	private MultiMap<String, String> childMap_;
	
	/** The set of all tags seen. */
	private Set<String> tags_;

	/** The roots of the hierarchy (no parents). */
	private Set<String> roots_;

	/** The children of the hierarchy (no children). */
	private Set<String> leaves_;

	/**
	 * Constructor for a new Tag Hierarchy
	 */
	private TagHierarchy() {
		initialise(new File(Audiabolikal.RESOURCE_DIR + TAG_FILE));
	}

	/**
	 * Initialisation methods.
	 * 
	 * @param tagFile
	 *            The tag file to read from.
	 */
	private void initialise(File tagFile) {
		MultiMap<String, WeightedRelation> tuples = new MultiMap<String, WeightedRelation>();
		tags_ = new HashSet<String>();

		try {
			FileReader reader = new FileReader(tagFile);
			BufferedReader bf = new BufferedReader(reader);

			// Read each line, storing the data.
			String input = null;
			while ((input = bf.readLine()) != null) {
				String[] split = input.split(",");
				// Clean up any " marks
				for (int i = 0; i < split.length; i++) {
					split[i] = split[i].replaceAll("\"", "");
				}
				tags_.add(split[0]);
				tags_.add(split[1]);

				// Check if the key already exists, and overwrite it if better
				WeightedRelation wr = new WeightedRelation(split[1], Float
						.parseFloat(split[2]));
				if (!tuples.putContains(split[0], wr)) {
					addWeightedRelation(null, new WeightedRelation(split[1],
							Float.parseFloat(split[2])), tuples.get(split[0]));
				}
			}

			bf.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		relations_ = flattenTuples(tuples);
	}

	/**
	 * Flattens the tuples into a map of a single tag key, and values of parent
	 * tags with associated weights. The flattening process involves recursively
	 * searching through the tuples and joining parents to children.
	 * 
	 * @param tuples
	 *            The mapping of tuples, extending only to immediate parents.
	 * @return A joined mapping of tuples, extending beyond parents.
	 */
	private MultiMap<String, WeightedRelation> flattenTuples(
			MultiMap<String, WeightedRelation> tuples) {
		MultiMap<String, WeightedRelation> flattened = new MultiMap<String, WeightedRelation>();

		// The children efficiency map
		childMap_ = new MultiMap<String, String>();
		// Two sets for examining the structure of the hierarchy.
		leaves_ = new HashSet<String>();
		roots_ = new HashSet<String>();

		// Recursively run through each tag key in the tuples map
		for (String tagKey : tuples.keySet()) {
			Set<String> loopTrace = new HashSet<String>();
			recursiveParentFind(tagKey, flattened, tuples, loopTrace);
		}
		return flattened;
	}

	/**
	 * Recursively flattens the parent hierarchy down. Stopping case is when
	 * tuples does not contain the tag as a key, or the key has already been
	 * found.
	 * 
	 * @param tagKey
	 *            The tag key being searched for.
	 * @param flattened
	 *            The flattened set.
	 * @param tuples
	 *            The mapping of each individual unflattened tuple.
	 * @param loopTrace
	 *            A collection for tracing loops.
	 */
	private List<WeightedRelation> recursiveParentFind(String tagKey,
			MultiMap<String, WeightedRelation> flattened,
			MultiMap<String, WeightedRelation> tuples, Set<String> loopTrace) {
		// Stopping condition: If tuples doesn't contain the key return null.
		if (!tuples.containsKey(tagKey)) {
			roots_.add(tagKey);
			return null;
		}
		// If key has already been searched, return the found set.
		if (flattened.containsKey(tagKey)) {
			return flattened.get(tagKey);
		}
		// Checking for loops
		if (loopTrace.contains(tagKey)) {
			System.out.println("LOOP FOUND: " + tagKey);
			System.out.println("TRACE: " + loopTrace.toString());
			return null;
		}

		List<WeightedRelation> flattenedParents = new ArrayList<WeightedRelation>();
		loopTrace.add(tagKey);

		// Find the set of parents to the tuple
		List<WeightedRelation> parents = tuples.get(tagKey);
		for (WeightedRelation parent : parents) {
			// Check if the parent tag has parents
			List<WeightedRelation> grandparents = recursiveParentFind(parent
					.getRelation(), flattened, tuples, loopTrace);
			if (grandparents != null) {
				// Add the grandparents, using the weight to modify weighting.
				for (WeightedRelation grandParent : grandparents) {
					addWeightedRelation(tagKey, new WeightedRelation(
							grandParent.getRelation(), grandParent.getWeight()
									* parent.getWeight()), flattenedParents);
				}

				// If a grandparent was searched, remove it from leaves
				leaves_.remove(tagKey);
			}

			// Add the parent itself
			addWeightedRelation(tagKey, parent, flattenedParents);
			// Cautiously add it as a leaf
			leaves_.add(tagKey);
		}

		// Add the tag key to the flattened list, so it isn't looped.
		flattened.putCollection(tagKey, flattenedParents);
		loopTrace.remove(tagKey);

		return flattenedParents;
	}

	/**
	 * Adds a parent to a set only if it isn't already in there, or it has a
	 * greater weight than the one already present. Also notes the
	 * parent-to-child mapping.
	 * 
	 * @param child
	 *            The child of the relation being added.
	 * @param weightedRelation
	 *            The weighted relation being added.
	 * @param bestRelations
	 *            The existing set to be added to.
	 */
	private void addWeightedRelation(String child,
			WeightedRelation weightedRelation,
			List<WeightedRelation> bestRelations) {
		int index = bestRelations.indexOf(weightedRelation);

		// If the set doesn't contain the relation, add it.
		if (index == -1) {
			bestRelations.add(weightedRelation);

			if (child != null) {
				// Adding to the child map
				childMap_.putContains(weightedRelation.getRelation(), child);
			}
		} else {
			WeightedRelation inList = bestRelations.get(index);
			// If the new relation is better than the old, replace the old with
			// the new.
			if (weightedRelation.getWeight() > inList.getWeight()) {
				bestRelations.remove(index);
				bestRelations.add(weightedRelation);
			}
		}
	}

	/**
	 * Gets the parent tags for this tag.
	 * 
	 * @param tag
	 *            The tag child.
	 * @return The parents of tag, or null if none.
	 */
	public List<WeightedRelation> getParentTags(String tag) {
		if (relations_.containsKey(tag))
			return relations_.get(tag);
		return null;
	}

	/**
	 * Gets the roots of the hierarchy.
	 * 
	 * @return The roots (no parents).
	 */
	public Set<String> getRoots() {
		return roots_;
	}

	/**
	 * Gets the leaves of the hierarchy.
	 * 
	 * @return The leaves (no children).
	 */
	public Set<String> getLeaves() {
		return leaves_;
	}

	/**
	 * Gets the children of a parent tag.
	 * 
	 * @param parent
	 *            The parent tag of the children.
	 * @return The children of the parent, or null.
	 */
	public List<String> getChildren(String parent) {
		return childMap_.get(parent);
	}

	/**
	 * Gets the parent weighting for a child tag: how much the parent affects
	 * the child.
	 * 
	 * @param child
	 *            The child tag, the one which has a parent of weight 1 or less.
	 * @param parent
	 *            The parent tag of the child, with a corresponding weight.
	 * @return The weight of the parent, or 0 if the parent does not exist.
	 */
	public double getParentWeighting(String child, String parent) {
		List<WeightedRelation> parents = relations_.get(child);
		if (parents != null) {
			// Find the relation using the parent as a lookup
			WeightedRelation wr = new WeightedRelation(parent, 1);
			int index = parents.indexOf(wr);
			if (index != -1)
				return parents.get(index).getWeight();
		}
		return 0;
	}

	/**
	 * Gets every tag seen in the hierarchy.
	 * 
	 * @return Every tag in the hierarchy.
	 */
	public Collection<String> getTags() {
		return tags_;
	}

	/**
	 * Gets the singleton instance.
	 * 
	 * @return The singleton instance.
	 */
	public static TagHierarchy getInstance() {
		if (instance_ == null) {
			instance_ = new TagHierarchy();
		}
		return instance_;
	}
}
