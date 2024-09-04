
package djview;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;

public class BeatModel implements BeatModelInterface, Runnable {
    List<BeatObserver> beatObservers = new ArrayList<>();
    List<BPMObserver> bpmObservers = new ArrayList<>();
    int bpm = 90;
    Thread thread;
    boolean stop = false;
    Clip clip;
    
    /* 
    2. Since a constructor is not explicitly implemented, the default
    constructor will be called by the compiler:
    
    public BeatModel() {}
    
    The constructor is added to the stack. Since there is no superclass, there 
    are no super() constructors to be called and the BeatModel object is
    directly instantiated.
    
    A BeatModel instance will be instantiated, initially with the attributes
    declared/initialized above. The methods of the class/instance will now 
    be available to the controller once it's also instantiated.
    
    The BeatModel instance has no access to the controller.
    
    => Continued in DJTestDrive.java
    */
    
    @Override
    public void initialize() {
        try {
            File resource = new File("clap.wav");
            clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
            clip.open(AudioSystem.getAudioInputStream(resource));
        } catch(Exception ex) {
            System.out.println("Error: Can't load clip.");
            System.out.println(ex);
        }
    }
    
    @Override
    public void on() {
        bpm = 90;
        notifyBPMObservers();
        thread = new Thread(this);  // Creating a separate Thread to run the 
        stop = false;               // run() method's while loop.
        thread.start();             // Starts the newly created thread and,
    }                               // since BeatModel implements Runnable,
                                    // thread.start() also automatically calls
                                    // the overriden run() method.
    
    @Override
    public void off() {
        stopBeat();
        stop = true;
    }
    
    /* Overriden from Runnable Interface. The method run() must be overriden.
    From the javadoc: When an object implementing interface Runnable is used to
    create a thread, starting the thread causes the object's run methon to be
    called in that separately executing thread.
    
    The general contract of the method run is that it may take any action
    whatsoever.
    */
    @Override
    public void run() {
        while (!stop) {
            playBeat();
            notifyBeatObservers();
            try {
                Thread.sleep(60000/getBPM());   // This will make sure to slow
            } catch(Exception ex) {}            // down the thread's execution
        }                                       // of the while loop to the 
    }                                           // current BPM.
    
    @Override
    public void setBPM(int bpm) {
        this.bpm = bpm;
        notifyBPMObservers();
    }
    
    @Override
    public int getBPM() {
        return bpm;
    }
    
    @Override
    public void registerObserver(BeatObserver o) {
        beatObservers.add(o);
    }
    
    public void notifyBeatObservers() {
        for (int i = 0; i < beatObservers.size(); i++) {
            BeatObserver observer = (BeatObserver)beatObservers.get(i);
            observer.updateBeat(); // !!! Can this be refactored to a generic method where it's not required for BeatModel to know the name of the method?
        }
    }
    
    @Override
    public void registerObserver(BPMObserver o) {
        bpmObservers.add(o);
    }
    
    public void notifyBPMObservers() {
        for (int i = 0; i < bpmObservers.size(); i++) {
            BPMObserver observer = (BPMObserver)bpmObservers.get(i);
            observer.updateBPM(); // !!! Can this be refactored to a generic method where it's not required for BeatModel to know the name of the method?
        }
    }
    
    @Override
    public void removeObserver(BeatObserver o) {
        int i = beatObservers.indexOf(o);
        if (i >= 0) {
            beatObservers.remove(i);
        }
    } 
    
    @Override
    public void removeObserver(BPMObserver o) {
        int i = bpmObservers.indexOf(o);
        if (i >= 0) {
            bpmObservers.remove(i);
        }
    }
    
    public void playBeat() {
        clip.setFramePosition(0);
        clip.start();
    }
    
    public void stopBeat() {
        clip.setFramePosition(0);
        clip.stop();
    }
}
