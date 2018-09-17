# accel-point-click (project-13-b)
accel-point-click is a Android library for accelerometer-based pointing and clicking. It supports:

2 control modes for pointing:
- **Velocity-control** - Pointer acts like a ball under the effects of gravity.
- **Position-control** - Pointer has an initial position and tilt the device moves the pointer away from the initial position.

4 ways of clicking:
- **Bezel Swiping** - Small swipe away from any position of the device’s bezel (edge frame of the device) triggers a click.
- **Floating Button** - A small floating button for clicking, that can be moved around or overlayed at the bezel of the device.
- **Volume Down Button** - Volume down button is overridden to register a click when pressed on.
- **Back Tapping** - Tapping the back of the device triggers a click.

## Using the library in your project
Before you can use the accel-point-click functionalities in your app, you must first download and import the library into your project. The minimum API level requirement for the library is 21.
### Downloading the library
Download the ```accel-point-click.aar``` library file from the releases folder.

### Importing the library into your project
To import the accel-point-click library, you must move the .AAR file into the libs folder of your app module

Next, you need to update your project's ```build.gradle``` file to allow the libs folder to be tracked
```
allprojects {
    repositories {
        ...
        flatDir {
            dirs 'libs'
        }
        ...
    }
}
```

Now, update your app module's ```build.gradle``` to import the library into the app module
```
dependencies {
    ...
    implementation(name:'accel-point-click', ext:'aar')
    ...
}
```

Now you can begin using the library!

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

The following lifecycle methods of the activity will need to be overriden. It ensures that resources are freed up when the application is not running.
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
mouseView.setPosTiltGain(tiltGain);

// For velocity-control
mouseView.setVelTiltGain(tiltGain);
```

More customisation options
```java
// Sets the bitmap of the pointer
mouseView.setMouseBitmap(bitmap);

// Sets the reference pitch added to the rest pitch of 0, when the device is laid flat
mouseView.setRefPitch(refPitch);
// same as setRefPitch but the refPitch value is the current pitch of the device
mouseView.calibratePitch();

// Sets the initial point of the pointer
mouseView.setXReference(xRef);
mouseView.setYReference(yRef);

// Enables recalibration of the pointer using the volume up button
mouseView.enableRecalibrationByVolumeUp(true);

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
Back Tapping requires the installation of a [separate application](https://play.google.com/store/apps/details?id=com.prhlt.aemus.BoDTapService) to work. The application will need to be launch and will start a service that will listen to back taps.
You will need to click ```Start BTAP Service```, then give it the appropriate permissions by opening the settings.

#### Customising the movable floating button
There are 3 customization options for the movable floating button.

```java
// Set Floating Button Color
// Default color is cyan
movableButtonView.setButtonColor(Color.CYAN);       // Give an int value of a color that you want

// Set Floating Button Size
// Default size is 200
movableButtonView.setButtonSize(200);               // Give an int value of the preffered button size in pixels

// Set Floating Button Opacity 
// Default opacity is 0.1f
movableButtonView.setButtonOpacity(0.1f);           // To set the opacity, give a floating point value between 0-1.
```

## Demo Application
The Accel World demo application is used to test the functionalities provided by the accel-point-click libray. The minimum API level requirement for the application is 21.
### Installation guide
Download the ```accel-world.apk``` file from the releases folder. Run the apk on your device to install the application.

### Note
Back Tapping to click requires the installation of a [separate application](https://play.google.com/store/apps/details?id=com.prhlt.aemus.BoDTapService) to work. The application will need to be launch and will start a service that will listen to back taps.
You will need to click ```Start BTAP Service```, then give it the appropriate permissions by opening the settings.


## Changes from Project Plan
* Added position-control of accelerometer-based pointer
* Added the ability to recalibrate the accelerometer so that the accelerometer can be used in any position comfortably
* Demo application now demonstrates the library with 3 different scenarios (Wikipedia page, Number dial pad, Keyboard) 
    * Instead of a screen with circular button targets of different clusterings (distance away from another button) and sizes
    * Demo application is still used for testing, so the independent variables {Target Size, Clustered} will change to {Scenario}