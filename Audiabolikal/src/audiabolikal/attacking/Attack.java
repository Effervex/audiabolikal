package audiabolikal.attacking;

import java.util.Collection;

import audiabolikal.Parser;

import net.roarsoftware.lastfm.Track;
import net.roarsoftware.lastfm.User;

/**
 * This class represents types of attacks, and how those attacks are used.
 * 
 * @author Samuel J. Sarjant
 */
public class Attack {
	/** The proportion of attack exp received from user charts. */
	private static final float FREE_ATTACK_EXP_COEFFICIENT = 0.1f;

	/** The last track count from the user's chart. */
	private int trackCount_;

	/** The track this attack represents. */
	private Track attackTrack_;

	/** The attributes this attack has (damage percent, range, poison, etc.) */
	private AttackAttributes attackAttributes_;

	/** The point of the attack song which starts up during the attack. */
	private float attackChorusPoint = 0.5f;

	/**
	 * Checks if the track count has increased. If it has increased to a point
	 * where free attack exp can be gained, the method returns true.
	 * 
	 * @param user
	 *            The user of the tracks.
	 * @return True if free exp was given, false otherwise.
	 */
	public boolean checkTrackCount(User user) {
		Collection<Track> topTracks = User.getTopTracks(user.getName(),
				Parser.API_KEY);
		
		if (topTracks.contains(attackTrack_)) {
			// TODO Find it and its count.
		}

		return false;
	}
}
