package djview;

public class DJTestDrive {

    // Entry point for the application
    public static void main(String[] args) {
        
        /* 
        1. Create a new model instance, the base for our application which holds 
        the state and logic. 
        => Continued in BeatModule.java
        */
        BeatModelInterface model = new BeatModel();
        
        /* 
        3. Create a new controller instance and pass the just created model to 
        it, thus, connecting the two. The controller will then create the view, 
        so we don't have to do that.
        => Continued in BeatController.java
        */
        ControllerInterface controller = new BeatController(model);
    }
    
}

// MVC - Strategy + Observer + Adapter patterns + hidden Composite Pattern

// The controller implements behavior for the view - not application logic

// Mediator Pattern doesn't apply here. The controller is not a pure mediator,
// since the view does have a reference of the model to access its state.
// If the controller were a mediator, then the view would have to go through 
// the controller to get access to the state of the model. 

// In this implementation, the view has access to methods of the model that can
// change its state, which sometimes is not desirable. 

// ?!! This can be solved by 
// adding a protection proxy which will limit access to the setter methods of 
// the model. ?!! 