package dom.model.challenge;

/**
 * Enum containing different possible statuses for challenges.
 * 
 * The status of a challenge is stored in a status column in the challenges table.
 * The status column is of type INT.
 * 
 * How do we get the integer value?
 * For example:
 * ChallengeStatus.refused.ordinal() will return 1.
 * 1 represents the position of "refused" in the enum declaration (initial constant, "open", is assigned 0).
 * 
 * @author vartanbeno
 *
 */
public enum ChallengeStatus {
	
	open, refused, withdrawn, accepted;

}
