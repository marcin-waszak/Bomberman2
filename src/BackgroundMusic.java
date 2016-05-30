import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

public class BackgroundMusic {
	Sequencer sequencer;
	String midiFile;
	
	public BackgroundMusic() {
		try {
			sequencer = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		midiFile = "src/sound/music.mid";
	}	

	public void play() throws Exception {
		// Obtains the default Sequencer connected to a default device.
	
		// Opens the device, indicating that it should now acquire any
	    // system resources it requires and become operational.
		sequencer.open();

	    // create a stream from a file
	    InputStream is = new BufferedInputStream(new FileInputStream(new File(midiFile)));

	    // Sets the current sequence on which the sequencer operates.
	    // The stream must point to MIDI file data.
	    sequencer.setSequence(is);
	    sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);

	    // Starts playback of the MIDI data in the currently loaded sequence.
	    sequencer.start();

	}
}