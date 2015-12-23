package weijinglab.radioextractor.main;

import weijinglab.radioextractor.kicker.ExtractorKicker;

public class StartMain {

	public static void main(String[] args) {
		ExtractorKicker.runningFlag = true;
		ExtractorKicker.startListening();
	}

}
