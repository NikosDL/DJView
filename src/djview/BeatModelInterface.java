package djview;


public interface BeatModelInterface {
    
    // Methods to be used by the controller to direct the model based on user interaction
    void initialize(); // Gets called after BeatModel is instantiated
    
    void on(); // Turns the beat generator on
    
    void off(); // Turns the beat generator off
    
    void setBPM(int bpm); // Sets the beats per minute - after it's called the beat frequency changes immediately
    
    
    // Methods to allow the view and the controller to get state and to become observers
    int getBPM(); // returns the current BPMs or 0 if the generator is off
    
    /* Two categories of observers: those who want to be notified on every beat 
    and observers that just want to be notified when the BPMs change. */
    void registerObserver(BeatObserver o); 
    
    void removeObserver(BeatObserver o);
    
    void registerObserver(BPMObserver o);
    
    void removeObserver(BPMObserver o);
}
