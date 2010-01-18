package audiabolikal.test;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import audiabolikal.TagHierarchy;
import audiabolikal.WeightedRelation;

import junit.framework.TestCase;

public class TagHierarchyTest extends TestCase {
	private TagHierarchy sut_;

	protected void setUp() throws Exception {
		sut_ = TagHierarchy.getInstance();
	}

	@Test
	public void testGetParentTags() {
		// NOTE: Due to the nature of WeightedRelation equals() method, the
		// weights aren't checked.
		// Null case
		assertNull(sut_.getParentTags("metal"));

		// Single parent
		List<WeightedRelation> parents = sut_.getParentTags("power metal");
		assertEquals(1, parents.size());
		WeightedRelation parent = new WeightedRelation("metal", 1f);
		assertTrue(parents.contains(parent));

		// Two parents
		parents = sut_.getParentTags("thrash metal");
		assertEquals(2, parents.size());
		parent = new WeightedRelation("metal", 1f);
		assertTrue(parents.contains(parent));
		parent = new WeightedRelation("speed metal", 1f);
		assertTrue(parents.contains(parent));

		// Grandparents
		parents = sut_.getParentTags("melodic death metal");
		assertEquals(6, parents.size());
		parent = new WeightedRelation("metal", 0.7f);
		assertTrue(parents.contains(parent));
		parent = new WeightedRelation("death metal", 0.7f);
		assertTrue(parents.contains(parent));
		parent = new WeightedRelation("metalcore", 0.4f);
		assertTrue(parents.contains(parent));
		parent = new WeightedRelation("hardcore", 0.24f);
		assertTrue(parents.contains(parent));
		parent = new WeightedRelation("punk", 0.168f);
		assertTrue(parents.contains(parent));
		parent = new WeightedRelation("rock", 0.096f);
		assertTrue(parents.contains(parent));
	}

	@Test
	public void testGetRoots() {
		Set<String> roots = sut_.getRoots();
		System.out.println("Roots:");
		System.out.println(roots.toString());
		assertTrue(roots.contains("metal"));
		assertFalse(roots.contains("thrash metal"));
	}

	@Test
	public void testGetLeaves() {
		Set<String> leaves = sut_.getLeaves();
		System.out.println("Leaves:");
		System.out.println(leaves.toString());
		assertTrue(leaves.contains("power metal"));
		assertFalse(leaves.contains("rock"));
	}
}
