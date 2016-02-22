package Main;
import java.io.File;
import javax.sound.sampled.*;


public class sounds {

	private Clip myClip;
	private boolean soundPlayed = false;
	private boolean runOnce = false;
	private AudioFormat af = null;
	private AudioInputStream audioInputStream = null;
	private int size = 0;
	byte[] audio = null;
	DataLine.Info info = null;

	public void loadSound(String fileName) {

		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(
					fileName));
			af = audioInputStream.getFormat();
			size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
			audio = new byte[size];
			info = new DataLine.Info(Clip.class, af, size);
			audioInputStream.read(audio, 0, size);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {

		try {
			myClip = (Clip) AudioSystem.getLine(info);
			myClip.open(af, audio, 0, size);
		}

		catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		myClip.start();
	}

	public void runOnce() {
		if (!soundPlayed) {
			try {
				myClip = (Clip) AudioSystem.getLine(info);
				myClip.open(af, audio, 0, size);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}

			myClip.start();
			soundPlayed = true;
		}
	}

	public void runLoop() {
		if (!runOnce) {
			try {
				myClip = (Clip) AudioSystem.getLine(info);
				myClip.open(af, audio, 0, size);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			myClip.loop(Clip.LOOP_CONTINUOUSLY);
			runOnce = true;
		}
	}

	public void stop() {
		myClip.stop();
	}
}
