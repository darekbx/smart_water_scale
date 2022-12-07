#include <M5Atom.h>
#include <FastLED.h>
#include "HX711.h"

#define DEBUG true
#define SCALE_TIMES_AVERAGE 5
#define THREAD_DELAY 5000
#define COMPARE_DELTA 10.0f

#define LOADCELL_DOUT_PIN 32
#define LOADCELL_SCK_PIN  26
#define PIN_LEDATOM 27

HX711 scale;

float prevWeight = 0.0f;
bool deviceConnected = false;

void setup() {
  M5.begin(true, false, true);

  startBluetooth();

  scale.begin(LOADCELL_DOUT_PIN, LOADCELL_SCK_PIN);
  
  // The scale value is the adc value corresponding to 1g
  scale.set_scale(27.61f);
  scale.tare();
}

void loop() {
  if (deviceConnected) {
    float weight = scale.get_units(SCALE_TIMES_AVERAGE) / 1000.0;
    
    if (weight > 0 && compare_floats(weight, prevWeight, COMPARE_DELTA) != 0) {
      // Notify with Blue that weight was changed
      M5.dis.drawpix(0, 0x00FF00);
      Serial.printf("New weight: %.2fkg \r\n", weight);
      writeData(String(weight));
      prevWeight = weight;
    }

    delay(500);
    
    // Notify with Green that device is connected
    M5.dis.drawpix(0, 0x00FF00);
  } else {
    // Notify with Red color when device is disconnected
    M5.dis.drawpix(0, 0xFF0000);
  }
  
  delay(THREAD_DELAY);
  M5.update();
}

int compare_floats(float a, float b, float delta) {
  if (fabs(a - b) < delta) {
    return 0;
  } else if (a < b) {
    return -1;  // a is less than b
  } else {
    return 1;  // a is greater than b
  }
}
