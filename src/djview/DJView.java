package djview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DJView implements ActionListener, BeatObserver, BPMObserver {
    BeatModelInterface model;
    ControllerInterface controller;
    JFrame viewFrame;
    JPanel viewPanel;
    BeatBar beatBar;
    JLabel bpmOutputLabel;
    JFrame controlFrame;
    JPanel controlPanel;
    JLabel bpmLabel;
    JTextField bpmTextField;
    JButton setBPMButton;
    JButton increaseBPMButton;
    JButton decreaseBPMButton;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem startMenuItem;
    JMenuItem stopMenuItem;
    
    /* 5. The DJView's constructor creates references to both the controller 
    and the model. It also registers the view as an Observer of the model
    instance using the model's API. 
    
    The model makes no assumptions about the view. The model is implemented
    using the Observer Pattern, so it just notifies any view registered
    as an Observer when its state changes.
    
    Except for that, the view uses the model's API (e.g. getBPM()) 
    to get access to the state.
    
    => Continued in BeatController.java
    */
    public DJView(ControllerInterface controller, BeatModelInterface model) {
        this.controller = controller;
        this.model = model;
        model.registerObserver((BeatObserver)this);
        model.registerObserver((BPMObserver)this);
    }
    
    public void createView() {
    // Create all Swing components here
        viewPanel = new JPanel(new GridLayout(1, 2));
        viewFrame = new JFrame("View");
        
        // Setting default exit condition
        viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        viewFrame.setSize(new Dimension(100, 80));
        bpmOutputLabel = new JLabel("offline", SwingConstants.CENTER);
        beatBar = new BeatBar();
        beatBar.setValue(0);
        JPanel bpmPanel = new JPanel(new GridLayout(2, 1));
        bpmPanel.add(beatBar);
        bpmPanel.add(bpmOutputLabel);
        viewPanel.add(bpmPanel);
        viewFrame.getContentPane().add(viewPanel, BorderLayout.CENTER);
        viewFrame.pack();
        viewFrame.setVisible(true);
    }
    
    public void createControls() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        controlFrame = new JFrame("Control");
        
        // Setting default exit condition
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controlFrame.setSize(new Dimension(100, 80));
        
        controlPanel = new JPanel(new GridLayout(1, 2));
        
        menuBar = new JMenuBar();
        menu = new JMenu("DJ Control");
        
        /*
        Creating an ActionListener for each menu item. They register the user
        interactions and they change the model's state THROUGH THE CONTROLLER, 
        not directly (loose coupling).
        */
        startMenuItem = new JMenuItem("Start");
        menu.add(startMenuItem);
        startMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                controller.start();
            }
        });
        
        stopMenuItem = new JMenuItem("Stop");
        menu.add(stopMenuItem);
        stopMenuItem.addActionListener((ActionEvent event) -> {
            controller.stop();
        });
        
        // Adding a non-default exit logic
        JMenuItem exit = new JMenuItem("Quit");
        menu.add(exit);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        
        menuBar.add(menu);
        controlFrame.setJMenuBar(menuBar);
        
        bpmTextField = new JTextField(2);
        bpmLabel = new JLabel("Enter BPM: ", SwingConstants.RIGHT);
        setBPMButton = new JButton("Set");
        setBPMButton.setSize(new Dimension(10, 40));
        increaseBPMButton = new JButton(">>");
        decreaseBPMButton = new JButton("<<");
        setBPMButton.addActionListener(this);
        increaseBPMButton.addActionListener(this);
        decreaseBPMButton.addActionListener(this);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(decreaseBPMButton);
        buttonPanel.add(increaseBPMButton);
        
        JPanel enterPanel = new JPanel(new GridLayout(1, 2));
        enterPanel.add(bpmLabel);
        enterPanel.add(bpmTextField);
        JPanel insideControlPanel = new JPanel(new GridLayout(3, 1));
        insideControlPanel.add(enterPanel);
        insideControlPanel.add(setBPMButton);
        insideControlPanel.add(buttonPanel);
        controlPanel.add(insideControlPanel);
        
        bpmLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bpmOutputLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        controlFrame.getRootPane().setDefaultButton(setBPMButton);
        controlFrame.getContentPane().add(controlPanel, BorderLayout.CENTER);
        
        controlFrame.pack();
        controlFrame.setVisible(true);
    }
    
    public void enableStopMenuItem() {
        stopMenuItem.setEnabled(true);
    }
    
    public void disableStopMenuItem() {
        stopMenuItem.setEnabled(false);
    }
    
    public void enableStartMenuItem() {
        startMenuItem.setEnabled(true);
    }
    
    public void disableStartMenuItem() {
        startMenuItem.setEnabled(false);
    }
    
    /* actionPerformed() is the only abstract method of the ActionListener 
    interface. It is invoked when an action occurs. It is passed an ActionEvent.
    
    From the javadoc: The object that implements the ActionListener interface 
    gets this ActionEvent when the event occurs. The listener is therefore 
    spared the details of processing individual mouse movements and mouse 
    clicks, and can instead process a "meaningful" (semantic) event like 
    "button pressed". 
    */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == setBPMButton) {
            int bpm = 90;
            String bpmText = bpmTextField.getText();
            if (bpmText == null || bpmText.contentEquals("")) {
                bpm = 90;
            } else {
                bpm = Integer.parseInt(bpmTextField.getText());
            }
            controller.setBPM(bpm);
        } else if (event.getSource() == increaseBPMButton) {
            controller.increaseBPM();
        } else if (event.getSource() == decreaseBPMButton) {
            controller.decreaseBPM();
        }
    }
    
    /* UpdateBPM() is called by the model everytime its state changes. It's part
    of the model's NotifyObservers() method and an implementation of the Observer
    Pattern.
    
    This requires coordination between the BeatModel code and the DJView code
    as the BeatModel must know which DJView method to use to notify the observer.
    
    !!! Can this be refactored in a way where the BeatModel doesn't need to know
    the method's name? !!!
    */
    @Override
    public void updateBPM() {
        int bpm = model.getBPM(); // Using the model's API to get information about the new state
        if (bpm == 0) {
            bpmOutputLabel.setText("offline");
        } else {
            bpmOutputLabel.setText("Current BPM: " + model.getBPM());
        }
    }
    
    @Override
    public void updateBeat() {
        beatBar.setValue(100);  // At every pulse, the progression's bar value 
    }                           // is set to 100 and it's left to handle the 
}                               // corresponding animation on its own.
