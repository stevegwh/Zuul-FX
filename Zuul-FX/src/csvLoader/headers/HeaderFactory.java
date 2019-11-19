package csvLoader.headers;

/**
 * Takes the index of the CSV file and determines what Header class it should be.
 * @author Steve
 *
 */
public class HeaderFactory {
	final int csvIndex;
	
	public Header getHeader() {
		if(NameHeader.matchesIndexCondition(csvIndex)) {
			return new NameHeader();
		} else if (DescriptionHeader.matchesIndexCondition(csvIndex)) {
			return new DescriptionHeader();
		} else if (DirectionHeader.matchesIndexCondition(csvIndex)) {
			return new DirectionHeader(csvIndex);
		} else if (ItemNameHeader.matchesIndexCondition(csvIndex)) {
			return new ItemNameHeader();
		} else if (ItemWeightHeader.matchesIndexCondition(csvIndex)) {
			return new ItemWeightHeader();
		} else {
			return null;
		}
	}
	
	public HeaderFactory(int idx) {
		csvIndex = idx;
	}

}
