package djview;

public class BeatController implements ControllerInterface {
    BeatModelInterface model;
    DJView view;
    
    
    /*
    4. The BeatController constructor is called by DJTestDrive.java and a new
    BeatController instance is created, with access to the BeatModel instance 
    that was created by DJTestDrive.java in the previous step. From the book:
    The controller is the object that gets to hold on to the view and the model
    and glues it all together.
    
    The instance variable this.model is assigned the create BeatModel instance.
    
    The variable "view" is not declared or initialized as an instance-, but       !!! Why not instance variable? 
    rather as a local variable.
    
    DJViews methods are called to create the GUI and the model is then 
    initialized.
    
    => Continued in DJView.java 
    */
    public BeatController(BeatModelInterface model) {   
        this.model = model; // Use BeatModelInterface, rather than BeatModel                         
                            // reference type to adhere to polymorphic      
                            // principles. This way the same controller can be  
                            // also used with other BeatModels who implement 
                            // this interface.
        
        // The controller is passed the model in the constructor and the creates
        // the view.
        view = new DJView(this, model); // Instantiating the DJView object
                                        // with references to this controller
                                        // object and the model object this 
                                        // controller already "knows" about.
                                        
        /*
        6. The controller tells the view to create its compotents and sets
        the view's initial state.
        */
        view.createView();
        view.createControls();
        view.disableStopMenuItem();
        view.enableStartMenuItem();
        
        /*
        7. The controller tells the model to initialize. This loads and opens
        the audio clip passed in BeatModel's initialize() method.
        
        At this point we have an initialized and visible view, so the user can 
        start interacting. The view is made visible to the user by the 
        createView()'s and createControls()'s method setVisible(). Those two
        methods also set the closing/exiting conditions.
        
        We have an initialized model that's still off, which 
        awaits to be turned on by user interaction. However, the user will NOT
        interact directly with the model. Once the user chooses "Start" in
        the view, the CONTROLLER will be notified about the user interaction
        first. It's the controller's job to make sense of the user input. In
        this case, choosing "Start" calls the controller's start() method and 
        it's THAT CONTROLLER's method that will turn the model on and will
        also partially update the view itself. The fact that the view doesn't
        directly control the model is part of the loose coupling rule of the 
        Observer Pattern.
        */
        model.initialize();
    }
    
    /*
    From the book: The controller is making the intelligent decisions for the 
    view. The view just knows HOW to turn menu items on and off; it doesn't 
    know the situations in which it should do so.
    */
    
    // This method is called by the View when the "Start" button is acted upon
    @Override
    public void start() {
        model.on();
        view.disableStartMenuItem();
        view.enableStopMenuItem();
    }
    
    // This method is called by the View when the "Stop" button is acted upon
    @Override
    public void stop() {
        model.off();
        view.disableStopMenuItem();
        view.enableStartMenuItem();
    }
    
    @Override
    public void increaseBPM() {
        int bpm = model.getBPM();
        model.setBPM(bpm + 1);
    }
    
    @Override
    public void decreaseBPM() {
        int bpm = model.getBPM();
        model.setBPM(bpm - 1);
    }
    
    @Override
    public void setBPM(int bpm) {
        model.setBPM(bpm);
    }
}
