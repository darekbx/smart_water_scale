#include <M5Atom.h>
#include <FastLED.h>
#include "HX711.h"

#define DEBUG true

#define LOADCELL_DOUT_PIN 32
#define LOADCELL_SCK_PIN  26

HX711 scale;

bool deviceConnected = false;
const byte PIN_LEDATOM = 27;

void setup() {
  M5.begin(true, false, true);

  startBluetooth();

  scale.begin(LOADCELL_DOUT_PIN, LOADCELL_SCK_PIN);
  
  // The scale value is the adc value corresponding to 1g
  scale.set_scale(27.61f);
  scale.tare();
}

void loop() {
  float weight = scale.get_units(1) / 1000.0;
  if (weight >= 0) {
      Serial.printf("Weight: %.2fkg \r\n", weight);
  } else {
      Serial.println("Weight: 0.00kg");
  }
    
  if (M5.Btn.wasPressed()) {
    // Tare scale on button press
    //scale.tare();
    if (deviceConnected) {
      writeData(String(weight));
      M5.dis.drawpix(0, 0xFF0000);
    }
  } else if (M5.Btn.wasReleased()) {
    M5.dis.drawpix(0, 0xFFFFFF);
  }

  delay(50);
  M5.update();
}