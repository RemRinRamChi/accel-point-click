# accel-point-click (project-13-b)
accel-point-click is a Android library for accelerometer-based pointing and clicking. It supports:

2 control modes for pointing:
- Velocity-control - Pointer acts like a ball under the effects of gravity.
- Position-control - Pointer has an initial position and tilt the device moves the pointer away from the initial position.

4 ways of clicking:
- *Bezel Swiping* - Small swipe away from any position of the device’s bezel (edge frame of the device) triggers a click.
- *Floating Button* - A small floating button for clicking, that can be moved around or overlayed at the bezel of the device.
- *Volume Down Button* - Volume down button is overridden to register a click when pressed on.
- *Back Tapping* - Tapping the back of the device triggers a click.

## Usage
The pointer exists in a separate full screen view and is added to the activity either through the activity's code or the layout file. 
The following is an example of adding the pointer through code.

``` java
// Declare MouseView object as a field of the activity class
MouseView mouseView;
```

``` java
// In the onCreate method, instantiate the MouseView and add it the root view group of the activity
mouseView = new MouseView(this);

// This is an example of adding to a root view that is a constraint layout
ConstraintLayout constraintLayout = findViewById(R.id.root_layout);
constraintLayout.addView(mouseView, -1, MouseView.getFullScreenConstraintLayoutParams());

// Set the clicking target for the MouseView
mouseView.setClickingTargetView(findViewById(R.id.root_layout));
```

The following lifecycle methods of tbe activity will need to be overriden. It ensures that resources are freed up when the application is not running.
```java
@Override
protected void onPause() {
    super.onPause();
    mouseView.pause();
}

@Override
protected void onResume() {
    super.onResume();
    mouseView.resume();
}
```

#### Customising pointer control method
Setting the control mode
``` java
// For position-control
mouseView.enablePositionControl(true);

// For velocity-control
mouseView.enablePositionControl(false);

```

Configuring tilt gain settings, the settings which determines the step sizes of the pointer
``` java
// For position-control
mouseView.setPosTiltGain(TILT_GAIN);

// For velocity-control
mouseView.setVelTiltGain(TILT_GAIN);
```

#### Customising pointer clicking method
Choosing the clicking method for the pointer
```java
// Bezel Swiping
mouseView.setClickingMethod(ClickingMethod.BEZEL_SWIPE);

// Floating Button
MovableFloatingActionButton movableButtonView = new MovableFloatingActionButton(this);
constraintLayout.addView(movableButtonView, constraintLayout.getChildCount(),MouseView.getFabConstraintLayoutParams(100,0));
mouseView.setMovableFloatingActionButton(movableButtonView);
mouseView.setClickingMethod(ClickingMethod.FLOATING_BUTTON);

// Volume Down Button
mouseView.setClickingMethod(ClickingMethod.VOLUME_DOWN);

// Back Tapping
mouseView.setClickingMethod(ClickingMethod.BACK_TAP);

```
Back Tapping requires the installation of a [separate application](https://play.google.com/store/apps/details?id=com.prhlt.aemus.BoDTapService) to work. The application starts a service that will listen to back taps.

Customising the floating button
```java
hmm yeah
```



## Download
Import the AAR file