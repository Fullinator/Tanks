package Main;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.sound.sampled.*;


public class sounds {

	private Clip myClip;
	private boolean soundPlayed = false;
	private AudioFormat af = null;
	private AudioInputStream audioInputStream = null;
	private int size = 0;
	byte[] audio = null;
	DataLine.Info info = null;
	private HashMap<String, Clip> snd = new HashMap<>();
	
	public sounds(){
		//Add menu song
		try {
			URL url = ClassLoader.getSystemResource("sound/titleSong2.wav");
			audioInputStream = AudioSystem.getAudioInputStream(url);
			af = audioInputStream.getFormat();
			size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
			audio = new byte[size];
			info = new DataLine.Info(Clip.class, af, size);
			audioInputStream.read(audio, 0, size);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			myClip = (Clip) AudioSystem.getLine(info);
			myClip.open(af, audio, 0, size);
		}

		catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		snd.put("song", myClip);
		
		//Add movement sound
		try {
			URL url = ClassLoader.getSystemResource("sound/vehicle.wav");
			audioInputStream = AudioSystem.getAudioInputStream(url);
			af = audioInputStream.getFormat();
			size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
			audio = new byte[size];
			info = new DataLine.Info(Clip.class, af, size);
			audioInputStream.read(audio, 0, size);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			myClip = (Clip) AudioSystem.getLine(info);
			myClip.open(af, audio, 0, size);
		}

		catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		snd.put("movement", myClip);
		
		//Add turret sound
		try {
			URL url = ClassLoader.getSystemResource("sound/turret.wav");
			audioInputStream = AudioSystem.getAudioInputStream(url);
			af = audioInputStream.getFormat();
			size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
			audio = new byte[size];
			info = new DataLine.Info(Clip.class, af, size);
			audioInputStream.read(audio, 0, size);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			myClip = (Clip) AudioSystem.getLine(info);
			myClip.open(af, audio, 0, size);
		}

		catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		snd.put("turret", myClip);
		
		//Add shot sound
		try {
			URL url = ClassLoader.getSystemResource("sound/TNT.wav");
			audioInputStream = AudioSystem.getAudioInputStream(url);
			af = audioInputStream.getFormat();
			size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
			audio = new byte[size];
			info = new DataLine.Info(Clip.class, af, size);
			audioInputStream.read(audio, 0, size);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			myClip = (Clip) AudioSystem.getLine(info);
			myClip.open(af, audio, 0, size);
		}

		catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		snd.put("shot1", myClip);
		
		//Add explosion sound
		try {
			URL url = ClassLoader.getSystemResource("sound/TNT.wav");
			audioInputStream = AudioSystem.getAudioInputStream(url);
			af = audioInputStream.getFormat();
			size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
			audio = new byte[size];
			info = new DataLine.Info(Clip.class, af, size);
			audioInputStream.read(audio, 0, size);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			myClip = (Clip) AudioSystem.getLine(info);
			myClip.open(af, audio, 0, size);
		}

		catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		snd.put("impact", myClip);
		
	}

	public void run(String name) {
		snd.get(name).loop(1);
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

	public void runLoop(String name) {

			snd.get(name).loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop(String name) {
		snd.get(name).stop();
	}
}
