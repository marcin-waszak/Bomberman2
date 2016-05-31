import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.midi.InvalidMidiDataException;
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
		midiFile = "sound/music.mid";
	}

	public void play() {
		// Obtains the default Sequencer connected to a default device.

		// Opens the device, indicating that it should now acquire any
		// system resources it requires and become operational.

		URL url = this.getClass().getClassLoader().getResource(midiFile);
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.setSequence(MidiSystem.getSequence(url));
			sequencer.open();
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			sequencer.start();

		} catch (MidiUnavailableException ex) {
			System.err.println("Midi Unavailable! " + ex.toString());
		} catch (InvalidMidiDataException ex) {
			System.err.println("Midi Invalid! " + ex.toString());
		} catch (IOException ex) {
			System.err.println("File Not Found! " + ex.toString());
		}
	}
}